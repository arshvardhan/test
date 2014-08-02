package com.kelltontech.maxisgetit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kelltontech.maxisgetit.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int[] galleryItemIds={R.drawable.classified_ads_banner,R.drawable.hot_deals_banner,R.drawable.find_more_customers_banner,R.drawable.classified_ads_banner,R.drawable.hot_deals_banner,R.drawable.find_more_customers_banner,R.drawable.classified_ads_banner,R.drawable.hot_deals_banner,R.drawable.find_more_customers_banner};
    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return galleryItemIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	imageView=(ImageView)((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.home_gallery_item, null);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setBackgroundDrawable(mContext.getResources().getDrawable(galleryItemIds[position]));
//        imageView.setImageDrawable(mContext.getResources().getDrawable(galleryItemIds[position]));
        return imageView;
    }
}