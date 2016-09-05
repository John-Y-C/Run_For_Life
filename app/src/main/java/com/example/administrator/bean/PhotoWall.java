package com.example.administrator.bean;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Administrator on 2016/9/5/005.
 */
public class PhotoWall {
    private int img;
    private Uri uri;
    private Bitmap bitmap;

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
