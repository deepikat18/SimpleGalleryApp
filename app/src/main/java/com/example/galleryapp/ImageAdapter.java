package com.example.galleryapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    public Context mContext;

    public ImageAdapter(Context mContext){
        this.mContext = mContext;
    }
    public int[] imageArray = {
            R.drawable.images,
            R.drawable.images__2_,
            R.drawable.images__1_,
            R.drawable.images__3_,
            R.drawable.images__4_,
            R.drawable.download,
            R.drawable.download__1_,
            R.drawable.download__2_,
            R.drawable._375153_bigthumbnail,
            R.drawable.download__2_,
            R.drawable._375153_bigthumbnail
    };
    @Override
    public int getCount() {
        return imageArray.length;
    }

    @Override
    public Object getItem(int position) {
        return imageArray[position];
    }



    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(mContext);


        imageView.setImageResource(imageArray[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(240,350));
        return imageView;
    }


}
