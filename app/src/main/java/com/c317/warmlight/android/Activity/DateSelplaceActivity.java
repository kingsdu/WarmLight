package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.c317.warmlight.android.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/18.
 */

public class DateSelplaceActivity extends Activity {
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tv_baidupos_position)
    TextView tvBaiduposPosition;
    @Bind(R.id.mv_baidumap_selplacemapview)
    MapView mvBaidumapMapview;
    @Bind(R.id.tv_baidumapstate_selplace)
    TextView tvBaidumapstateSelplace;


    /**
     * 定位SDK核心类
     */
    private LocationClient locationClient;
    /**
     * 定位监听
     */
    public LocationListenner myListener = new LocationListenner();
    /**
     * 百度地图控件
     */
    private MapView mapView;
    /**
     * 百度地图对象
     */
    private BaiduMap baiduMap;
    /**
     * 当前地点击点
     */
    private LatLng currentPt;
    /**
     * 控制按钮
     */
    private String touchType;

//    BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_marka);

//    BitmapDescriptor bdA = BitmapDescriptorFactory
//            .fromBitmap(bd.getBitmap());


    boolean isFirstLoc = true; // 是否首次定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.dateselplace_aty);
        ButterKnife.bind(this);
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("友约地图");
        /**
         * 地图初始化
         */
        //获取百度地图控件
        mapView = (MapView) findViewById(R.id.mv_baidumap_selplacemapview);
        //获取百度地图对象
        baiduMap = mapView.getMap();
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        /**
         * 定位初始化
         */
        //声明定位SDK核心类
        locationClient = new LocationClient(this);
        //注册监听
        locationClient.registerLocationListener(myListener);
        //定位配置信息
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//定位请求时间间隔
        locationClient.setLocOption(option);
        //开启定位
        locationClient.start();

        initListener();



        ivBackMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
     * 对地图事件的消息响应
     */
    private void initListener() {
        baiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {

            @Override
            public void onTouch(MotionEvent event) {

            }
        });


        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            /**
             * 单击地图
             */
            public void onMapClick(LatLng point) {
                touchType = "单击地图";
                currentPt = point;
                updateMapState();
            }

            /**
             * 单击地图中的POI点
             */
            public boolean onMapPoiClick(MapPoi poi) {
                touchType = "单击POI点";
                currentPt = poi.getPosition();
                updateMapState();
                return false;
            }
        });
        baiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            /**
             * 长按地图
             */
            public void onMapLongClick(LatLng point) {
                touchType = "长按";
                currentPt = point;
                updateMapState();
            }
        });
        baiduMap.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
            /**
             * 双击地图
             */
            public void onMapDoubleClick(LatLng point) {
                touchType = "双击";
                currentPt = point;
                updateMapState();
            }
        });

        /**
         * 地图状态发生变化
         */
        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            public void onMapStatusChangeStart(MapStatus status) {
                updateMapState();
            }

            @Override
            public void onMapStatusChangeStart(MapStatus status, int reason) {

            }

            public void onMapStatusChangeFinish(MapStatus status) {
                updateMapState();
            }

            public void onMapStatusChange(MapStatus status) {
                updateMapState();
            }
        });
//        zoomButton = (Button) findViewById(R.id.zoombutton);
//        rotateButton = (Button) findViewById(R.id.rotatebutton);
//        overlookButton = (Button) findViewById(R.id.overlookbutton);
//        animateStatus = (Button) findViewById(R.id.updatestatus);
//        saveScreenButton = (Button) findViewById(R.id.savescreen);
//        View.OnClickListener onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (view.equals(zoomButton)) {
//                    perfomZoom();
//                } else if (view.equals(rotateButton)) {
//                    perfomRotate();
//                } else if (view.equals(overlookButton)) {
//                    perfomOverlook();
//                } else if (view.equals(animateStatus)) {
//                    perfomAll();
//                } else if (view.equals(saveScreenButton)) {
//                    // 截图，在SnapshotReadyCallback中保存图片到 sd 卡
//                    baiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
//                        public void onSnapshotReady(Bitmap snapshot) {
//                            File file = new File("/mnt/sdcard/test.png");
//                            FileOutputStream out;
//                            try {
//                                out = new FileOutputStream(file);
//                                if (snapshot.compress(
//                                        Bitmap.CompressFormat.PNG, 100, out)) {
//                                    out.flush();
//                                    out.close();
//                                }
//                                Toast.makeText(MapControlDemo.this,
//                                        "屏幕截图成功，图片存在: " + file.toString(),
//                                        Toast.LENGTH_SHORT).show();
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    Toast.makeText(MapControlDemo.this, "正在截取屏幕图片...",
//                            Toast.LENGTH_SHORT).show();
//
//                }
//                updateMapState();
//            }
//
//        };
//        zoomButton.setOnClickListener(onClickListener);
//        rotateButton.setOnClickListener(onClickListener);
//        overlookButton.setOnClickListener(onClickListener);
//        saveScreenButton.setOnClickListener(onClickListener);
//        animateStatus.setOnClickListener(onClickListener);
    }

    /**
     * 更新地图状态显示面板
     */
    private void updateMapState() {
        if (tvBaidumapstateSelplace == null) {
            return;
        }
        String state = "";
        if (currentPt == null) {
            state = "点击、长按、双击地图以获取经纬度和地图状态";
        } else {
            state = String.format(touchType + ",当前经度： %f 当前纬度：%f",
                    currentPt.longitude, currentPt.latitude);
//            MarkerOptions ooA = new MarkerOptions().position(currentPt).icon(bdA);
            baiduMap.clear();
//            baiduMap.addOverlay(ooA);
        }
        state += "\n";
        MapStatus ms = baiduMap.getMapStatus();
        state += String.format(
                "zoom=%.1f rotate=%d overlook=%d",
                ms.zoom, (int) ms.rotate, (int) ms.overlook);
        tvBaidumapstateSelplace.setText(state);

    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
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
}
