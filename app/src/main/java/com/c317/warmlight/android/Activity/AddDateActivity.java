package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.DateNews_detalis;
import com.c317.warmlight.android.bean.Result;
import com.c317.warmlight.android.bean.UserInfo;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CacheUtils;
import com.c317.warmlight.android.utils.CommonUtils;
import com.c317.warmlight.android.utils.UtilImags;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/15.
 */

public class AddDateActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tv_date_publicate)
    TextView tvDatePublicate;
    @Bind(R.id.et_date_inTitle)
    EditText etDateInTitle;//标题
    @Bind(R.id.et_date_inContent)
    EditText etDateInContent;//内容
    @Bind(R.id.iv_add_date)
    ImageView ivAddDate;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.iv_locate_date)
    ImageView ivLocateDate;
    @Bind(R.id.iv_menu)
    ImageView ivMenu;
    @Bind(R.id.iv_me_setting)
    ImageView ivMeSetting;
    @Bind(R.id.iv_read_collect)
    ImageView ivReadCollect;
    @Bind(R.id.iv_read_uncollect)
    ImageView ivReadUncollect;
    @Bind(R.id.tv_date_showtime)
    TextView tvDateShowtime;//开始时间
    @Bind(R.id.rl_date_starttime)
    RelativeLayout rlDateStarttime;
    @Bind(R.id.rl_date_inendtime)
    RelativeLayout rlDateInendtime;
    @Bind(R.id.rl_date_inplace)
    RelativeLayout rlDateInplace;
    @Bind(R.id.iv_date_instarttime)
    ImageView ivDateInstarttime;
    @Bind(R.id.rl_date_selplace)
    RelativeLayout rlDateSelplace;
    @Bind(R.id.et_date_inplace)
    EditText etDateInplace;//友约地点
    @Bind(R.id.rl_date_joinsetting)
    RelativeLayout rlDateJoinsetting;
    @Bind(R.id.rl_date_jointype)
    RelativeLayout rlDateJointype;
    @Bind(R.id.et_date_joinnum)
    EditText etDateJoinnum;//参加总人数
    @Bind(R.id.tv_date_showendtime)
    TextView tvDateShowendtime;//结束时间
    @Bind(R.id.tv_date_joinshowtype)
    TextView tvDateJoinshowtype;//类型设置
    @Bind(R.id.btn_date_release)
    Button btnDateRelease;
    @Bind(R.id.btn_date_uploadpho)
    Button btnDateUploadpho;
    final int themeHoloLight = AlertDialog.THEME_HOLO_LIGHT;
    StringBuilder selectTime = null;//用户设置时间设置
    int mDateType = 10;
    private LinearLayout ll_popup;
    private PopupWindow pop;
    protected static Uri tempUri;
    private Bitmap mBitmap;
    private ImageView mImage;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.adddate_date_aty);
        ButterKnife.bind(this);
        //初始化顶部
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("新建友约");
        ivBackMe.setOnClickListener(this);
        //开始时间设置监听
        rlDateStarttime.setOnClickListener(this);
        //结束时间设置
        rlDateInendtime.setOnClickListener(this);
        //友约地点地图选择
        rlDateSelplace.setOnClickListener(this);
        //报名设置
        rlDateJoinsetting.setOnClickListener(this);
        //活动类型设置
        rlDateJointype.setOnClickListener(this);
        //发布友约
        btnDateRelease.setOnClickListener(this);
        //上传图片
        btnDateUploadpho.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_me:
                finish();
                break;
            case R.id.btn_date_release:
                releaseDate();
                break;
            case R.id.rl_date_jointype:
                setDateType();
                break;
            case R.id.rl_date_joinsetting:
                enrollSetting();
                break;
            case R.id.rl_date_selplace:
                selectMap();
                break;
            case R.id.rl_date_inendtime:
                endtimeSetting(themeHoloLight);
                break;
            case R.id.rl_date_starttime:
                startTimeSetting();
                break;
            case R.id.btn_date_uploadpho:
                dateUpLoadPic();
//                showPopupWindow();
//                ll_popup.startAnimation(AnimationUtils.loadAnimation(
//                        AddDateActivity.this, R.anim.photo_translate_in));
//                pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
        }
    }


    /**
     * 上传图片
     *
     * @params
     * @author Du
     * @Date 2018/3/26 15:52
     **/
    private void dateUpLoadPic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddDateActivity.this);
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "temp_image.jpg"));
                        // 将拍照所得的相片保存到SD卡根目录
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.show();

    }


    /****
     * 头像提示框，下拉选择框
     */
    public void showPopupWindow() {
        pop = new PopupWindow(AddDateActivity.this);
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
            saveBitmapFile(UtilImags.compressScale(bmp), UtilImags.SHOWFILEURL(AddDateActivity.this) + "/stscname.jpg");
            staffFileupload(new File(UtilImags.SHOWFILEURL(AddDateActivity.this) + "/stscname.jpg"));
        } catch (Exception e) {
            CommonUtils.showToastShort(this, "上传失败");
        }
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
            filename = UtilImags.SHOWFILEURL(AddDateActivity.this) + "/" + name;
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
        staffFileupload(new File(filename));//上传图片至服务端
        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AddDateActivity.RESULT_OK && null != data) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri); // 对图片进行裁剪处理
