package net.opentrends.selectgallery;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import net.opentrends.selectgallery.adapters.FolderAdapter;
import net.opentrends.selectgallery.model.Folder;

public class ImagesActivity extends AppCompatActivity implements ClickContract.ClickFolder, LoaderManager.LoaderCallbacks<Cursor> {

    private int folderPosition;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private FolderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel);
        }
        
        folderPosition  = getIntent().getExtras().getInt(EXTRA_POS);
        
        initViews();

    }

    private void initViews() {

        getSupportLoaderManager().initLoader(1, null, this);

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_main_grid);
        mProgressBar = (ProgressBar)findViewById(R.id.pb_main);

        if (mAdapter == null){
            mAdapter = new FolderAdapter(getApplicationContext(), this, false);
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
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

    }

    private final static String EXTRA_POS = "posF";

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 1) {

            return new CursorLoader(this,
                    FolderAdapter.uri,
                    FolderAdapter.projections,
                    FolderAdapter.projections[3] + " = \"" + FolderAdapter.getData().get(folderPosition).getName() + "\"",
                    null,
                    FolderAdapter.sortOrder);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.setData(Folder.getData(false, data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
