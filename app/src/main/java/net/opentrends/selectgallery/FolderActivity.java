package net.opentrends.selectgallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;

import net.opentrends.selectgallery.adapters.FolderAdapter;
import net.opentrends.selectgallery.model.Folder;
import net.opentrends.selectgallery.model.Photos;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FolderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, ClickContract.ClickFolder {


    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 100;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private FolderAdapter mAdapter;


    private List<String> bucketIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            initViews();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_READ_EXTERNAL_STORAGE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initViews();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //
    private void setGridAdapter() {
        // Create a new grid adapter


        String[] projection = new String[] {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.DISPLAY_NAME};

        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Make the query.
        Cursor cur = getContentResolver().query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                sortOrder   // Ordering
        );

        List<Photos> items = new ArrayList<Photos>();
        if ( cur != null && cur.getCount() > 0 ) {

            if (cur.moveToFirst()) {
                String bucketId;
                String bucketName;
                String data;
                String imageId;
                int bucketNameColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                int imageUriColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.DATA);

                int imageIdColumn = cur.getColumnIndex(
                        MediaStore.Images.Media._ID );

                int imageNameColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.DISPLAY_NAME );

                int bucketID = cur.getColumnIndex(
                        MediaStore.Images.Media.BUCKET_ID );


                do {
                    bucketName = cur.getString( bucketNameColumn );
                    bucketId= cur.getString( bucketID );
                    if (!bucketIds.contains(bucketId)) {
                        bucketIds.add(bucketId);
                        Log.e("hola", "uyyyy");
                        items.add(new Photos(cur.getString(imageUriColumn), bucketName));
                    }

                }while (cur.moveToNext());
            }
        }

        cur.close();
        mAdapter.setData(items);

        /*

        String directoryPath = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath();
        List<Photos> items = new ArrayList<Photos>();
        File[] files = new File(directoryPath).listFiles(new ImageFileFilter());
        for (File file : files) {

            // Add the directories containing images or sub-directories
            if (file.isDirectory()
                    && file.listFiles(new ImageFileFilter()).length > 0) {
                items.add(new Photos(file.getAbsolutePath(), file.getName()));
            }
        }
        mAdapter.setData(items);
        */

    }


    //sorts based on the files name
    public class SortFileName implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            return f1.getName().compareTo(f2.getName());
        }
    }

    //sorts based on a file or folder. folders will be listed first
    public class SortFolder implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            if (f1.isDirectory() == f2.isDirectory())
                return 0;
            else if (f1.isDirectory() && !f2.isDirectory())
                return -1;
            else
                return 1;
        }
    }

    private void initViews() {





        mRecyclerView = (RecyclerView)findViewById(R.id.rv_main_grid);
        mProgressBar = (ProgressBar)findViewById(R.id.pb_main);


        if (mAdapter == null){
            mAdapter = new FolderAdapter(getApplicationContext(), this, true);
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mAdapter);

        setGridAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                FolderAdapter.uri,
                FolderAdapter.projections,
                null,
                null,
                FolderAdapter.sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void clickFolder(int pos) {
        Intent i = new Intent(getBaseContext(), ImagesActivity.class);
        i.putExtra(EXTRA_POS, pos);
        startActivity(i);
    }

    @Override
    public void clickImage(int pos) {

    }


    private boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".png"))
        // Add other formats as desired
        {
            return true;
        }
        return false;
    }
    /**
     * This can be used to filter files.
     */
    private class ImageFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            else if (isImageFile(file.getAbsolutePath())) {
                return true;
            }
            return false;
        }
    }

    private final static String EXTRA_POS = "posF";


}
