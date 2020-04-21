package com.max.tour.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.max.tour.R;
import com.max.tour.bean.Route;
import com.max.tour.common.MyActivity;
import com.max.tour.event.RouteEvent;
import com.max.tour.event.SearchEvent;
import com.max.tour.helper.DataUtils;
import com.max.tour.helper.DbHelper;
import com.max.tour.http.model.HttpData;
import com.max.tour.ui.adapter.BusRouteAdapter;
import com.max.tour.ui.adapter.RouteAdpter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class RouteActivity extends MyActivity implements RouteSearch.OnRouteSearchListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.layout_back)
    RelativeLayout mLayoutBack;
    @BindView(R.id.input_location_start)
    TextView mInputLocationStart;
    @BindView(R.id.layout_start)
    LinearLayout mLayoutStart;
    @BindView(R.id.type_icon)
    ImageView mTypeIcon;
    @BindView(R.id.input_location_end)
    TextView mInputLocationEnd;
    @BindView(R.id.layout_end)
    LinearLayout mLayoutEnd;
    @BindView(R.id.tabLayout)
    CommonTabLayout mTabLayout;
    @BindView(R.id.bus_recyclerView)
    RecyclerView mBusRecyclerView;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private ArrayList<CustomTabEntity> mTabEntity = new ArrayList<>();
    private String[] mTitles = {"驾车", "公交", "步行"};

    LinearLayoutManager mLayoutManager;
    LinearLayoutManager mBusLayoutManager;
    List<Route> mList;
    RouteAdpter mAdapter;
    private ProgressDialog progDialog = null;


    List<BusPath> mBusList;
    BusRouteAdapter mBusAdapter;


    private LatLonPoint startPoint = new LatLonPoint(39.903588, 116.47357);
    private LatLonPoint endPoint = new LatLonPoint(39.993253, 116.473195);

    private int routeType = 0;

    private RouteSearch mRouteSearch;

    private String currentCityName = "北京";

    private DriveRouteResult mDriveRouteResult;
    private BusRouteResult mBusRouteResult;
    private WalkRouteResult mWalkRouteResult;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_route;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        for (String mTitle : mTitles) {
            mTabEntity.add(new CustomTabEntity() {
                @Override
                public String getTabTitle() {
                    return mTitle;
                }

                @Override
                public int getTabSelectedIcon() {
                    return 0;
                }

                @Override
                public int getTabUnselectedIcon() {
                    return 0;
                }
            });
        }
        mTabLayout.setTabData(mTabEntity);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                routeType = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mTabLayout.setCurrentTab(0);

        mRouteSearch = new RouteSearch(getActivity());
        mRouteSearch.setRouteSearchListener(this);

        mList = new ArrayList<>();
        mAdapter = new RouteAdpter(mList);
        mAdapter.setOnItemClickListener(this);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mBusList = new ArrayList<>();
        mBusAdapter = new BusRouteAdapter(mBusList);
        mBusAdapter.setOnItemClickListener(this);
        mBusLayoutManager = new LinearLayoutManager(this);
        mBusRecyclerView.setLayoutManager(mBusLayoutManager);
        mBusRecyclerView.setAdapter(mBusAdapter);


    }

    @Override
    protected void initData() {

        queryRoutes();

    }

    private void queryRoutes() {
        Observable
                .create(new ObservableOnSubscribe<HttpData<List<Route>>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<List<Route>>> emitter) throws Exception {

                        List<Route> list = DbHelper.queryRoute();
                        emitter.onNext(DataUtils.getInstance().getData(200, "", list));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<List<Route>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<List<Route>> data) {
                        if (data.getData() != null) {
                            mList.clear();
                            mList.addAll(data.getData());
                            mAdapter.setNewData(mList);


                        } else {
                            ToastUtils.showShort("查询失败，请联系管理员");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @OnClick({R.id.layout_back, R.id.layout_start, R.id.layout_end, R.id.tv_search})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_back:
                RouteEvent event = new RouteEvent();
                EventBus.getDefault().post(event);
                finish();
                break;

            case R.id.layout_start:
                intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("tag", 1);
                startActivity(intent);

                break;
            case R.id.layout_end:
                intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("tag", 2);
                startActivity(intent);
                break;
            case R.id.tv_search:
                if (TextUtils.isEmpty(mInputLocationStart.getText().toString())
                        || TextUtils.isEmpty(mInputLocationEnd.getText().toString())) {
                    ToastUtils.showShort("请输入出发点和终点");
                    return;
                }
                saveRoute();


                break;
            default:
                break;
        }
    }

    private void saveRoute() {
        Observable
                .create(new ObservableOnSubscribe<HttpData<Boolean>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<Boolean>> emitter) throws Exception {

                        boolean result = DbHelper.insertRoute(mInputLocationStart.getText().toString(), mInputLocationEnd.getText().toString(),
                                startPoint.getLatitude(), startPoint.getLongitude(), endPoint.getLatitude(), endPoint.getLongitude());
                        emitter.onNext(DataUtils.getInstance().getData(200, "", result));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<Boolean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<Boolean> data) {
                        if (data.getData()) {
                            // 查询路线
                            searchRoute(routeType);
                        } else {
                            ToastUtils.showShort("存储失败，请联系管理员");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void searchRoute(int routeType) {

        if (startPoint == null) {
            ToastUtils.showShort("定位中，稍后再试...");
            return;
        }
        if (endPoint == null) {
            ToastUtils.showShort("终点未设置");
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                startPoint, endPoint);
        if (routeType == 0) {
            // 驾车路径规划
            // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_MULTI_STRATEGY_FASTEST_SHORTEST_AVOID_CONGESTION, null,
                    null, "");
            // 异步路径规划驾车模式查询
            mRouteSearch.calculateDriveRouteAsyn(query);
        } else if (routeType == 1) {
            // 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BUS_DEFAULT,
                    currentCityName, 0);
            // 异步路径规划公交模式查询
            mRouteSearch.calculateBusRouteAsyn(query);
        } else if (routeType == 2) {
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo);
            mRouteSearch.calculateWalkRouteAsyn(query);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(SearchEvent event) {
        if (event != null) {
            if (1 == event.getType()) {
                //  出发位置
                String place = event.getLocationName();
                double lon = event.getLon();
                double lat = event.getLau();

                startPoint.setLongitude(lon);
                startPoint.setLatitude(lat);

                mInputLocationStart.setText(place);

            } else if (2 == event.getType()) {
                //终点
                String place = event.getLocationName();
                double lon = event.getLon();
                double lat = event.getLau();
                endPoint.setLongitude(lon);
                endPoint.setLatitude(lat);

                mInputLocationEnd.setText(place);
            }
        }
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null) {
            progDialog = new ProgressDialog(getActivity());
        }
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        dissmissProgressDialog();
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {

                    mBusList.clear();
                    mBusList.addAll(result.getPaths());
                    mBusAdapter.setNewData(mBusList);

                    mRecyclerView.setVisibility(View.GONE);
                    mBusRecyclerView.setVisibility(View.VISIBLE);

                    mBusRouteResult = result;


                } else if (result != null && result.getPaths() == null) {
                    ToastUtils.showShort(R.string.no_result);
                }
            } else {
                ToastUtils.showShort(R.string.no_result);
            }
        } else {
            ToastUtils.showShort(errorCode);
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        dissmissProgressDialog();
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    RouteEvent event = new RouteEvent();
                    event.setTag(0);
                    event.setDriveRouteResult(mDriveRouteResult);
                    EventBus.getDefault().post(event);
                    finish();
                } else {
                    ToastUtils.showShort(R.string.no_result);
                }
            } else {
                ToastUtils.showShort(R.string.no_result);
            }
        } else {
            ToastUtils.showShort(errorCode);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {

                    final WalkPath walkPath = result.getPaths().get(0);
                    if (walkPath == null) {
                        return;
                    }
                    RouteEvent event = new RouteEvent();
                    event.setTag(2);
                    event.setWalkRouteResult(result);
                    EventBus.getDefault().post(event);
                    finish();


                } else if (result != null && result.getPaths() == null) {
                    ToastUtils.showShort(R.string.no_result);
                }
            } else {
                ToastUtils.showShort(R.string.no_result);
            }
        } else {
            ToastUtils.showShort(errorCode);
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        if (adapter instanceof RouteAdpter) {
            Route route = mList.get(position);
            startPoint.setLatitude(route.getStartLatitude());
            startPoint.setLongitude(route.getStartLongitude());
            endPoint.setLatitude(route.getEndLatitude());
            endPoint.setLongitude(route.getEndLongitude());

            // 查询路线
            searchRoute(routeType);
        } else {
            RouteEvent event = new RouteEvent();
            event.setTag(1);
            event.setBusRouteResult(mBusRouteResult);
            EventBus.getDefault().post(event);
            finish();

        }

    }


    @Override
    public void onBackPressed() {
        RouteEvent event = new RouteEvent();
        EventBus.getDefault().post(event);
        super.onBackPressed();

    }
}
