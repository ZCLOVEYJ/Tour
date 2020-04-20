package com.max.tour.ui.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.blankj.utilcode.util.ToastUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.max.tour.R;
import com.max.tour.common.MyFragment;
import com.max.tour.event.RouteEvent;
import com.max.tour.event.SearchEvent;
import com.max.tour.ui.activity.MainActivity;
import com.max.tour.ui.activity.SearchActivity;
import com.max.tour.ui.adapter.RouteAdpter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2019, Relx
 * RouteFragment
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-14, ZhengChen, Create file
 */
public class RouteFragment extends MyFragment<MainActivity> implements RouteSearch.OnRouteSearchListener {

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
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private ArrayList<CustomTabEntity> mTabEntity = new ArrayList<>();
    private String[] mTitles = {"驾车", "公交", "步行"};

    LinearLayoutManager mLayoutManager;
    RouteAdpter mAdapter;
    private ProgressDialog progDialog = null;

    private LatLonPoint startPoint = new LatLonPoint(39.903588, 116.47357);
    private LatLonPoint endPoint = new LatLonPoint(39.993253, 116.473195);

    private int routeType = 0;

    private RouteSearch mRouteSearch;

    private String currentCityName = "北京";

    private DriveRouteResult mDriveRouteResult;


    public static RouteFragment newInstance() {
        return new RouteFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_route;
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

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.layout_start, R.id.layout_end, R.id.tv_search})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {

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
                // 查询路线
                searchRoute(routeType);

                break;
            default:
                break;
        }
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
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVEING_PLAN_DEFAULT, null,
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

                    final BusPath busPath = result.getPaths().get(0);
                    if (busPath == null) {
                        return;
                    }
                    RouteEvent event = new RouteEvent();
                    event.setTag(1);
                    event.setBusPath(busPath);
                    event.setBusRouteResult(result);
                    EventBus.getDefault().post(event);


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
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    if (drivePath == null) {
                        return;
                    }
                    RouteEvent event = new RouteEvent();
                    event.setTag(0);
                    event.setDrivePath(drivePath);
                    event.setDriveRouteResult(mDriveRouteResult);
                    EventBus.getDefault().post(event);


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
                    event.setWalkPath(walkPath);
                    event.setWalkRouteResult(result);
                    EventBus.getDefault().post(event);


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
}
