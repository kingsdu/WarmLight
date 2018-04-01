package com.c317.warmlight.android.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.CommentItem;
import com.c317.warmlight.android.common.Application_my;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/28.
 */

public class CommentActivity extends AppCompatActivity {

    @Bind(R.id.lv_read_comment)
    ListView lvReadComment;
    @Bind(R.id.et_read_input)
    EditText etReadInput;
    @Bind(R.id.btn_read_submit)
    Button btnReadSubmit;
    @Bind(R.id.btn_read_replay)
    Button btnReadReplay;
    @Bind(R.id.ll_read_comment)
    LinearLayout llReadComment;
    ArrayList<CommentItem> commentItemArrayList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.read_comment_main);
        ButterKnife.bind(this);
//        initData();
    }

    private void initData() {
        CommentItem commentItem = new CommentItem();
        commentItem.commentID = 0;
        commentItem.userID = 2016+"";
        commentItem.agreeNum = "6";
        commentItem.userName = "LBJ";
        commentItem.comTime = "2017-11-11";
        commentItem.searchID = 1;

        for(int i=0;i<10;i++){
            commentItemArrayList.add(commentItem);
        }
    }


}
