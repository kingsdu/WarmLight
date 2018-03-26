package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.UserManager;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Result;
import com.c317.warmlight.android.bean.UserInfo;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.ACache;
import com.c317.warmlight.android.utils.CacheUtils;
import com.c317.warmlight.android.utils.CommonUtils;
import com.c317.warmlight.android.utils.UtilFileDB;
import com.c317.warmlight.android.utils.UtilImags;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/3/14.
 */

public class EditPersonnalinfoActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.civ_editpersonnalinfo_circleImageView)
    CircleImageView civEditpersonnalinfoCircleImageView;
    @Bind(R.id.btn_editpersonnalinfo_save)
    Button btnEditpersonnalinfoSave;
    @Bind(R.id.et_editpersonnalinfo_username)
    EditText etEditpersonnalinfoUsername;
    @Bind(R.id.et_editpersonnalinfo_sex)
    EditText etEditpersonnalinfoSex;
    @Bind(R.id.et_editpersonnalinfo_birthday)
    EditText etEditpersonnalinfoBirthday;
    @Bind(R.id.et_editpersonnalinfo_sig)
    EditText etEditpersonnalinfoSig;
    @Bind(R.id.et_editpersonnalinfo_email)
    EditText etEditpersonnalinfoEmail;

    private String[] sexArry = new String[]{"男", "女"};// 性别选择
    private int sexnum;
    private Boolean infoCheck = true;
    private PopupWindow pop;
    private LinearLayout ll_popup;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.my_editpersonnalinfo_aty);
        ButterKnife.bind(this);
        //顶部图标
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("编辑个人信息");
        String account = UserManage.getInstance().getUserInfo(EditPersonnalinfoActivity.this).account;
        //获取用户头像
        initPhotoData(account);
        //获取用户信息
        getUserDataFromServer(account);
        //监听
        etEditpersonnalinfoSex.setOnClickListener(this);
        btnEditpersonnalinfoSave.setOnClickListener(this);
        ivBackMe.setOnClickListener(this);
        civEditpersonnalinfoCircleImageView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ_editpersonnalinfo_circleImageView:
                showPopupWindow();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        EditPersonnalinfoActivity.this, R.anim.photo_translate_in));
                pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.iv_back_me:
                finish();
                break;
            case R.id.et_editpersonnalinfo_sex:
                showSexChooseDialog();
                break;
            case R.id.btn_editpersonnalinfo_save:
                savePersonInfo();//存储用户信息
                break;

        }
    }


    /****
     * 头像提示框，下拉选择框
     */
    public void showPopupWindow() {
        pop = new PopupWindow(EditPersonnalinfoActivity.this);
        View view = getLayoutInflater().inflate(R.layout.photo_me_pops, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_photo_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.photo_parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//拍照
                startActivityForResult(camera, 1);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent picture = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//从相册选择
                startActivityForResult(picture, 2);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();//取消
                ll_popup.clearAnimation();
            }
        });
    }


    /**
     * 初始化相片
     * photoImage 用户拍照产生的图片
     * mobileImage 相册选择产生的图片
     * 如果myimg为空,加载stscimage字段图片
     *
     * @params
     * @author Du
     * @Date 2018/3/16 19:49
     **/
    private void initPhotoData(String account) {
        String picname = "icon/" + account + ".jpg";
        String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + picname;
        Picasso.with(EditPersonnalinfoActivity.this).load(imageUrl).into(civEditpersonnalinfoCircleImageView);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照的返回码a
        if (requestCode == 1 && resultCode == Activity.RESULT_OK
                && null != data) {
            if (takePicture(data)) return;
        }
        //从相册选择的返回码
        if (requestCode == 2 && resultCode == Activity.RESULT_OK
                && null != data) {
            selectMobPhotos(data);
        }
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
            civEditpersonnalinfoCircleImageView.setImageBitmap(bmp);
            saveBitmapFile(UtilImags.compressScale(bmp), UtilImags.SHOWFILEURL(EditPersonnalinfoActivity.this) + "/stscname.jpg");
            staffFileupload(new File(UtilImags.SHOWFILEURL(EditPersonnalinfoActivity.this) + "/stscname.jpg"));
        } catch (Exception e) {
            CommonUtils.showToastShort(this, "上传失败");
        }
    }


    private boolean takePicture(Intent data) {
        String sdState = Environment.getExternalStorageState();
        if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
            return true;
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
            filename = UtilImags.SHOWFILEURL(EditPersonnalinfoActivity.this) + "/" + name;
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
        civEditpersonnalinfoCircleImageView.setImageBitmap(bmp);//显示拍照图片
        staffFileupload(new File(filename));//上传图片至服务端
        return false;
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
        String account = UserManage.getInstance().getUserInfo(EditPersonnalinfoActivity.this).account;
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
                    CommonUtils.showToastShort(EditPersonnalinfoActivity.this, "上传成功");
                } else {
                    CommonUtils.showToastShort(EditPersonnalinfoActivity.this, "上传失败");
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


    /* 性别选择框 */
    private void showSexChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);// 自定义对话框
        builder.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                etEditpersonnalinfoSex.setText(sexArry[which]);
                if (which == 0) {//男
                    sexnum = 0;
                    etEditpersonnalinfoSex.setText(sexArry[sexnum]);
                } else {//女
                    sexnum = 1;
                }

                dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
            }


        });
        builder.show();// 让弹出框显示
    }


    private void getUserDataFromServer(final String account) {
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
        etEditpersonnalinfoUsername.setText(userInfo_content.username);
        etEditpersonnalinfoSig.setText(userInfo_content.signature);
        etEditpersonnalinfoEmail.setText(userInfo_content.email);
        etEditpersonnalinfoBirthday.setText(userInfo_content.birthday);
        etEditpersonnalinfoSex.setText(sexArry[sexnum]);
    }

    private void savePersonInfo() {
        if (etEditpersonnalinfoEmail.getText().toString() != null) {
            Pattern pt = Pattern
                    .compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
            Matcher m = pt.matcher(etEditpersonnalinfoEmail.getText().toString());
            if (!m.matches()) {
                CommonUtils.showToastShort(EditPersonnalinfoActivity.this, "请输入正确的邮箱");
                infoCheck = false;
            } else {
                infoCheck = true;
            }
        }
        if (infoCheck.booleanValue() == true) {
            EditPersonnalInfo(UserManage.getInstance().getUserInfo(EditPersonnalinfoActivity.this).account,
                    etEditpersonnalinfoUsername.getText().toString(),
                    sexnum,
                    etEditpersonnalinfoBirthday.getText().toString(),
                    etEditpersonnalinfoSig.getText().toString(),
                    etEditpersonnalinfoEmail.getText().toString());
        }
    }

    public void EditPersonnalInfo(final String account, final String username, final int sex, final String birthday, final String signature, final String email) {
        RequestParams params = new RequestParams("http://14g97976j3.51mypc.cn:10759/my/modifyUser");
        params.addParameter("account", account);
        params.addParameter("username", username);
        params.addParameter("sex", sex);
        params.addParameter("birthday", birthday);
        params.addParameter("signature", signature);
        params.addParameter("email", email);
        x.http().request(HttpMethod.PUT, params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Result resultInfo = gson.fromJson(result, Result.class);
                if (resultInfo.code == 200) {
                    CommonUtils.showToastShort(EditPersonnalinfoActivity.this, "保存成功");
                } else {
                    CommonUtils.showToastShort(EditPersonnalinfoActivity.this, resultInfo.desc.toString());
                }
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //失败
                Toast.makeText(
                        EditPersonnalinfoActivity.this,
                        "保存失败",
                        Toast.LENGTH_LONG)
                        .show();
                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
