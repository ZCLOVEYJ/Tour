package com.max.tour.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.max.tour.R;
import com.max.tour.bean.Sights;
import com.max.tour.event.SearchEvent;
import com.max.tour.helper.DbHelper;
import com.max.tour.ui.activity.SearchActivity;
import com.max.tour.ui.activity.SightDetailsActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2019, Relx
 * HomeFragment
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-14, ZhengChen, Create file
 */
public class HomeFragment extends Fragment implements PoiSearch.OnPoiSearchListener, AMapLocationListener,
        AMap.OnMarkerClickListener, AMap.OnMapClickListener,
        AMap.OnInfoWindowClickListener, AMap.OnCameraChangeListener {

    public static final String KEYWORDS = "风景名胜";


    private View mView;
    MapView mMapView = null;

    LinearLayout mLayoutSearch;
    TextView mTvKeywords;
    ImageView mIvClose;

    /**
     * 权限
     */
    RxPermissions mRxPermissions;

    AMap aMap;
    /**
     * Location样式
     */
    MyLocationStyle mLocationStyle;

    /**
     * 定位
     */
    AMapLocationClient mLocationClient;
    AMapLocationClientOption mLocationOptions;

    List<PoiItem> mList = new ArrayList<>();

    int mPosition = -1;

    /**
     * poi返回的结果
     */
    private PoiResult poiResult;
    private int currentPage = 1;
    /**
     * Poi查询条件类
     */
    private PoiSearch.Query query;
    /**
     * POI搜索
     */
    private PoiSearch poiSearch;//
    private Marker mPoiMarker;

    private String mCityCode;
    private String mCityName;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, null);
        // 初始化权限
        initPermissions(savedInstanceState);
        return mView;
    }

    /**
     * 初始化权限
     */
    @SuppressLint("CheckResult")
    private void initPermissions(Bundle savedInstanceState) {

        mRxPermissions = new RxPermissions(this);
        mRxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(
                grant -> {
                    if (grant) {
                        LogUtils.i("已同意权限");
                        // 初始化MapView
                        initMap(savedInstanceState);
                        initSearchLayout();


                    } else {
                        LogUtils.i("拒绝权限");
                    }
                }
        );
    }

    private void initSearchLayout() {
        mLayoutSearch = mView.findViewById(R.id.layout_search);
        mTvKeywords = mView.findViewById(R.id.tv_keywords);
        mIvClose = mView.findViewById(R.id.iv_close);
        mLayoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("tag", 0);
                startActivity(intent);

            }
        });
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvClose.setVisibility(View.GONE);
                mTvKeywords.setText("");
                doSearchQuery(KEYWORDS, mCityCode);
            }
        });

    }

    /**
     * 初始化地图map
     *
     * @param savedInstanceState bundle
     */
    private void initMap(Bundle savedInstanceState) {
        //获取地图控件引用
        mMapView = (MapView) mView.findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
            //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
            //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
            mLocationStyle = new MyLocationStyle();
            //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            mLocationStyle.interval(2000);
            mLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
            //设置定位蓝点的Style
            aMap.setMyLocationStyle(mLocationStyle);
            //设置默认定位按钮是否显示，非必需设置。
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);

            aMap.setOnMapClickListener(this);
            aMap.setOnInfoWindowClickListener(this);
            aMap.setOnMarkerClickListener(this);
            aMap.setOnCameraChangeListener(this);
            aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    View infoWidow = LayoutInflater.from(getActivity()).inflate(
                            R.layout.layout_info_window, null);

                    render(marker, infoWidow);

                    return infoWidow;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });

            //声明AMapLocationClient类对象
            mLocationClient = new AMapLocationClient(getActivity());
            mLocationClient.setLocationListener(this);
            mLocationOptions = new AMapLocationClientOption();
            //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
            mLocationOptions.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
            mLocationClient.setLocationOption(mLocationOptions);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
            mLocationClient.stopLocation();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPoiSearched(PoiResult result, int i) {

        if (i == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {
                    poiResult = result;
                    //是否是同一条
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        initMarker(poiItems);
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtils.showShort("对不起，没有搜索到相关数据！");
                    }

                }

            }
        }

    }

    /**
     * 初始化标记
     *
     * @param pois PoiItem
     */
    private void initMarker(List<PoiItem> pois) {
        mList.clear();
        mList.addAll(pois);
        for (int i = 0; i < mList.size(); i++) {
            LatLng latLng = new LatLng(pois.get(i).getLatLonPoint().getLatitude(), pois.get(i).getLatLonPoint().getLongitude());
            String title = pois.get(i).getTitle();
            String url = pois.get(i).getPhotos().size() > 0 ? pois.get(i).getPhotos().get(0).getUrl() : "";
            aMap.addMarker(new MarkerOptions().position(latLng).title(title).snippet(url).period(1));

        }
        LatLng latLng = new LatLng(pois.get(0).getLatLonPoint().getLatitude(), pois.get(0).getLatLonPoint().getLongitude());
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));


    }

    /**
     * 宣软
     *
     * @param marker
     * @param infoWidow
     */
    private void render(Marker marker, View infoWidow) {
        ImageView ivPic = infoWidow.findViewById(R.id.iv_pic);
        TextView tvTitle = infoWidow.findViewById(R.id.tv_title);
        TextView tvAddress = infoWidow.findViewById(R.id.tv_address);
        tvTitle.setText(marker.getTitle());
        if (1 == marker.getPeriod()) {
            Glide.with(this).load(marker.getSnippet()).into(ivPic);
            ivPic.setVisibility(View.VISIBLE);
            tvAddress.setVisibility(View.GONE);
        } else {
            ivPic.setVisibility(View.GONE);
            tvAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(marker.getSnippet());
        }


    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        // 设置地图比例
        LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        //初始化位置信息的所有景点信息
        mCityCode = aMapLocation.getCityCode();
        mCityName = aMapLocation.getCity();
        doSearchQuery(KEYWORDS, mCityCode);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        for (int i = 0; i < mList.size(); i++) {
            if (marker.getTitle().equals(mList.get(i).getTitle())) {
                mPosition = i;
            }
        }
        if (mPosition != -1) {
            DbHelper.saveTourByLocation(mList.get(mPosition));
        }
        return true;
    }


    @Override
    public void onMapClick(LatLng latLng) {

        if (aMap.getMapScreenMarkers().get(mPosition).isInfoWindowShown()) {
            aMap.getMapScreenMarkers().get(mPosition).hideInfoWindow();
            mPosition = -1;
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        Intent intent = new Intent(getActivity(), SightDetailsActivity.class);
        intent.putExtra("lat_lon", marker.getPosition());
        getActivity().startActivity(intent);

        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            mPosition = -1;
        }

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(SearchEvent event) {
        if (event != null) {
            if (1 == event.getTag()) {
                // 关键子搜索
                aMap.clear();
                String keywords = event.getKeywords();
                if (keywords != null && !keywords.equals("")) {
                    doSearchQuery(keywords, "");
                }
                mTvKeywords.setText(keywords);
                if (!keywords.equals("")) {
                    mIvClose.setVisibility(View.VISIBLE);
                }

            } else if (2 == event.getTag()) {
                // 这是tip搜索
                aMap.clear();
                Tip tip = event.getTip();
                if (tip.getPoiID() == null || tip.getPoiID().equals("")) {
                    doSearchQuery(tip.getName(), mCityCode);
                } else {
                    doSearchQuery(tip.getName(), "");
                }
                mTvKeywords.setText(tip.getName());
                if (!tip.getName().equals("")) {
                    mIvClose.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String keywords, String cityCode) {
        //showProgressDialog();// 显示进度框
        currentPage = 1;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(TextUtils.isEmpty(keywords) ? KEYWORDS : keywords, "", cityCode);
        // 设置每页最多返回多少条poiitem
        query.setPageSize(100);
        // 设置查第一页
        query.setPageNum(currentPage);

        poiSearch = new PoiSearch(getActivity(), query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }


    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtils.showShort(infomation);

    }
}
