package com.c317.warmlight.android.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.c317.warmlight.android.bean.Collect_Article_Detail;
import com.c317.warmlight.android.bean.Collect_Article_Info;
import com.c317.warmlight.android.bean.Collect_Date_Details;
import com.c317.warmlight.android.bean.DateNews;
import com.c317.warmlight.android.bean.DateNews_detalis;
import com.c317.warmlight.android.bean.GroupNewsDTO;
import com.c317.warmlight.android.bean.Smallnews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/3/14.
 */

public class WarmLightDataBaseHelper extends SQLiteOpenHelper {

    private static WarmLightDataBaseHelper instance;
    public static final String DATABASE_NAME = "WarmLight_db";
    private static final int SCHEMA_VERSION = 1;//版本号,则是升级之后的,升级方法请看onUpgrade方法里面的判断
    private static SQLiteDatabase db;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 友约表及字段
     */
    public static final String DATE_TABLENAME = "date";
    public static final String DATE_ID = "activity_id";
    private static final String DATE_TITLE = "title";
    private static final String DATE_PICTURE = "picture";
    private static final String DATE_PLACE = "place";
    private static final String DATE_COORDINATE = "coordinate";
    private static final String DATE_STARTTIME = "startTime";
    private static final String DATE_ENDTIME = "endTime";
    public static final String DATE_SAVE_ID = "saveId";
    public static final String DATE_LASTTIME = "lastTime";
    public static final String DATE_ISDEL = "isDel";

    /**
     * 有读表及字段
     */
    public static final String READ_TABLENAME = "read";
    public static final String READ_ID = "article_id";
    public static final String READ_SAVE_ID = "save_id";
    public static final String READ_ISDEL = "isDel";
    public static final String READ_LASTTIME = "lastTime";
    public static final String READ_TITLE = "title";
    public static final String READ_PICTUREURL = "pictureURL";


    /**
     * 群消息表及字段
     */
    public static final String GROUPMESSAGE_TABLENAME = "groupmessage";
    public static final String GROUPMESSAGE_CHATID = "chat_id";
    public static final String GROUPMESSAGE_LASTTIME = "lastTime";
    public static final String GROUPMESSAGE_ACCOUNT = "account";
    public static final String GROUPMESSAGE_GROUPID = "group_id";
    public static final String GROUPMESSAGE_CHATTIME = "chatTime";
    public static final String GROUPMESSAGE_CONTENT = "content";
    public static final String GROUPMESSAGE_ISREAD = "isread";

    public static synchronized WarmLightDataBaseHelper getDatebaseHelper(
            Context context) {
        if (instance == null) {
            instance = new WarmLightDataBaseHelper(context);
        }
        return instance;
    }

    public WarmLightDataBaseHelper(Context context) {//构造函数,接收上下文作为参数,直接调用的父类的构造函数
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        db = getWritableDatabase();// 如果数据库不存在则会调用onCreate函数
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * 创建友约表
         * */
        String sql = "create table " + DATE_TABLENAME + "("
                + DATE_ID + " integer primary key autoincrement, "
                + DATE_TITLE + " text, "
                + DATE_PICTURE + " text, "
                + DATE_PLACE + " text, "
                + DATE_COORDINATE + " text, "
                + DATE_STARTTIME + " text, "
                + DATE_ENDTIME + " text, "
                + DATE_ISDEL + " text, "
                + DATE_LASTTIME + " text, "
                + DATE_SAVE_ID + " integer)";
        db.execSQL(sql);


        /**
         * 创建有读表
         * */
        sql = "create table " + READ_TABLENAME + "("
                + READ_ID + " integer primary key autoincrement, "
                + READ_SAVE_ID + " integer, "
                + READ_ISDEL + " text, "
                + READ_LASTTIME + " text, "
                + READ_TITLE + " text, "
                + READ_PICTUREURL + " text)";
        db.execSQL(sql);

        /**
         * 创建群消息表
         */
        sql = "create table " + GROUPMESSAGE_TABLENAME + "("
                + GROUPMESSAGE_CHATID + " integer primary key autoincrement, "
                + GROUPMESSAGE_LASTTIME + " text ,"
                + GROUPMESSAGE_ACCOUNT + " text ,"
                + GROUPMESSAGE_GROUPID + " integer, "
                + GROUPMESSAGE_CHATTIME + " text ,"
                + GROUPMESSAGE_CONTENT + " text ,"
                + GROUPMESSAGE_ISREAD + " integer, UNIQUE("
                + GROUPMESSAGE_CONTENT + ", " + GROUPMESSAGE_CHATTIME + ", "
                + GROUPMESSAGE_CHATID + ") ON CONFLICT REPLACE )";
        db.execSQL(sql);
    }


