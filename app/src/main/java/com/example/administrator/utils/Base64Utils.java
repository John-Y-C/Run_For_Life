package com.example.administrator.utils;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by Administrator on 2016/9/6/006.
 */
public class Base64Utils {

    public static String saveListToString(List list) {
        String savedList = null;
        try {
            //实例化一个ByteArrayOutPutStream对象，用来装载压缩后的字节文件
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //将得到的字符数据装载到ObjectOutPutStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            //使用writeObject方法负责写入特定类的对象的状态，以便相应的readObject方法可以还原它
            objectOutputStream.writeObject(list);
            //用Base64.encode将字节文件转换成Base64编码保存在String中
            savedList = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            //关闭objectOutputStream
            objectOutputStream.close();
            byteArrayOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return savedList;
    }

    public static List getStringToList(String str) {
        List gotString = null;
        try {
            byte[] decode = Base64.decode(str.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decode);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            gotString = (List) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return gotString;
    }
}
