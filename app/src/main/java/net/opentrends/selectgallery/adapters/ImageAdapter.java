package net.opentrends.selectgallery.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.opentrends.selectgallery.ClickContract;
import net.opentrends.selectgallery.R;
import net.opentrends.selectgallery.model.PhonePhoto;
import net.opentrends.selectgallery.model.PhotoFolder;

import java.io.File;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private  List<PhonePhoto> data;
    private ClickContract.ClickFolder mListener;

    public ImageAdapter(Context mContext, ClickContract.ClickFolder mListener, List<PhonePhoto> photoList) {
        this.mContext = mContext;
        this.mListener = mListener;
        mInflater = LayoutInflater.from(mContext);
        this.data = photoList;
    }
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = mInflater.inflate(R.layout.image_item, parent, false);
        return new ImageAdapter.ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PhonePhoto photo = data.get(position);
        File b = new File(Environment.getExternalStorageDirectory(), photo.getPhotoPath());

        Glide.with(mContext)
                .load(photo.getPhotoPath()) // Uri of the picture
                .into(holder.mImageGrid);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photo.isPressed()){
                    holder.mImageGrid.setImageAlpha(255);
                    holder.mSelected.setVisibility(View.INVISIBLE);
                    photo.setPressed(false);
                    mListener.countSelectImages();
                }else {
                    mListener.clickImage(position);
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (photo.isPressed()){
                    holder.mImageGrid.setImageAlpha(255);
                    holder.mSelected.setVisibility(View.INVISIBLE);
                    photo.setPressed(false);
                }else{
                    photo.setPressed(true);
                    holder.mImageGrid.setImageAlpha(150);
                    holder.mSelected.setVisibility(View.VISIBLE);
                }
                mListener.countSelectImages();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return  0;

        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView mImageGrid;
        LinearLayout mSelected;
        public ViewHolder(View view){
            super(view);
            mView = view;
            mImageGrid = (ImageView)mView.findViewById(R.id.gv_image);
            mSelected = (LinearLayout)mView.findViewById(R.id.selected);
        }


    }
}