    /**
     * 友约是否收藏数据至数据库
     *
     * @params table:数据库名称（read）、id：传入的id值、isCollect：isCollect的值、isCollectName：isCollect的名称（创建是存储变量不同，值相同）
     * @author Du
     * @Date 2018/3/13 21:10
     **/
    public void InsertCollectInfoDate(Collect_Date_Details collect_date_details) {
        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(DATE_ID, collect_date_details.data.activity_id);
            cv.put(DATE_TITLE, collect_date_details.data.title);
            cv.put(DATE_PICTURE, collect_date_details.data.picture);
            cv.put(DATE_PLACE, collect_date_details.data.place);
            cv.put(DATE_COORDINATE, collect_date_details.data.place);
            cv.put(DATE_STARTTIME, collect_date_details.data.startTime);
            cv.put(DATE_ENDTIME, collect_date_details.data.endTime);
            if(collect_date_details.data.isDel == false){
                cv.put(DATE_ISDEL,"0");
            }
            cv.put(DATE_LASTTIME, collect_date_details.data.endTime);
            cv.put(DATE_SAVE_ID, collect_date_details.data.save_id);
            db.insert(DATE_TABLENAME, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            db.endTransaction();
        }
    }


    public void InsertCollectInfoRead(Collect_Article_Detail.Collect_Article_Item collect_article_item) {
        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(READ_ID, collect_article_item.article_id);
            cv.put(READ_SAVE_ID, collect_article_item.save_id);
            cv.put(READ_PICTUREURL, collect_article_item.pictureURL);
            if(collect_article_item.isDelete == false){
                cv.put(READ_ISDEL,"0");
            }
            cv.put(READ_LASTTIME, collect_article_item.lastTime);
            cv.put(READ_TITLE, collect_article_item.title);
            db.insert(READ_TABLENAME, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            db.endTransaction();
        }
    }


    /**
     * 根据列名、id，更新已收藏
     *
     * @params tableName：数据库名称  id：id值、idName：id名称、isCollectName：isCollect名称
     * @author Du
     * @Date 2018/3/14 22:33
     **/
    public int updateCollectState(String tableName, String id, String idName, String isCollectName) {
        String where = idName + " = ? ";
        String[] whereValue = {id + ""};
        ContentValues cv = new ContentValues();
        cv.put(isCollectName, "1");
        return db.update(tableName, cv, where, whereValue);
    }



    /**
     * 根据列名、id，取消已收藏
     *
     * @params tableName：数据库名称  id：id值、idName：id名称、isCollectName：isCollect名称
     * @author Du
     * @Date 2018/3/14 22:34
     **/
    public int unUpdateCollectState(String tableName, String id, String idName, String isCollectName) {
        String where = idName + " = ? ";
        String[] whereValue = {id + ""};
        ContentValues cv = new ContentValues();
        cv.put(isCollectName, "0");
        return db.update(tableName, cv, where, whereValue);
    }