//                    if (takePicture(data)) return;
                    break;
                case CHOOSE_PICTURE:
//                    selectMobPhotos(data);
                    cutImage(tempUri); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }


    public void staffFileupload(File file) {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.UPLOADICON;
        RequestParams params = new RequestParams(url);
        String account = UserManage.getInstance().getUserInfo(AddDateActivity.this).account;
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
                    CommonUtils.showToastShort(AddDateActivity.this, "上传成功");
                } else {
                    CommonUtils.showToastShort(AddDateActivity.this, "上传失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.showToastShort(AddDateActivity.this, "文件上传失败");
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
     * 裁剪图片方法实现
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }



    private void startTimeSetting() {
        selectTime = new StringBuilder();
        Calendar day = Calendar.getInstance();
        DatePickerDialog dayDialog = new DatePickerDialog(AddDateActivity.this, themeHoloLight,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectTime.append(year + "-" + (month + 1) + "-" + dayOfMonth + " ");
                        Calendar time = Calendar.getInstance();
                        Dialog timeDialog = new TimePickerDialog(AddDateActivity.this, themeHoloLight, new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                selectTime.append(hourOfDay + ":" + minute);
                                tvDateShowtime.setVisibility(View.VISIBLE);
                                tvDateShowtime.setText(selectTime);
                            }
                        }, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), true);
                        timeDialog.show();
                    }
                }, day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH));
        //当前年份-1 >= 年份 <= 当前年份+1
        dayDialog.getDatePicker().setMaxDate(((new Date().getTime() / 1000) + 60 * 60 * 24 * 365) * 1000);
        dayDialog.getDatePicker().setMinDate(((new Date().getTime() / 1000) - 60 * 60 * 24 * 365) * 1000);
        dayDialog.show();
    }


    private void endtimeSetting(final int themeHoloLight) {
        selectTime = new StringBuilder();
        Calendar day = Calendar.getInstance();
        DatePickerDialog dayDialog = new DatePickerDialog(AddDateActivity.this, themeHoloLight,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectTime.append(year + "-" + (month + 1) + "-" + dayOfMonth + " ");
                        Calendar time = Calendar.getInstance();
                        Dialog timeDialog = new TimePickerDialog(AddDateActivity.this, themeHoloLight, new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                selectTime.append(hourOfDay + ":" + minute);
                                tvDateShowendtime.setVisibility(View.VISIBLE);
                                tvDateShowendtime.setText(selectTime);
                            }
                        }, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), true);
                        timeDialog.show();
                    }
                }, day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH));
        //当前年份-1 >= 年份 <= 当前年份+1
        dayDialog.getDatePicker().setMaxDate(((new Date().getTime() / 1000) + 60 * 60 * 24 * 365) * 1000);
        dayDialog.getDatePicker().setMinDate(((new Date().getTime() / 1000) - 60 * 60 * 24 * 365) * 1000);
        dayDialog.show();
    }


    private void selectMap() {
        Intent intent = new Intent(AddDateActivity.this, DateSelplaceActivity.class);
        startActivity(intent);
    }

    private void enrollSetting() {
        Intent intent = new Intent(AddDateActivity.this, JoinSettingActivity.class);
        startActivity(intent);
    }


    public void releaseDate() {
        String enrollInfo = getEnrollInfo();
        CacheUtils.cleanCache(AddDateActivity.this, AppConstants.ENROLLSETTING);//清除页面缓存
        if (!TextUtils.isEmpty(getUserAccount())) {
            if (!TextUtils.isEmpty(enrollInfo)) {
                DateNews_detalis.DateNews_content newDateParams = getNewDateParams();
                newDateParams.proposer = getUserAccount();
                String[] enrollInfos = enrollInfo.split("\\|");
                if (enrollInfos.length == 2) {
                    newDateParams.beginTime = enrollInfos[0];
                    newDateParams.deadline = enrollInfos[1];
                    saveNewDateToServer(newDateParams);
                } else {
                    Toast.makeText(AddDateActivity.this, "报名设置数据不完整",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddDateActivity.this, "请设置报名设置模块",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AddDateActivity.this, "无法请求到用户",
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 存储新建友约的信息到服务端
     *
     * @params
     * @author Du
     * @Date 2018/3/15 22:18
     **/
    private void saveNewDateToServer(DateNews_detalis.DateNews_content news_content) {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.CREATEACTIVITY;
        RequestParams params = new RequestParams(url);
        params.addParameter("title", news_content.title);
        params.addParameter("content", news_content.content);
        params.addParameter("memberTotalNum", news_content.memberTotalNum);
        params.addParameter("startTime", news_content.startTime);
        params.addParameter("endTime", news_content.endTime);
        params.addParameter("beginTime", news_content.beginTime);
        params.addParameter("deadline", news_content.deadline);
        params.addParameter("place", news_content.place);
        params.addParameter("type", news_content.type);
        params.addParameter("proposer", news_content.proposer);
        if (!TextUtils.isEmpty(news_content.telephone)) {
            params.addParameter("telephone", news_content.telephone);
        }
        if (!TextUtils.isEmpty(news_content.picture)) {
            params.addParameter("picture", news_content.picture);
        }
        if (!TextUtils.isEmpty(news_content.coordinate)) {
            params.addParameter("coordinate", news_content.coordinate);
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Result resultInfo = gson.fromJson(result, Result.class);
                if (resultInfo.code == 201) {
                    Toast.makeText(AddDateActivity.this, "发布成功",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    CacheUtils.cleanCache(AddDateActivity.this, AppConstants.ENROLLSETTING);
                } else {
                    Toast.makeText(AddDateActivity.this, resultInfo.desc.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(AddDateActivity.this, "发布失败",
                        Toast.LENGTH_SHORT).show();
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
     * 获取报名设置信息
     *
     * @params
     * @author Du
     * @Date 2018/3/15 22:16
     **/
    private String getEnrollInfo() {
        String enrollSetting = CacheUtils.getCache(AppConstants.ENROLLSETTING, AddDateActivity.this);
        if (!TextUtils.isEmpty(enrollSetting)) {
            return enrollSetting;
        } else {
            return null;
        }
    }

    /**
     * 获取新建友约详细信息
     *
     * @params
     * @author Du
     * @Date 2018/3/15 21:10
     **/
    private DateNews_detalis.DateNews_content getNewDateParams() {
        if (TextUtils.isEmpty(etDateInTitle.getText())) {
            Toast.makeText(AddDateActivity.this, "友约标题不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else if (TextUtils.isEmpty(etDateInContent.getText())) {
            Toast.makeText(AddDateActivity.this, "友约主题描述不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else if (TextUtils.isEmpty(tvDateShowtime.getText())) {
            Toast.makeText(AddDateActivity.this, "友约开始时间不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else if (TextUtils.isEmpty(tvDateShowendtime.getText())) {
            Toast.makeText(AddDateActivity.this, "友约结束时间不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else if (TextUtils.isEmpty(etDateInplace.getText())) {
            Toast.makeText(AddDateActivity.this, "友约地点不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else if (mDateType == 10) {
            Toast.makeText(AddDateActivity.this, "友约类型不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else if (TextUtils.isEmpty(etDateJoinnum.getText())) {
            Toast.makeText(AddDateActivity.this, "友约活动总人数不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else {
            DateNews_detalis.DateNews_content dateNews_content = new DateNews_detalis.DateNews_content();
            dateNews_content.title = etDateInTitle.getText().toString();
            dateNews_content.content = etDateInContent.getText().toString();
            dateNews_content.startTime = tvDateShowtime.getText().toString();
            dateNews_content.endTime = tvDateShowendtime.getText().toString();
            dateNews_content.place = etDateInplace.getText().toString();
            dateNews_content.type = mDateType;
            dateNews_content.memberTotalNum = Integer.valueOf(etDateJoinnum.getText().toString());
            return dateNews_content;
        }
    }


    /**
     * 设置活动类型
     *
     * @params
     * @author Du
     * @Date 2018/3/15 17:19
     **/
    private void setDateType() {
        final String items[] = {"知识分享", "书籍交流", "学术论坛", "社团活动", "其他"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this, 4);
        // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                tvDateJoinshowtype.setVisibility(View.VISIBLE);
                tvDateJoinshowtype.setText(items[which]);
                mDateType = which;
            }
        });
        builder.create().show();
    }


    public String getUserAccount() {
        UserInfo.UserInfo_content userInfo_content = UserManage.getInstance().getUserInfo(this);
        return userInfo_content.getAccount();
    }


    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            //这里图片是方形的，可以用一个工具类处理成圆形（很多头像都是圆形，这种工具类网上很多不再详述）
            mImage.setImageBitmap(mBitmap);//显示图片
            //在这个地方可以写上上传该图片到服务器的代码，后期将单独写一篇这方面的博客，敬请期待...
        }
    }

}
