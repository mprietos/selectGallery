package net.opentrends.selectgallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;

import net.opentrends.selectgallery.adapters.FolderAdapter;
import net.opentrends.selectgallery.model.PhonePhoto;
import net.opentrends.selectgallery.model.PhotoFolder;
import net.opentrends.selectgallery.model.Photos;

import java.util.ArrayList;
import java.util.List;

public class FolderActivity extends AppCompatActivity implements ClickContract.ClickFolder {


    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 100;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private FolderAdapter mAdapter;


    private List<String> bucketIds = new ArrayList<>();
    private List<PhotoFolder> folders = new ArrayList<>();

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
                String imageUri;
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
                    imageId = cur.getString( imageIdColumn );
                    imageUri = cur.getString( imageUriColumn );

                    PhonePhoto phonePhoto = new PhonePhoto();
                    phonePhoto.setFolderName( bucketName );
                    phonePhoto.setPhotoPath( imageUri );
                    phonePhoto.setId( Integer.valueOf( imageId ) );
                    phonePhoto.setPressed(false);

                    if (bucketIds.contains(bucketId)) {
                        for ( PhotoFolder folder : folders ) {
                            if ( folder.getName().equals( bucketName ) ) {
                                folder.getPhotoList().add( phonePhoto );
                                break;
                            }
                        }
                    }else{

                        PhotoFolder folder = new PhotoFolder();
                        folder.setId( phonePhoto.getId() );
                        folder.setName( bucketName );
                        folder.setCoverPath( phonePhoto.getPhotoPath() );
                        folder.getPhotoList().add( phonePhoto );

                        folders.add( folder );
                        bucketIds.add(bucketId);

                    }

                }while (cur.moveToNext());
            }
        }

        cur.close();
    }


    private void initViews() {


        setGridAdapter();


        mRecyclerView = (RecyclerView)findViewById(R.id.rv_main_grid);
        mProgressBar = (ProgressBar)findViewById(R.id.pb_main);


        if (mAdapter == null){
            mAdapter = new FolderAdapter(getApplicationContext(), this, true, folders);
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void clickFolder(int pos) {
        Intent i = new Intent(getBaseContext(), ImagesActivity.class);

        i.putExtra(EXTRA_LIST, folders.get(pos));
        i.putExtra(EXTRA_POS, pos);
        startActivity(i);
    }

    @Override
    public void clickImage(int pos) {

    }

    @Override
    public void countSelectImages() {

    }

    private final static String EXTRA_POS = "posF";
    private final static String EXTRA_LIST = "listF";

}
