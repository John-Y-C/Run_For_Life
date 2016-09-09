package com.example.administrator.bean;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Administrator on 2016/9/5/005.
 */
public class PhotoWall {
    public int img;
    public Uri uri;
    public Bitmap bitmap;
    public String picUri;
    public String picPath;

    public PhotoWall(String picPath) {
        this.picPath = picPath;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public PhotoWall() {

    }

    public String getPicUri() {
        return picUri;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }

    public PhotoWall(Bitmap bitmap) {
        this.bitmap = bitmap;

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public PhotoWall(Uri uri)
    {

        this.uri=uri;
    }

    public PhotoWall(int img) {
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
