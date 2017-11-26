//package com.c317.warmlight.android.holder;
//
//import android.app.Activity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.c317.warmlight.android.R;
//import com.c317.warmlight.android.bean.Image;
//import com.c317.warmlight.android.bean.Index;
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
///**
// * Created by Administrator on 2017/11/25.
// */
//
//public class DetailAppPicsHolder extends BaseHolder<Index> {
//
//    private Activity picActivity;
//    private ImageView activity_pic;
//    private LinearLayout ll_activity;
//    private TextView tv_actTitle;
//
//    public DetailAppPicsHolder(Activity activity) {
//        picActivity = activity;
//    }
//
//
//    @Override
//    public View initView(Index number) {
//        ll_activity = (LinearLayout) picActivity.findViewById(R.id.ll_activity);
//        int size = number.imageList.size();
//        for (int i = 0; i < size; i++) {
//            View fragment_activity = LayoutInflater.from(picActivity).inflate(
//                    R.layout.card_switch, null);
//            activity_pic = (ImageView) fragment_activity.findViewById(R.id.iv_pic1);
//            tv_actTitle = (TextView)fragment_activity.findViewById(R.id.tv_actTitle);
//            ll_activity.addView(fragment_activity);
//        }
//        return ll_activity;
//    }
//
//    @Override
//    public void refreshView(Index data) {
//        if (data != null) {
//            List<Image> list = data.imageList;
//            for (int i = 0; i < list.size(); i++) {
//                if (i < list.size()) {
//                    String imageUrl = list.get(i).IMAURL;
//                    ll_activity.setVisibility(View.VISIBLE);
//                    activity_pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    Picasso.with(picActivity).load(imageUrl).into(activity_pic);
//                    tv_actTitle.setText("活动的标题" + i);
//                } else {
//                    ll_activity.setVisibility(View.GONE);
//                }
//            }
//        }
//    }
//}
