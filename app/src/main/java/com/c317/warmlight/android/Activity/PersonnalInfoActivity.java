package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Result;
import com.c317.warmlight.android.bean.UserInfo;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CommonUtils;
import com.c317.warmlight.android.utils.UtilImags;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/3/14.
 */

public class PersonnalInfoActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.civ_personnalinfo_circleImageView)
    CircleImageView civPersonnalinfoCircleImageView;
    @Bind(R.id.tv_personnalinfo_username)
    TextView tvPersonnalinfoUsername;
    @Bind(R.id.tv_personnalinfo_username1)
    TextView tvPersonnalinfoUsername1;
    @Bind(R.id.tv_personnalinfo_sex)
    TextView tvPersonnalinfoSex;
    @Bind(R.id.tv_personnalinfo_sex1)
    TextView tvPersonnalinfoSex1;
    @Bind(R.id.tv_personnalinfo_sig)
    TextView tvPersonnalinfoSig;
    @Bind(R.id.tv_personnalinfo_sig1)
    TextView tvPersonnalinfoSig1;
    @Bind(R.id.iv_add_date)
    ImageView ivAddDate;
    @Bind(R.id.ll_me_editname)
    RelativeLayout llMeEditname;
    @Bind(R.id.ll_me_editsex)
    RelativeLayout llMeEditsex;
    @Bind(R.id.ll_me_editsigure)
    RelativeLayout llMeEditsigure;
    @Bind(R.id.ll_me_editpassward)
    RelativeLayout llMeEditpassward;

    private String sex1;
    private String account;
    private PopupWindow pop;
    private LinearLayout ll_popup;
    protected static final int TAKE_PICTURE = 0;//拍照
    protected static final int CHOOSE_PICTURE = 1;//选择相片
    private static final int SHOW_PICTURE = 2;//显示图片
    private static final int RESULT_CODE = 3;//返回码

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_personnalinfo_aty);
        ButterKnife.bind(this);
        //顶部图标
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("个人信息");
        account = UserManage.getInstance().getUserInfo(PersonnalInfoActivity.this).account;
        initPhotoData();
        getDataFromServer();
        ivBackMe.setOnClickListener(this);
        civPersonnalinfoCircleImageView.setOnClickListener(this);
        llMeEditname.setOnClickListener(this);
        llMeEditsex.setOnClickListener(this);
        llMeEditsigure.setOnClickListener(this);
        llMeEditpassward.setOnClickListener(this);
    }

    /**
     * 初始化相片
     * myimg 用户拍照产生的图片
     * stscimage 相册选择产生的图片
     * 如果myimg为空,加载stscimage字段图片
     *
     * @params
     * @author Du
     * @Date 2018/3/16 19:49
     **/
    private void initPhotoData() {
        String picname = "icon/" + account + ".jpg";
        String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + picname;
        Picasso.with(PersonnalInfoActivity.this).load(imageUrl).memoryPolicy(MemoryPolicy.NO_CACHE).into(civPersonnalinfoCircleImageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_me:
                finish();
                break;
            //上传图片
            case R.id.civ_personnalinfo_circleImageView:
                showPopupWindow();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        PersonnalInfoActivity.this, R.anim.photo_translate_in));
                pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            //编辑姓名
            case R.id.ll_me_editname:
                break;
            //编辑性别
            case R.id.ll_me_editsex:
                break;
            //编辑个性签名
            case R.id.ll_me_editsigure:
                break;
            //修改密码
            case R.id.ll_me_editpassward:
                Intent intent = new Intent(PersonnalInfoActivity.this, PersonnalinfoChangepasswordAty.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PersonnalInfoActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    takePicture(data);
                    break;
                case CHOOSE_PICTURE:
                    selectMobPhotos(data);
                    break;
                case SHOW_PICTURE:

                    break;
            }
        }
    }



    /****
     * 头像提示框，下拉选择框
     */
    public void showPopupWindow() {
        pop = new PopupWindow(PersonnalInfoActivity.this);
        View view = getLayoutInflater().inflate(R.layout.photo_me_pops, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_photo_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.photo_parent);
        Button camera = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button Photo = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button cancel = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//拍照
                startActivityForResult(camera, TAKE_PICTURE);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        Photo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent picture = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//从相册选择
                startActivityForResult(picture, CHOOSE_PICTURE);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();//取消
                ll_popup.clearAnimation();
            }
        });
    }


    private void selectMobPhotos(Intent data) {
        try {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = this.getContentResolver().query(selectedImage,
                    filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);//获取图片路径
            String picturePath = c.getString(columnIndex);
            c.close();
            //转为Bitmap
            Bitmap bmp = BitmapFactory.decodeFile(picturePath);
            // 获取图片并显示
            civPersonnalinfoCircleImageView.setImageBitmap(bmp);
            saveBitmapFile(UtilImags.compressScale(bmp), UtilImags.SHOWFILEURL(PersonnalInfoActivity.this) + "/stscname.jpg");
            staffFileupload(new File(UtilImags.SHOWFILEURL(PersonnalInfoActivity.this) + "/stscname.jpg"));
            setResult(RESULT_CODE,data);
        } catch (Exception e) {
            CommonUtils.showToastShort(this, "上传失败");
        }
    }


    private void takePicture(Intent data) {
        String sdState = Environment.getExternalStorageState();
        if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        new DateFormat();
        String name = DateFormat.format("yyyyMMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA)) + ".jpg";
        Bundle bundle = data.getExtras();
        // 获取相机返回的数据，并转换为图片格式
        Bitmap bmp = (Bitmap) bundle.get("data");
        FileOutputStream fout = null;
        String filename = null;
        try {
            filename = UtilImags.SHOWFILEURL(PersonnalInfoActivity.this) + "/" + name;
        } catch (IOException e) {
        }
        try {
            fout = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fout);
        } catch (FileNotFoundException e) {
            CommonUtils.showToastShort(this, "上传失败");
        } finally {
            try {
                fout.flush();
                fout.close();
            } catch (IOException e) {
                CommonUtils.showToastShort(this, "上传失败");
            }
        }
        civPersonnalinfoCircleImageView.setImageBitmap(bmp);//显示拍照图片
        staffFileupload(new File(filename));//上传图片至服务端
        setResult(RESULT_CODE,data);
    }


    public void saveBitmapFile(Bitmap bitmap, String path) {
        File file = new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void staffFileupload(File file) {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.UPLOADICON;
        RequestParams params = new RequestParams(url);
        String account = UserManage.getInstance().getUserInfo(PersonnalInfoActivity.this).account;
        params.addBodyParameter(AppConstants.ACCOUNT, account);
        if (file != null) {
            params.addBodyParameter("picture", file);
        }
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Result resultInfo = gson.fromJson(result, Result.class);
                if (resultInfo.code == 200) {
                    CommonUtils.showToastShort(PersonnalInfoActivity.this, "上传成功");
                } else {
                    CommonUtils.showToastShort(PersonnalInfoActivity.this, "上传失败");
                }
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


    private void getDataFromServer() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + "getUserInfo";
        RequestParams params = new RequestParams(url);
        params.addParameter("account", account);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserInfo userinfo = gson.fromJson(result, UserInfo.class);
                UserInfo.UserInfo_content userInfo_content = userinfo.data;
                setUserInfoView(userInfo_content);
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

    private void setUserInfoView(UserInfo.UserInfo_content userInfo_content) {
        if (userInfo_content.sex == 0) {
            sex1 = "男";
        } else {
            sex1 = "女";
        }
        tvPersonnalinfoUsername1.setText(userInfo_content.username);
        tvPersonnalinfoSig1.setText(userInfo_content.signature);
        tvPersonnalinfoSex1.setText(sex1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
