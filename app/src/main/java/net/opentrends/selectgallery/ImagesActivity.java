package net.opentrends.selectgallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ProgressBar;

import net.opentrends.selectgallery.adapters.FolderAdapter;
import net.opentrends.selectgallery.adapters.ImageAdapter;
import net.opentrends.selectgallery.model.PhonePhoto;
import net.opentrends.selectgallery.model.PhotoFolder;

public class ImagesActivity extends AppCompatActivity implements ClickContract.ClickFolder {

    private int folderPosition;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ImageAdapter mAdapter;
    private PhotoFolder folder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        setTitle("Selecciona imagen");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

        }

        folder = (PhotoFolder) getIntent().getSerializableExtra(EXTRA_LIST);

        folderPosition = getIntent().getExtras().getInt(EXTRA_POS);

        initViews();

    }

    private void initViews() {

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main_grid);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_main);

        if (mAdapter == null) {
            mAdapter = new ImageAdapter(getApplicationContext(), this, folder.getPhotoList());
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clickFolder(int pos) {

    }

    @Override
    public void clickImage(int pos) {
        Intent i = new Intent(this, ImageViewActivity.class);
        i.putExtra(EXTRA_PATH, folder.getPhotoList().get(pos).getPhotoPath());
        startActivity(i);
    }

    @Override
    public void countSelectImages() {
        int counts = 0;
        for (PhonePhoto photo:folder.getPhotoList()) {
            if (photo.isPressed()){
                counts++;
            }
        }

        if (counts == 0){
            setTitle("Selecciona imagen");
        }else if (counts == 1){
            setTitle("1 seleccionada");
        }else{
            setTitle("" + counts + " seleccionadas");
        }


    }

    private final static String EXTRA_POS = "posF";
    private final static String EXTRA_LIST = "listF";

    private static final String EXTRA_PATH = "path";
    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
