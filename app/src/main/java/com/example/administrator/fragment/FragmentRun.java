package com.example.administrator.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
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
    @InjectView(R.id.iv_fragment_column)
    ImageView ivFragmentColumn;

    private List<PhotoWall> photoWallList;
    private MyRLAdapter myRLAdapter;
    private Uri uri;
    private Bitmap bitmap;
    private int clickPosition;
    private int column=2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflate = View.inflate(getActivity(), R.layout.fragment_run, null);
        ButterKnife.inject(this, inflate);
        photoWallList = new ArrayList<>();
        //设置layoutManager
        lrFragmentrunImg.setLayoutManager(new StaggeredGridLayoutManager(column, StaggeredGridLayoutManager.VERTICAL));
        //initData();
        myRLAdapter = new MyRLAdapter(photoWallList);
        lrFragmentrunImg.setAdapter(myRLAdapter);
        //设置间距
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(5);
        lrFragmentrunImg.addItemDecoration(spacesItemDecoration);
        lrFragmentrunImg.setItemAnimator(new DefaultItemAnimator());

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

        itemClick();

        return inflate;
    }

    private void itemClick() {
        setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setType("image/*");
                startActivityForResult(intent, 0);
                clickPosition = position;
            }
        });

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
                Toast.makeText(getActivity(), "long position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        photoWallList = new ArrayList<PhotoWall>();
        PhotoWall photoWall = new PhotoWall(R.drawable.a10);
        photoWallList.add(photoWall);
        PhotoWall photoWall2 = new PhotoWall(R.drawable.a27);
        photoWallList.add(photoWall2);
        PhotoWall photoWall3 = new PhotoWall(R.drawable.a28);
        photoWallList.add(photoWall3);
        PhotoWall photoWall4 = new PhotoWall(R.drawable.a10);
        photoWallList.add(photoWall4);
    }

    @OnClick({R.id.iv_fragment_initimg, R.id.ib_fragment_run,R.id.iv_fragment_column})
    public void onClick(View view) {
        final String[] columns={"1","2","3","4","5","6"};
        switch (view.getId()) {
            case R.id.iv_fragment_initimg:
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setType("image/*");
                startActivityForResult(intent, 0);
                ivFragmentInitimg.setVisibility(View.GONE);
                break;
            case R.id.ib_fragment_run:
                startActivity(new Intent(getActivity(), RunActivity.class));
                break;
            case R.id.iv_fragment_column:
                new AlertDialog.Builder(getActivity())
                        .setTitle("设置列数")
                        .setSingleChoiceItems(columns, 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "columns[which]:" + columns[which], Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                column=which+1;
                                //---------------------刷新？？？---------------------------
                                //myRLAdapter.notify();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                break;
        }
    }

    /*
    *点击进入跑步页面
    */


    public class MyRLAdapter extends RecyclerView.Adapter<MyRecycleView> {

        private List<PhotoWall> photoWalls;

        public MyRLAdapter(List<PhotoWall> photoWalls) {
            this.photoWalls = photoWalls;
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
            return photoWalls.size();
        }

        /*
        * 增加数据
        */
        public void addData(int position) {
            photoWallList.add(position, new PhotoWall(bitmap));
            Log.d("MyRLAdapter", "uri11111:" + bitmap);
            notifyItemInserted(position);
        }

        /*
        * 删除数据
        */
        public void removeData(int position) {
            photoWallList.remove(position);
            notifyItemRemoved(position);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("FragmentRun", "requestCode:" + requestCode);
        Log.d("FragmentRun", "Activity.RESULT_OK:" + Activity.RESULT_OK);
        if (requestCode == 0 && data != null) {
            uri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            //缩放图片
            //获得图片的宽高
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//指定为true, 加载器就不会获得图片, 但是opts里边的out开头的字段会设置.
            BitmapFactory.decodeFile(picturePath, options);
            int imageWidth = options.outWidth;
            int imageHeight = options.outHeight;
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
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;//指定加载器以scale比例进行缩放加载
            bitmap = BitmapFactory.decodeFile(picturePath, options);

            Log.d("FragmentRun", "uri:" + uri);
            myRLAdapter.addData(clickPosition);
        }
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
    }

}
