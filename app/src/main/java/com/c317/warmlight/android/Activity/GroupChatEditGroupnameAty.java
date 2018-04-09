package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.common.Application_my;

/**
 * Created by Administrator on 2018/4/9.
 */

public class GroupChatEditGroupnameAty extends Activity implements View.OnClickListener {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.groupchat_editgroupname_aty);
    }
    @Override
    public void onClick(View v) {

    }
}
