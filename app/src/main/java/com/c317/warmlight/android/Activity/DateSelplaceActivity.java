package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CommonUtils;
import com.c317.warmlight.android.utils.SharedPrefUtility;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/25.
 */

public class DateSelplaceActivity extends Activity implements OnGetGeoCoderResultListener {
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tv_topbar_right)
    TextView tvTopbarRight;
    @Bind(R.id.tv_addrName)
    TextView tvAddrName;
    @Bind(R.id.tv_coordinate)
    TextView tvCoordinate;

    /**
     * UI相关
     */
    private RelativeLayout popuInfoView = null;                 //点击marker后弹出的窗口
    private TextView locationText = null;                       //显示的当前地址的view

    /**
     * 百度地图相关
     */
    private LocationClient locationClient;                      //定位SDK核心类
    private MapView mapView;
    private BaiduMap baiduMap;                                  //百度地图对象
    boolean isFirstLoc = true;                                  //是否首次定位
    private LatLng myLocation;                                  //当前定位信息
    private LatLng clickLocation;                               //长按地址信息
    private BDLocation currentLocation;                         //当前定位信息[最好使用这个]

    GeoCoder mSearch = null;                                    //地理编码模块

    public LocationListenner myListener = new LocationListenner();

    private float lastX = 0.0f;                                 //传感器返回的方向

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.dateselplace_aty);
        ButterKnife.bind(this);
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("友约地点");
        tvTopbarRight.setText("完成");
        //界面初始化：控件初始化
        initView();
        //初始化百度地图相关
        initBaiduMap();
        ivBackMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTopbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savecoordinate();
            }
        });
    }

    /**
     * 界面初始化
     */
    private void initView() {
        //百度地图view
        mapView = (MapView) findViewById(R.id.bmapView);
    }

    /**
     * 初始化百度地图相关模块
     */
    private void initBaiduMap() {
        /*****************************************************
         * 地图模块
         *****************************************************/
        //百度地图map
        baiduMap = mapView.getMap();

        /*****************************************************
         * 定位模块
         *****************************************************/
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        //定位服务客户端
        locationClient = new LocationClient(this);
        //注册监听
        //注册监听
        locationClient.registerLocationListener(myListener);
        //定位配置信息
        LocationClientOption option = new LocationClientOption();
        // 打开gps
        option.setOpenGps(true);
        // 设置坐标类型,国测局经纬度坐标系:gcj02;  百度墨卡托坐标系:bd09;  百度经纬度坐标系:bd09ll
        option.setCoorType("bd09ll");
        //定位请求时间间隔 1秒
        option.setScanSpan(1000);

        locationClient.setLocOption(option);
        //开启定位
        locationClient.start();
        //增加监听：长按地图
        baiduMap.setOnMapLongClickListener(new OnMapLongClickListener());

        /******************************************************
         * 地理编码模块
         ******************************************************/
        //地理编码模块
        mSearch = GeoCoder.newInstance();
        //增加监听：地理编码查询结果
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(DateSelplaceActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        baiduMap.clear();
        baiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_gcoding)));
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
        Toast.makeText(DateSelplaceActivity.this, strInfo, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(DateSelplaceActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        baiduMap.clear();
        baiduMap.addOverlay(
                new MarkerOptions()
                        .position(result.getLocation())                                     //坐标位置
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding))  //图标
                        .title(result.getAddress())                                         //标题

        );
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        /**
         * 弹出InfoWindow，显示信息
         */
        BDLocation bdLocation = new BDLocation();
        bdLocation.setLatitude(result.getLocation().latitude);
        bdLocation.setLongitude(result.getLocation().longitude);
        bdLocation.setAddrStr(result.getAddress());
        String add = bdLocation.getAddrStr();
        poputInfo(bdLocation, result.getAddress());
    }

    /**
     * 弹出InfoWindow，显示信息
     */
    public void poputInfo(final BDLocation bdLocation, final String address) {
        /**
         * 获取弹窗控件
         */
        popuInfoView = (RelativeLayout) findViewById(R.id.id_marker_info);

        double la = bdLocation.getLatitude();
        la=  (double) Math.round(la * 1000000000) / 1000000000;
        double lu = bdLocation.getLongitude();
        lu=  (double) Math.round(lu * 1000000000) / 1000000000;
        String str = bdLocation.getAddrStr();
        if (tvAddrName != null)
            tvAddrName.setText(address);
        tvCoordinate.setText(la + "," + lu);

        popuInfoView.setVisibility(View.VISIBLE);
    }

    private class LocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // map view 销毁后不在处理新接收的位置
            if (bdLocation == null || mapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(bdLocation.getLatitude(),
                        bdLocation.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }

    /**
     * 重写map long click:长按地图选点,进行反地理编码,查询该点信息
     */
    private class OnMapLongClickListener implements BaiduMap.OnMapLongClickListener {
        @Override
        public void onMapLongClick(LatLng latLng) {
            clickLocation = latLng;
            reverseSearch(latLng);
        }
    }

    /**
     * 反向搜索
     *
     * @param latLng
     */
    public void reverseSearch(LatLng latLng) {
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLng));
    }


    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        baiduMap.clear();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        locationClient.stop();
        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    /**
     * 存储友约地点经纬度
     */
    private void savecoordinate() {
        String coordinate =tvCoordinate.getText().toString();
        if (!TextUtils.isEmpty(coordinate)) {
//            Editname(UserManage.getInstance().getUserInfo(DateSelplaceActivity.this).account, coordinate);
            SharedPrefUtility.setParam(this, AppConstants.COORDINATE, coordinate);
        } else {
            CommonUtils.showToastShort(this, "选择友约地点为空");
        }
        finish();
    }
}
