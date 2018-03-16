package com.c317.warmlight.android.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;

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
    public static final String DATE_ISCOLLECT = "iscollect";


    /**
     * 有读表及字段
     */
    public static final String READ_TABLENAME = "read";
    public static final String READ_ID = "article_id";
    public static final String READ_ISCOLLECT = "iscollect";


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
                + DATE_ISCOLLECT + " text)";
        db.execSQL(sql);

        /**
         * 创建有读表
         * */
        sql = "create table " + READ_TABLENAME + "("
                + READ_ID + " integer primary key autoincrement, "
                + READ_ISCOLLECT + " text)";
        db.execSQL(sql);

    }


    /**
     * 插入是否收藏数据至数据库
     *
     * @params  table:数据库名称（read）、id：传入的id值、isCollect：isCollect的值、isCollectName：isCollect的名称（创建是存储变量不同，值相同）
     * @author Du
     * @Date 2018/3/13 21:10
     **/
    public void InsertCollectInfo(String table,String id,String isCollect,String idName,String isCollectName) {
        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(idName, id);
            cv.put(isCollectName, isCollect);
            db.insert(table, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            db.endTransaction();
        }
    }

    /**
    * 根据列名、id，更新已收藏
    * @params tableName：数据库名称  id：id值、idName：id名称、isCollectName：isCollect名称
    * @author Du
    * @Date 2018/3/14 22:33
    **/
    public int updateCollectState(String tableName, String id, String idName, String isCollectName) {
        String where = idName + " = ? ";
        String[] whereValue = {id+ ""};
        ContentValues cv = new ContentValues();
        cv.put(isCollectName, "1");
        return db.update(tableName, cv, where, whereValue);
    }

    /**
    * 根据列名、id，取消已收藏
    * @params tableName：数据库名称  id：id值、idName：id名称、isCollectName：isCollect名称
    * @author Du
    * @Date 2018/3/14 22:34
    **/
    public int unUpdateCollectState(String tableName, String id, String idName,String isCollectName) {
        String where = idName + " = ? ";
        String[] whereValue = {id+ ""};
        ContentValues cv = new ContentValues();
        cv.put(isCollectName, "0");
        return db.update(tableName, cv, where, whereValue);
    }

    /**
     * 根据tableName、id、dateName,查询是否收藏
     *
     * @params  tableName：数据库名称  id：id值、idName：id名称
     * @author Du
     * @Date 2018/3/13 22:15
     **/
    public String queryIsCollect(String tableName, String id, String idName) {
        String isCollect = null;
        String where = idName + " = ? ";
        String[] whereValue = {id + ""};
        Cursor cursor = db.query(tableName, null, where, whereValue,
                null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
        }
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            isCollect = cursor.getString(1);
        }
        cursor.close();
        return isCollect;
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
