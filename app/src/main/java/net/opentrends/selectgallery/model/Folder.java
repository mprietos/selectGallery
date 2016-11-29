package net.opentrends.selectgallery.model;

import android.database.Cursor;
import android.util.Log;

import net.opentrends.selectgallery.adapters.FolderAdapter;

import java.util.ArrayList;
import java.util.List;

public class Folder {
    public static boolean isFolder;

    public static List<Photos> getData(boolean home, Cursor cursor) {
        isFolder = home;
        List<Photos> photos = new ArrayList<>();
        List<String> bucketIds = new ArrayList<>();
        List<String> imagePaths = new ArrayList<>();

        String[] projections = FolderAdapter.projections;
        Log.d("SelectGallery", "PhotosData");
        while (cursor.moveToNext()) {
            String imageName = cursor.getString(cursor.getColumnIndex(projections[4]));
            String imageFolder= cursor.getString(cursor.getColumnIndex(projections[2]));
            String mId = cursor.getString(cursor.getColumnIndex(projections[3]));
            String imagePath = cursor.getString(cursor.getColumnIndex(projections[1]));
            Photos model;

            if (home) {
                model = new Photos( imagePath, null);
                if (!bucketIds.contains(mId)) {
                    photos.add(model);
                    bucketIds.add(mId);
                }
            } else {
                model = new Photos(imagePath, imageName);

                if (!imagePaths.contains(imagePath)) {
                    photos.add(model);
                    //imagePaths.add(imagePath);
                }
            }
        }
        return photos;
    }
}
