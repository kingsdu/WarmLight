package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.common.Application_my;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/31.
 */

public class SingleChatActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.mymessage_singlechat_aty);
        ButterKnife.bind(this);
    }
    @Override
    public void onClick(View v) {

    }
}
