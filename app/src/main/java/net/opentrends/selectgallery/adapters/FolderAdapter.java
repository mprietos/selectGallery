package net.opentrends.selectgallery.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.opentrends.selectgallery.ClickContract;
import net.opentrends.selectgallery.R;
import net.opentrends.selectgallery.model.PhotoFolder;

import java.io.File;
import java.util.List;


public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    public static final Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public static final String[] projections = {MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DISPLAY_NAME};
    public static String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

    private Context mContext;
    private LayoutInflater mInflater;
    private  List<PhotoFolder> data;
    private ClickContract.ClickFolder mListener;
    private boolean isFolder;

    public FolderAdapter(Context mContext, ClickContract.ClickFolder mListener, boolean isFolder, List<PhotoFolder> folders) {
        this.mContext = mContext;
        this.mListener = mListener;
        mInflater = LayoutInflater.from(mContext);
        this.data = folders;
        this.isFolder = isFolder;
    }

    @Override
    public FolderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = mInflater.inflate(R.layout.folder_item, parent, false);
        return new FolderAdapter.ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PhotoFolder photo = data.get(position);

        if (isFolder){
            holder.mTextGrid.setText(photo.getName() == null ? "-1" : photo.getName());
        }else{
            holder.mTextGrid.setVisibility(View.GONE);
        }

        File b = new File(Environment.getExternalStorageDirectory(), photo.getCoverPath());

        Glide.with(mContext)
                .load(photo.getCoverPath()) // Uri of the picture
                .into(holder.mImageGrid);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mListener.clickFolder(position);

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
        TextView mTextGrid;

        public ViewHolder(View view){
            super(view);
            mView = view;
            mImageGrid = (ImageView)mView.findViewById(R.id.gv_image);
            mTextGrid = (TextView)mView.findViewById(R.id.gv_title);
        }


    }
}
