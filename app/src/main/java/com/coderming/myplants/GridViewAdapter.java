package com.coderming.myplants;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by linna on 4/14/2016.
 */
public class GridViewAdapter extends ArrayAdapter {
    private static final String LOG_TAG = GridViewAdapter.class.getSimpleName();

//    List<PlantItem> data;
    private Context context;
    private int layoutResourceId;
    private boolean mDisplayby;

    public GridViewAdapter(Context context, int layoutResourceId, boolean displayBy, List<PlantItem> list ) {
        super(context,  layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        mDisplayby = displayBy;
        for (PlantItem item : list) {
            if (item.mDrawable == null ) {
                item.mDrawable = getDrawableImage(item.getImageFilename(), 300);
            }
        }
        super.addAll(list);
    }

    private Bitmap getDrawableImage(String imageFilename, int imageSize) {
        InputStream is =  null;
        try {
            is = context.getAssets().open(imageFilename);
            Bitmap  thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(is), imageSize, imageSize);
            return thumbnail;
        } catch (IOException ex) {
            if (is != null) {
                try { is.close();} catch (Exception e) { }
            }
        }
        return null;
    }

    @Override
    public PlantItem getItem(int position) {
        return (PlantItem) super.getItem(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CustomerViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new CustomerViewHolder(row);
            holder.mImageView = (ImageView) row.findViewById(R.id.imageView2);
            row.setTag(holder);
        } else {
            holder = (CustomerViewHolder) row.getTag();
        }

        PlantItem item = this.getItem(position);
        holder.mImageView.setImageBitmap(item.mDrawable);
        if (mDisplayby)
            holder.mTextView.setText(item.mCommonName);
        else
            holder.mTextView.setText(item.mScientificName);

        return row;
    }
    public void resetList(List<PlantItem> list, boolean displayBy) {
        this.mDisplayby = displayBy;
        this.clear();
        this.addAll(list);
    }
    class CustomerViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;

        public CustomerViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView2);
            mTextView = (TextView) itemView.findViewById((R.id.textView));
        }
    }
}