    /**
     * 根据tableName、id、dateName,查询是否收藏  -- 有读
     *
     * @params tableName：数据库名称  id：id值、idName：id名称
     * @author Du
     * @Date 2018/3/13 22:15
     **/
    public String queryIsCollectRead(String id) {
        String isCollect = null;
        String where = READ_ID + " = ? ";
        String[] whereValue = {id + ""};
        Cursor cursor = db.query(READ_TABLENAME, null, where, whereValue,
                null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
        }
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            isCollect = cursor.getString(2);//isDel
        }
        cursor.close();
        return isCollect;
    }



    /**
     * 查询存储的SaveID
     *
     * @params tableName：数据库名称  id：id值、idName：id名称
     * @author Du
     * @Date 2018/3/13 22:15
     **/
    public int queryIsCollectSaveID_READ(String id) {
        int isCollect = 0;
        String where = READ_ID + " = ? ";
        String[] whereValue = {id};
        Cursor cursor = db.query(READ_TABLENAME, null, where, whereValue,
                null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
        }
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            isCollect = cursor.getInt(1);//SaveId
        }
        cursor.close();
        return isCollect;
    }



    /**
     * 根据tableName、id、dateName,查询是否收藏  -- 友约
     *
     * @params tableName：数据库名称  id：id值、idName：id名称
     * @author Du
     * @Date 2018/3/13 22:15
     **/
    public String queryIsCollectDate_SaveID(String id) {
        String isCollect = null;
        String where = DATE_ID + " = ? ";
        String[] whereValue = {id + ""};
        Cursor cursor = db.query(DATE_TABLENAME, null, where, whereValue,
                null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
        }
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            isCollect = cursor.getString(9);
        }
        cursor.close();
        return isCollect;
    }


    /**
     * 根据tableName、id、dateName,查询是否收藏  -- 友约
     *
     * @params tableName：数据库名称  id：id值、idName：id名称
     * @author Du
     * @Date 2018/3/13 22:15
     **/
    public String queryIsCollectDate_isDel(String id) {
        String isCollect = null;
        String where = DATE_ID + " = ? ";
        String[] whereValue = {id + ""};
        Cursor cursor = db.query(DATE_TABLENAME, null, where, whereValue,
                null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
        }
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            isCollect = cursor.getString(7);
        }
        cursor.close();
        return isCollect;
    }







    /**
     * 批量插入群消息
     */
    public void batchInsertGroupNews(Collection<GroupNewsDTO.GroupNewsDTO_Content> newsDTOs_contents) {
        try {
            db.beginTransaction();
            for (GroupNewsDTO.GroupNewsDTO_Content newmessage : newsDTOs_contents) {
                ContentValues cv = new ContentValues();
                cv.put(GROUPMESSAGE_CHATID, newmessage.getChat_id());
                cv.put(GROUPMESSAGE_LASTTIME, newmessage.getLastTime());
                cv.put(GROUPMESSAGE_ACCOUNT, newmessage.getAccount());
                cv.put(GROUPMESSAGE_GROUPID, newmessage.getGroup_id());
                cv.put(GROUPMESSAGE_CHATTIME, newmessage.getChatTime());
                cv.put(GROUPMESSAGE_CONTENT, newmessage.getContent());
                cv.put(GROUPMESSAGE_ISREAD, newmessage.getReadStatus());
                db.insert(GROUPMESSAGE_TABLENAME, null, cv);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 查询多条已读群消息
     */
    public List<GroupNewsDTO.GroupNewsDTO_Content> queryMultiGroupNewsRead(int group_id) {
//        db = getWritableDatabase();// 如果数据库不存在则会调用onCreate函数
        ArrayList<GroupNewsDTO.GroupNewsDTO_Content> messages = new ArrayList<GroupNewsDTO.GroupNewsDTO_Content>();
        String where = GROUPMESSAGE_GROUPID + " =? ";
        String[] whereValue = {group_id + ""};
        Cursor cursor = db.query(GROUPMESSAGE_TABLENAME, null, where, whereValue,
                null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            return messages;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GroupNewsDTO.GroupNewsDTO_Content message = new GroupNewsDTO.GroupNewsDTO_Content();
            message.setChat_id(cursor.getInt(0));
            message.setLastTime(cursor.getString(1));
            message.setAccount(cursor.getString(2));
            message.setGroup_id(cursor.getInt(3));
            message.setChatTime(cursor.getString(4));
            message.setContent(cursor.getString(5));
            message.setRead(cursor.getInt(6));
            messages.add(message);
            cursor.moveToNext();
        }
        cursor.close();
        return messages;
    }

    /**
     * 查询所有未读群消息
     */
    public List<GroupNewsDTO.GroupNewsDTO_Content> queryMultiGroupNewsUnRead(int group_id) {
        ArrayList<GroupNewsDTO.GroupNewsDTO_Content> messages = new ArrayList<GroupNewsDTO.GroupNewsDTO_Content>();
        String where = GROUPMESSAGE_GROUPID + " =? and " + GROUPMESSAGE_ISREAD
                + " =?";
        String[] whereValue = {group_id + "", "0"};
        Cursor cursor = db.query(GROUPMESSAGE_TABLENAME, null, where,
                whereValue, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            return messages;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GroupNewsDTO.GroupNewsDTO_Content message = new GroupNewsDTO.GroupNewsDTO_Content();
            message.setChat_id(cursor.getInt(0));
            message.setLastTime(cursor.getString(1));
            message.setAccount(cursor.getString(2));
            message.setGroup_id(cursor.getInt(3));
            message.setChatTime(cursor.getString(4));
            message.setContent(cursor.getString(5));
            message.setRead(cursor.getInt(6));
            messages.add(message);
            cursor.moveToNext();
        }
        cursor.close();
        return messages;
    }

    /**
     * 更新群所有未读消息为已读
     */
    public int updataGroupNews(int group_id) {
        String where = GROUPMESSAGE_GROUPID + " =? and " + GROUPMESSAGE_ISREAD
                + "=0";
        String[] whereValue = {group_id + ""};
        ContentValues cv = new ContentValues();
        cv.put(GROUPMESSAGE_ISREAD, "1");
        return db.update(GROUPMESSAGE_TABLENAME, cv, where, whereValue);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
