package com.c317.warmlight.android.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.c317.warmlight.android.Activity.PersonnalInfoActivity;
import com.c317.warmlight.android.Activity.SettingMeActivity;
import com.c317.warmlight.android.Activity.SettingMyDateActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseFragment;
import com.c317.warmlight.android.bean.DateNews;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.tabpager.MyDataTabPager;
import com.c317.warmlight.android.utils.CacheUtils;
import com.c317.warmlight.android.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/11/16.
 */

public class Me_Fragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.iv_me_setting)
    ImageView ivMeSetting;
    @Bind(R.id.circleImageView)
    CircleImageView circleImageView;
    @Bind(R.id.tv_nickname_me)
    TextView tvNicknameMe;
    @Bind(R.id.tv_renown_me)
    TextView tvRenownMe;
    @Bind(R.id.btn_signIn)
    Button btnSignIn;
    @Bind(R.id.rl_myread)
    RelativeLayout rlMyread;
    @Bind(R.id.rl_mydate)
    RelativeLayout rlMydate;
    @Bind(R.id.rl_my_answer)
    RelativeLayout rlMyAnswer;
    @Bind(R.id.rl_mymessage)
    RelativeLayout rlMymessage;

    private static final int SHOW_PICTURE = 2;//显示图片
    private static final int RESULT_CODE = 3;//返回码
    private Bitmap bmp;

    @Override
    public View initView() {
        View view = UIUtils.getXmlView(R.layout.fragment_me);
        ButterKnife.bind(this, view);
        //顶部图标
        ivBackMe.setVisibility(View.VISIBLE);
        ivMeSetting.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("我的");
        //个人资料初始化
        tvNicknameMe.setText("LBJ");
        tvRenownMe.setText("总得分：40000");
        btnSignIn.setText("签到");
        //监听事件初始化
        ivMeSetting.setOnClickListener(this);
        //圆形头像个人资料监听
        circleImageView.setOnClickListener(this);
        //我的友约监听
        rlMydate.setOnClickListener(this);
        return view;
    }


    @Override
    public void initData() {
        String account = UserManage.getInstance().getUserInfo(mActivity).account;
        initPhotoData(account);
    }


    public void initPhotoData(String account) {
        String picname = "icon/" + account + ".jpg";
        String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + picname;
        Picasso.with(mActivity).load(imageUrl).into(circleImageView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE) {
            switch (requestCode) {
                case SHOW_PICTURE:
                    setImageToView(data);
                    break;
            }
        }
    }

    protected void setImageToView(Intent data) {
        if(data!=null){
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = mActivity.getContentResolver().query(selectedImage,
                    filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);//获取图片路径
            String picturePath = c.getString(columnIndex);
            c.close();
            //转为Bitmap
            bmp = BitmapFactory.decodeFile(picturePath);
            // 获取图片并显示
            circleImageView.setImageBitmap(bmp);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_me_setting:
                Intent intent = new Intent(mActivity, SettingMeActivity.class);
                startActivity(intent);
                break;
            case R.id.circleImageView:
                Intent PersonnalIntent = new Intent(mActivity, PersonnalInfoActivity.class);
//                PersonnalIntent.putExtra("Bitmap",bmp);
                startActivityForResult(PersonnalIntent, SHOW_PICTURE);

                break;
            case R.id.rl_mydate:
                intent = new Intent(mActivity, SettingMyDateActivity.class);
                startActivity(intent);
                break;
        }
    }
}
