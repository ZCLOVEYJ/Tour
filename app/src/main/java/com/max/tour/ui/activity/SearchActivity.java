package com.max.tour.ui.activity;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.max.tour.R;
import com.max.tour.common.MyActivity;
import com.max.tour.event.SearchEvent;
import com.max.tour.ui.adapter.TipAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class SearchActivity extends MyActivity implements SearchView.OnQueryTextListener,
        Inputtips.InputtipsListener, BaseQuickAdapter.OnItemClickListener, GeocodeSearch.OnGeocodeSearchListener {


    @BindView(R.id.searchView)
    SearchView mSearchView;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    List<Tip> mTipList;

    TipAdapter mAdapter;

    LinearLayoutManager mLayoutManager;

    private int mTag;

    private GeocodeSearch geocoderSearch;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {

        ImmersionBar.with(this).statusBarColor(R.color.white).fullScreen(true).autoDarkModeEnable(true);

        mTag = getIntent().getIntExtra("tag", -1);

        initSearchView();

        mTipList = new ArrayList<>();
        mAdapter = new TipAdapter(mTipList);
        mAdapter.setOnItemClickListener(this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        init();
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(this);
        //设置SearchView默认为展开显示
        mSearchView.setIconified(false);
        mSearchView.onActionViewExpanded();
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setSubmitButtonEnabled(false);
    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.layout_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 开始进行poi搜索
     */
    protected void init() {
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    public void getLatlon(final String name, String adcode) {

        // 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        GeocodeQuery query = new GeocodeQuery(name, adcode);
        // 设置同步地理编码请求
        geocoderSearch.getFromLocationNameAsyn(query);
    }


    @Override
    public boolean onQueryTextSubmit(String keywords) {
        if (mTag == 0) {
            SearchEvent event = new SearchEvent();
            event.setTag(1);
            event.setKeywords(keywords);
            EventBus.getDefault().post(event);
            this.finish();
            return false;
        } else if (mTag == 1 || mTag == 2) {
            getLatlon(keywords, "010");
            return false;
        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        if (!TextUtils.isEmpty(s)) {
            InputtipsQuery inputquery = new InputtipsQuery(s, "北京");
            Inputtips inputTips = new Inputtips(SearchActivity.this.getApplicationContext(), inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        } else {
            if (mAdapter != null && mTipList != null) {
                mTipList.clear();
                mAdapter.notifyDataSetChanged();
            }
        }
        return false;
    }

    @Override
    public void onGetInputtips(List<Tip> list, int rCode) {
        // 正确返回
        if (rCode == 1000) {
            mTipList = list;
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < list.size(); i++) {
                listString.add(list.get(i).getName());
            }
            mAdapter.setNewData(mTipList);

        } else {
            ToastUtils.showShort(rCode + "");
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mTipList != null) {
            Tip tip = mTipList.get(position);
            if (mTag == 0) {

                SearchEvent event = new SearchEvent();
                event.setTag(2);
                event.setKeywords("");
                event.setTip(tip);
                EventBus.getDefault().post(event);
                this.finish();

            } else if (mTag == 1 || mTag == 2) {
                getLatlon(tip.getName(), tip.getAdcode());

            }
        }


    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);

                SearchEvent event = new SearchEvent();
                event.setLocationName(result.getGeocodeQuery().getLocationName());
                event.setLon(address.getLatLonPoint().getLongitude());
                event.setLau(address.getLatLonPoint().getLatitude());
                event.setType(mTag);
                EventBus.getDefault().post(event);
                finish();
            }
        } else {
            ToastUtils.showShort(rCode + "");
        }
    }
}
