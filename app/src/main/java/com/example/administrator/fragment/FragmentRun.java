package com.example.administrator.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.bean.PhotoWall;
import com.example.administrator.bean.SpacesItemDecoration;
import com.example.administrator.dao.PhotoWallDao;
import com.example.administrator.runforlife.R;
import com.example.administrator.runforlife.RunActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/2/002.
 */
public class FragmentRun extends Fragment {

    @InjectView(R.id.lr_fragmentrun_img)
    RecyclerView lrFragmentrunImg;
    @InjectView(R.id.iv_fragment_initimg)
    ImageView ivFragmentInitimg;
    @InjectView(R.id.ib_fragment_run)
    ImageButton ibFragmentRun;
    @InjectView(R.id.iv_fragment_list)
    ImageView ivFragmentColumn;
    /*    @InjectView(R.id.iv_fragment_grid)
        ImageView ivFragmentGrid;*/
    @InjectView(R.id.iv_fragment_stag)
    ImageView ivFragmentStag;

    private List<PhotoWall> photoWallList;
    private MyRLAdapter myRLAdapter;
    private Uri uri;
    private Bitmap bitmap;
    private int clickPosition;
    private SharedPreferences sdphotoWall;
    private PhotoWallDao photoWallDao;
    private PhotoWall photoWall;
    private PhotoWall deletePhotoWall;
    private boolean flag;
    private int size;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        photoWallDao = new PhotoWallDao(getActivity());

        View inflate = View.inflate(getActivity(), R.layout.fragment_run, null);
        ButterKnife.inject(this, inflate);

        sdphotoWall = getActivity().getSharedPreferences("photoWall", Context.MODE_APPEND);

        photoWallList = new ArrayList<>();

        //设置layoutManager，将布局设置为瀑布流
        lrFragmentrunImg.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        initData();

        itemClick();

        Log.d("FragmentRun", "photoWallList:---" + photoWallList);
        myRLAdapter = new MyRLAdapter(photoWallList);
        lrFragmentrunImg.setAdapter(myRLAdapter);

        //设置间距
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(5);
        lrFragmentrunImg.addItemDecoration(spacesItemDecoration);
        lrFragmentrunImg.setItemAnimator(new DefaultItemAnimator());

        scrollChangeState();

        photoWall = new PhotoWall();

        flag = true;

