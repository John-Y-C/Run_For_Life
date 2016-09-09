package com.example.administrator.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.bean.PhotoWall;
import com.example.administrator.db.PhotoWallDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/17/017.
 */
public class PhotoWallDao {

    SQLiteDatabase db;

    public PhotoWallDao(Context context)
    {
        PhotoWallDBHelper photoWallDBHelper = new PhotoWallDBHelper(context, "MyPhotoWall.db", null, 1);
        db = photoWallDBHelper.getReadableDatabase();
    }

    public void insertNum(String path)
    {
        //存储二进制-------------
        Log.d("PhotoWallDao", path);
        db.execSQL("insert into photowall values('"+path+"');");
    }

    public void deleteNum(String path)
    {
        //where picuri='"+picuri+"'
        db.execSQL("delete from photowall where path='"+path+"'");
    }

    public List<PhotoWall> getAllPhotoWall()
    {
        List<PhotoWall> list=new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from photowall ", null);
        while (cursor.moveToNext())
        {
            String picPath = cursor.getString(0);
            PhotoWall photoWall=new PhotoWall();
            //photoWall.picUri=picuri;
          /*  Uri parse = Uri.parse(picuri);
            photoWall.uri=parse;*/
            photoWall.picPath=picPath;
            list.add(photoWall);
        }

        return list;
    }

/*    public int getAllNumCount()
    {
        Cursor cursor = db.rawQuery("select count(*) from photowall ", null);
        int count = cursor.getCount();
        return count;
    }*/


}
