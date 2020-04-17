package com.max.tour.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.amap.api.maps2d.model.PolygonOptions;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.max.tour.R;
import com.max.tour.bean.Sights;
import com.max.tour.helper.DbHelper;
import com.max.tour.ui.activity.SightDetailsActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

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
public class HomeFragment extends Fragment implements PoiSearch.OnPoiSearchListener, AMapLocationListener, AMap.OnMarkerClickListener,
        DistrictSearch.OnDistrictSearchListener, GeocodeSearch.OnGeocodeSearchListener,
        AMap.OnMapClickListener, AMap.OnInfoWindowClickListener, AMap.OnCameraChangeListener, AMap.OnMapTouchListener {


    private View mView;
    MapView mMapView = null;
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

    boolean isFirstClick = true;


    public static HomeFragment newInstance() {
        return new HomeFragment();
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
                    } else {
                        LogUtils.i("拒绝权限");
                    }
                }
        );
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
    public void onPoiSearched(PoiResult poiResult, int i) {
        LogUtils.i("-------onPoiSearched-------", poiResult.getPois());

        initMarker(poiResult.getPois());
    }

    /**
     * 初始化标记
     *
     * @param pois PoiItem
     */
    private void initMarker(ArrayList<PoiItem> pois) {
        mList.addAll(pois);
        for (int i = 0; i < mList.size(); i++) {
            LatLng latLng = new LatLng(pois.get(i).getLatLonPoint().getLatitude(), pois.get(i).getLatLonPoint().getLongitude());
            String title = pois.get(i).getTitle();
            String url = pois.get(i).getPhotos().size() > 0 ? pois.get(i).getPhotos().get(0).getUrl() : "";
            aMap.addMarker(new MarkerOptions().position(latLng).title(title).snippet(url));
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

        }

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
        tvTitle.setText(marker.getTitle());
        Glide.with(this).load(marker.getSnippet()).into(ivPic);


    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
        LogUtils.i("-------onPoiItemSearched-------", poiItem);
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        LogUtils.i("-------onLocationChanged-------", aMapLocation.getCityCode());
        // 设置地图比例

        LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());//构造一个位置
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        //初始化位置信息的所有景点信息
        initLocationPlace(aMapLocation.getCityCode());
    }

    /**
     * 初始化景点信息
     *
     * @param cityCode 城市编码
     */
    private void initLocationPlace(String cityCode) {
        PoiSearch.Query query = new PoiSearch.Query("风景名胜", "", cityCode);
        query.setPageSize(100);
        PoiSearch poiSearch = new PoiSearch(getActivity(), query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LogUtils.i("-------onMarkerClick-------", marker.getTitle(), marker.getSnippet());
        marker.showInfoWindow();
        for (int i = 0; i < mList.size(); i++) {
            if (marker.getTitle().equals(mList.get(i).getTitle())) {
                mPosition = i;
            }
        }
        if (mPosition != -1) {
//            cavasDis(mList.get(mPosition));
            saveDb(mList.get(mPosition));

        }
        return true;
    }

    /**
     * 保存景点信息到数据库
     *
     * @param item
     */
    private void saveDb(PoiItem item) {
        DbHelper.findTourByLocation(item);


    }


    private void cavasDis(PoiItem item) {
        DistrictSearch search = new DistrictSearch(getActivity());
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords(item.getTitle());//传入关键字
        query.setShowBoundary(true);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(this);//绑定监听器
        search.searchDistrictAsyn();//开始搜索
        //
        GeocodeSearch geocoderSearch = new GeocodeSearch(getActivity());
        geocoderSearch.setOnGeocodeSearchListener(this);
        GeocodeQuery queryGeo = new GeocodeQuery(item.getTitle(), "010");
        geocoderSearch.getFromLocationNameAsyn(queryGeo);
    }

    @Override
    public void onDistrictSearched(DistrictResult districtResult) {
        LogUtils.i("-----" + districtResult.getAMapException().getErrorCode() + "------");
        if (districtResult.getAMapException().getErrorCode() == 1000) {
            ArrayList<DistrictItem> list = districtResult.getDistrict();

            LogUtils.i("-----" + list.size() + "------");
            // 声明 多边形参数对象
            PolygonOptions polygonOptions = new PolygonOptions();
            // 添加 多边形的每个顶点（顺序添加）
            for (int i = 0; i < list.size(); i++) {
                LatLng latLng = new LatLng(list.get(i).getCenter().getLatitude(), list.get(i).getCenter().getLongitude());
                polygonOptions.add(latLng);
            }
            polygonOptions.strokeWidth(15) // 多边形的边框
                    .strokeColor(Color.argb(50, 1, 1, 1)) // 边框颜色
                    .fillColor(Color.argb(1, 1, 1, 1));   // 多边形的填充色

            aMap.addPolygon(polygonOptions);
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        LogUtils.i("--------onRegeocodeSearched-------" + regeocodeResult + "------------");
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        LogUtils.i("--------onGeocodeSearched-------" + geocodeResult + "------------");
        LogUtils.i(geocodeResult.getGeocodeAddressList().size());
    }

    @Override
    public void onMapClick(LatLng latLng) {
        for (int i = 0; i < aMap.getMapScreenMarkers().size(); i++) {
            if (aMap.getMapScreenMarkers().get(i).isInfoWindowShown()) {
                aMap.getMapScreenMarkers().get(i).hideInfoWindow();
                mPosition = -1;
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        for (int i = 0; i < mList.size(); i++) {
            if (marker.getTitle().equals(mList.get(i).getTitle())) {
                mPosition = i;
            }
        }
        if (mPosition != -1) {

            Sights bean = DbHelper.findSightByLatLon(mList.get(mPosition));
            if (bean != null) {
                Intent intent = new Intent(getActivity(), SightDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("sight", bean);
                intent.putExtra("value", bundle);
                getActivity().startActivity(intent);

                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                    mPosition = -1;
                }
            }


        }


    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        LogUtils.i("-----------onCameraChange------------");
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LogUtils.i("-----------onCameraChangeFinish------------");
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        LogUtils.i("-----------onTouch------------");
    }
}