        return inflate;
    }

    private void scrollChangeState() {
        lrFragmentrunImg.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    ibFragmentRun.setVisibility(View.GONE);
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    ibFragmentRun.setVisibility(View.VISIBLE);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

        });
    }

    private void itemClick() {
        /*
        * 进入图库选择图片
        * */
        setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setType("image/*");
                startActivityForResult(intent, 0);
                clickPosition = position;
            }
        });

        /*
        * 长按删除图片
        * */
        setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                final int currentPosition = position;
                new AlertDialog.Builder(getActivity())
                        .setTitle("删除图片")
                        .setMessage("确定要删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myRLAdapter.removeData(currentPosition);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                deletePhotoWall = photoWallList.get(position);
                Log.d("FragmentRun", "longClick:" + photoWall);
                Toast.makeText(getActivity(), "long position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.iv_fragment_initimg, R.id.ib_fragment_run, R.id.iv_fragment_list, R.id.iv_fragment_stag})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_fragment_initimg://进入图库选图
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setType("image/*");
                startActivityForResult(intent, 0);
                ivFragmentInitimg.setVisibility(View.GONE);
                break;
            case R.id.ib_fragment_run://点击进入跑步页面
                startActivity(new Intent(getActivity(), RunActivity.class));
                break;
            case R.id.iv_fragment_list://切换成listview布局
                lrFragmentrunImg.setLayoutManager(new LinearLayoutManager(getActivity()));
                break;
          /*  case R.id.iv_fragment_grid://切换成gridview布局
                lrFragmentrunImg.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                break;*/
            case R.id.iv_fragment_stag://切换成StaggeredGrid布局
                lrFragmentrunImg.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                break;
        }
    }

    public class MyRLAdapter extends RecyclerView.Adapter<MyRecycleView> {

        private List<PhotoWall> photoWalls;

        public MyRLAdapter(List<PhotoWall> photoWalls) {
            this.photoWalls = photoWalls;
            Log.d("MyRLAdapter", "photoWalls:" + photoWalls);
        }

        @Override
        public MyRecycleView onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle, parent, false);
            MyRecycleView myRecycleView = new MyRecycleView(inflate);
            return myRecycleView;
        }

        @Override
        public void onBindViewHolder(final MyRecycleView holder, int position) {
            //holder.imageView.setImageResource(photoWalls.get(position).getImg());

            PhotoWall photoWallonAdapter = photoWalls.get(position);
            BitmapFactory.Options options = new BitmapFactory.Options();
            //增加负担，运行迟缓，不可在此操作
            zoomPic(photoWallonAdapter.picPath, options);
            bitmap = BitmapFactory.decodeFile(photoWallonAdapter.picPath, options);

            if (bitmap == null) {
                zoomPic(photoWall.picPath, options);
                bitmap = BitmapFactory.decodeFile(photoWall.picPath, options);
            }

            //new AsyncTask<PhotoWall,>()

            Log.d("MyRLAdapter", "bitmap:" + bitmap);
            holder.imageView.setImageBitmap(bitmap);

            //判断是否设置了监听器
            if (onItemClickListener != null) {
                //设置监听器
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int layoutPosition = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, layoutPosition);
                    }
                });
            }

            if (onItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int layoutPosition = holder.getLayoutPosition();
                        onItemLongClickListener.onItemLongClick(holder.itemView, layoutPosition);
                        //返回true，消耗此事件，使之不再继续传递
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            Log.d("MyRLAdapter", "photoWalls.size():" + photoWalls.size());
            return this.photoWalls.size();
        }

        /*
        * 增加数据
        */
        public void addData(int position) {
            PhotoWall addphotoWall = new PhotoWall(bitmap);
            Log.d("MyRLAdapter", "addData:" + photoWall);
            photoWalls.add(position, addphotoWall);
            Log.d("MyRLAdapter", "addData position:" + position);
            //将新加入的图片对象转换为String存入数据库

            //-----------------------二进制------------------------
            Log.d("MyRLAdapter", "photoWall.picPath: " + photoWall.picPath);
            photoWallDao.insertNum(photoWall.picPath);

            notifyItemInserted(position);
        }

        /*
        * 删除数据
        */
        public void removeData(int position) {
            //photoWallList.remove(position);
            photoWalls.remove(deletePhotoWall);
            //删除数据库中对应的数据
            photoWallDao.deleteNum(deletePhotoWall.picPath);

            notifyItemRemoved(position);
        }
    }

    private void initData() {

        photoWallList = photoWallDao.getAllPhotoWall();

        if (photoWallList == null) {
            photoWallList = new ArrayList<>();
        }
        if (photoWallList.size() == 0) {
            ivFragmentInitimg.setVisibility(View.VISIBLE);
        } else {
            //Toast.makeText(getActivity(), "view gone", Toast.LENGTH_SHORT).show();
            ivFragmentInitimg.setVisibility(View.GONE);
        }
        Log.d("FragmentRun", "photoWallList:===" + photoWallList);

        size = photoWallList.size();
        Log.d("FragmentRun", "size:" + size);

    }

    /*
    * 接收来自图库的图片，并对其进行缩放显示
    * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("FragmentRun", "requestCode:" + requestCode);
        if (requestCode == 0 && data != null) {
            uri = data.getData();
            Log.d("FragmentRun", "uri:" + uri);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
            Log.d("FragmentRun", "cursor:" + cursor);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            //detelephotoWall.picPath=picturePath;
            photoWall.picPath = picturePath;
            Log.d("FragmentRun", "picturePath：" + photoWall.picPath);
            cursor.close();

            //缩放图片
            BitmapFactory.Options options = new BitmapFactory.Options();
            zoomPic(picturePath, options);
            bitmap = BitmapFactory.decodeFile(picturePath, options);

            Log.d("FragmentRun", "uri:" + uri);
            myRLAdapter.addData(clickPosition);
        }
    }

    /*
    * 缩放图片
    * */
    private void zoomPic(String picturePath, BitmapFactory.Options zoomOptions) {
        //缩放图片
        //获得图片的宽高
        zoomOptions.inJustDecodeBounds = true;//指定为true, 加载器就不会获得图片, 但是opts里边的out开头的字段会设置.
        BitmapFactory.decodeFile(picturePath, zoomOptions);
        int imageWidth = zoomOptions.outWidth;
        int imageHeight = zoomOptions.outHeight;
        Log.d("FragmentRun", "imageWidth+imageHeight:" + (imageWidth + "---" + imageHeight));

        //获得屏幕的宽高
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        int screenWidth = defaultDisplay.getWidth();
        int screenHeight = defaultDisplay.getHeight();
        Log.d("FragmentRun", "width+height:" + (screenWidth + "===" + screenHeight));

        //计算缩放比例
        int widthScale = imageWidth / screenWidth;
        int heightScale = imageHeight / screenHeight;
        int scale = widthScale > heightScale ? widthScale : heightScale;
        Log.d("FragmentRun", "scale:" + scale);

        //根据缩放比例进行对图片的缩放
        zoomOptions.inJustDecodeBounds = false;
        zoomOptions.inSampleSize = scale;//指定加载器以scale比例进行缩放加载
    }

    /*
    * 点击监听事件
    */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /*
    *长按点击监听事件
    */
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /*
    *设置RecyclerView的样式
    */
    public static class MyRecycleView extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyRecycleView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_itemrecycle);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        Log.d("FragmentRun", "==========");
    }


}/* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_layout,menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.lv_menu:
                lrFragmentrunImg.setLayoutManager(new LinearLayoutManager(getActivity()));
                break;

            case R.id.sta_menu:
                lrFragmentrunImg.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                break;
        }

        return true;
    }*/
