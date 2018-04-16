package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.DateNews;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/14.
 * <p>
 * 友约地图
 */

public class MapActivity extends Activity {

    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tv_baidupos_position)
    TextView tvBaiduposPosition;
    @Bind(R.id.mv_baidumap_mapview)
    MapView mvBaidumapMapview;
    @Bind(R.id.pull_coordinatedate_refresh)
    PullToRefreshListView pullCoordinatedateRefresh;

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

    boolean isFirstLoc = true; // 是否首次定位

    public int PAGE = 1;
    public int pageSize = 5;
    public int UPPAGESIZE = 0;
    private boolean isHaveNextPage = true;//是否还有下一页
    boolean isFirst = true;//listview刷新用
    private int startPage = 1;//初始页
    private ArrayList<DateNews.DateNews_Detail> coordinatedateNews_details = new ArrayList();
    private DateNews dateNews_info;//服务端解析数据
    private CoordinateDateAdapter coordinateDateAdapter;
    List<LatLng> latLngs = new ArrayList<>();
    public String[] coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.baidumap_aty);
        ButterKnife.bind(this);
        pullCoordinatedateRefresh.setMode(PullToRefreshBase.Mode.BOTH);//上拉下拉都支持
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("友约地图");
        /**
         * 地图初始化
         */
        //获取百度地图控件
        mapView = (MapView) findViewById(R.id.mv_baidumap_mapview);
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
        pullCoordinatedateRefresh.setMode(PullToRefreshBase.Mode.BOTH);//上拉下拉都支持
        initData();

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

    public void initData() {
        getDataFromServer();

        pullCoordinatedateRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
//                if(PAGE>=1){
//                    PAGE--;//数据页数增加
//                    UPPAGESIZE--;
//                }
//                getDataFromServer();
                getDataFromServerPullDown();
                GetCoordinatePoint();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if (isHaveNextPage) {
                    PAGE++;//数据页数增加
                    UPPAGESIZE++;//总页数
                }
                getDataFromServer();
            }
        });
        pullCoordinatedateRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MapActivity.this, DateDetailActivity.class);
                DateNews.DateNews_Detail dateNews_detail = coordinatedateNews_details.get(position-1);
                intent.putExtra("activity_id", dateNews_detail.activity_id);
                intent.putExtra("picUrl", dateNews_detail.picture);
                intent.putExtra("title", dateNews_detail.title);
                intent.putExtra("content", dateNews_detail.content);
                intent.putExtra("readNum", dateNews_detail.readNum + "");
                intent.putExtra("agreeNum", dateNews_detail.agreeNum + "");
                intent.putExtra("commentNum", dateNews_detail.commentNum + "");
                intent.putExtra("endTime", dateNews_detail.endTime);
                intent.putExtra("startTime", dateNews_detail.startTime);
                intent.putExtra("memberNum", dateNews_detail.memberNum + "");
                intent.putExtra("type", dateNews_detail.type + "");
                intent.putExtra("place", dateNews_detail.place);
                MapActivity.this.startActivity(intent);
            }
        });
    }

    /**
     * 获取友约活动列表
     */
    private void getDataFromServer() {
        String url = "http://14g97976j3.51mypc.cn:10759/youyue/getActivityList";
        RequestParams params = new RequestParams(url);
        params.addParameter("isCoor", 1);
        params.addParameter("page", PAGE);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                DateNews dateNews = gson.fromJson(result, DateNews.class);
                if (UPPAGESIZE < dateNews.data.total) {
                    processData(result, true);
                    isHaveNextPage = true;
                } else {
                    //无新数据
                    isHaveNextPage = false;
                }
                GetCoordinatePoint();
                pullCoordinatedateRefresh.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


    private void processData(String cache, boolean isMore) {
        if (isFirst) {
            if (isMore) {
                Gson gson = new Gson();
                dateNews_info = gson.fromJson(cache, DateNews.class);
                coordinatedateNews_details.clear();
                coordinatedateNews_details.addAll(dateNews_info.data.detail);
                coordinateDateAdapter = new CoordinateDateAdapter();
                pullCoordinatedateRefresh.setAdapter(coordinateDateAdapter);
            } else {
                //第一次，无数据
            }
            isFirst = false;
        } else {
            if (isMore) {
                Gson gson = new Gson();
                dateNews_info = gson.fromJson(cache, DateNews.class);
                if (!haveRepeat(dateNews_info)) {
                    coordinatedateNews_details.clear();
                    coordinatedateNews_details.addAll(dateNews_info.data.detail);
                    coordinateDateAdapter.notifyDataSetChanged();
                }
            } else {
                //非第一次，无数据
            }
        }
        pullCoordinatedateRefresh.onRefreshComplete();
    }

    /**
     * 是否有重复项
     *
     * @params true有
     * @author Du
     * @Date 2018/3/27 14:21
     **/
    private boolean haveRepeat(DateNews dateNews_info) {
        ArrayList<DateNews.DateNews_Detail> detail = dateNews_info.data.detail;
        for (int i = 0; i < detail.size(); i++) {
            if (coordinatedateNews_details.get(i).activity_id.equals(detail.get(i).activity_id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 得到点标记
     */
    public final List<OverlayOptions> GetCoordinatePoint() {

        baiduMap.clear();
        //创建OverlayOptions的集合
        List<OverlayOptions> markerList = new ArrayList<OverlayOptions>();
        List<LatLng> latLngs = new ArrayList<>();
        int markerSize = 0;
        for (int i = 0; i < coordinatedateNews_details.size(); i++) {
            String[] coordinates = coordinatedateNews_details.get(i).getCoordinate().split(",");
            LatLng point = new LatLng(Double.valueOf(coordinates[0]), Double.valueOf(coordinates[1]));

            latLngs.add(point);
            Log.e("bbb", String.valueOf(point));
        }

        for (int i = 0; i < latLngs.size() && markerSize < 5; i++) {
//            if (coordinatedateNews_details.get(i).coordinate == null) {
//                continue;
//            }
            String[] coordinates = coordinatedateNews_details.get(i).getCoordinate().split(",");
            LatLng point = new LatLng(Double.valueOf(coordinates[0]), Double.valueOf(coordinates[1]));
            markerSize++;
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            markerList.add(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromAssetWithDpi("Icon_mark"
                            + markerSize + ".png")).extraInfo(bundle)
                    .position(point));

        }
        baiduMap.addOverlays(markerList);
        return markerList;

    }


    private void getDataFromServerPullDown() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.ACTIVITYLIST;
        RequestParams params = new RequestParams(url);
        params.addParameter("isCoor", 1);
        params.addParameter("page", startPage);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                DateNews dateNews = gson.fromJson(result, DateNews.class);
                //判断下一页是否还有数据
                if (UPPAGESIZE < dateNews.data.total) {
                    processDataPullDown(result);
                } else {
                }

                pullCoordinatedateRefresh.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MapActivity.this, "onError", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 下拉刷新数据，更新数据
     *
     * @params
     * @author Du
     * @Date 2018/3/27 14:46
     **/
    private void processDataPullDown(String result) {
        //重新开始加载
        coordinatedateNews_details.clear();
        PAGE = 1;
        UPPAGESIZE = 0;
        //加载新数据
        Gson gson = new Gson();
        dateNews_info = gson.fromJson(result, DateNews.class);
        coordinatedateNews_details.addAll(dateNews_info.data.detail);
        coordinateDateAdapter = new CoordinateDateAdapter();
        pullCoordinatedateRefresh.setAdapter(coordinateDateAdapter);
    }

    private class CoordinateDateAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return coordinatedateNews_details.size();
        }

        @Override
        public Object getItem(int position) {
            return coordinatedateNews_details.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //重用ListView
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MapActivity.this, R.layout.list_item_coordinatedates, null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_coordate_itemPic);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_coordate_itemTitle);
                holder.tvStartTime = (TextView) convertView.findViewById(R.id.tv_coordate_itemStartTime);
                holder.tvPlace = (TextView) convertView.findViewById(R.id.tv_coordate_itemPlace);
                holder.tvMember = (TextView) convertView.findViewById(R.id.tv_coordate_itemMembernum);
                holder.ivTime = (ImageView) convertView.findViewById(R.id.iv_coordate_clock);
                holder.ivLocate = (ImageView) convertView.findViewById(R.id.iv_coordate_locate);
                holder.ivJoinPeople = (ImageView) convertView.findViewById(R.id.iv_coordate_joinpeople);
                holder.btnnavigation = (Button) convertView.findViewById(R.id.btn_coordate_itemnavigation);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final DateNews.DateNews_Detail dateNews_detail = coordinatedateNews_details.get(position);
            String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + dateNews_detail.picture;
            Picasso.with(MapActivity.this).load(imageUrl).into(holder.ivPic);
            holder.tvTitle.setText(dateNews_detail.title);
            holder.tvStartTime.setText(dateNews_detail.startTime);
            holder.tvPlace.setText(dateNews_detail.place);
            holder.tvMember.setText(String.valueOf(dateNews_detail.memberNum));
            holder.tvStartTime.setText(dateNews_detail.startTime);
            holder.ivTime.setImageResource(R.drawable.time);
            holder.ivLocate.setImageResource(R.drawable.locate);
            holder.ivJoinPeople.setImageResource(R.drawable.join_people);


            holder.btnnavigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invokingBD(dateNews_detail.coordinate);
                }
            });
            return convertView;
        }


        public void invokingBD(String coordinate) {
            Intent intent = null;
//            for(int i = 0; i < coordinatedateNews_details.size(); i++){
//                String[] coordinates = coordinatedateNews_details.get(i).getCoordinate().split(",");
//
//            }
            String[] coordinates = coordinate.split(",");

            try {
                intent = Intent.getIntent("intent://map/direction?" +
                        //"origin=latlng:"+"34.264642646862,108.95108518068&" +   //起点  此处不传值默认选择当前位置
                        "destination=latlng:" + coordinates[0] + "," + coordinates[1] + "|name:我的目的地" +        //终点
                        "&mode=driving&" +          //导航路线方式
                        "region=武汉" +           //
                        "&src=慧医#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                if (isInstallByread("com.baidu.BaiduMap")) {
                    startActivity(intent); //启动调用
                    Log.e("GasStation", "百度地图客户端已经安装");
                } else {
                    Toast.makeText(MapActivity.this, "没有安装百度地图客户端", Toast.LENGTH_SHORT).show();
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


        }

        /**
         * 判断是否安装目标应用
         *
         * @param packageName 目标应用安装后的包名
         * @return 是否已安装目标应用
         */
        private boolean isInstallByread(String packageName) {
            return new File("/data/data/" + packageName).exists();
        }


    }

    static class ViewHolder {
        public ImageView ivPic;
        public TextView tvTitle;
        public TextView tvStartTime;
        public TextView tvPlace;
        public TextView tvMember;
        public ImageView ivTime;
        public ImageView ivLocate;
        public ImageView ivJoinPeople;
        public Button btnnavigation;
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
