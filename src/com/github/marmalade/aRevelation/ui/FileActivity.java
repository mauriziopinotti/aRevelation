package com.github.marmalade.aRevelation.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.github.marmalade.aRevelation.ui.fragment.FileEntriesFragment;
import com.github.marmalade.aRevelation.ui.fragment.FileEntriesFragment.ReadFileCallback;
import com.github.marmalade.aRevelation.R;
import com.github.marmalade.aRevelation.io.Entry;
import com.github.marmalade.aRevelation.ui.fragment.ErrorDialogFragment;
import com.github.marmalade.aRevelation.ui.fragment.ErrorDialogFragment.OnErrorDialogCloseListener;
import com.github.marmalade.aRevelation.ui.fragment.FileOpenRetainFragment;
import com.github.marmalade.aRevelation.ui.fragment.FileOpenRetainFragment.OnReadFileListener;

import java.util.List;

/**
 * Created by sviro on 10/27/13.
 */
public class FileActivity extends FragmentActivity implements OnReadFileListener, ReadFileCallback, OnErrorDialogCloseListener {

    private static final String READ_FILE_FRAGMENT = "read_file_fragment";
    private static final String ERROR_DIALOG = "error_dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri path = null;
        Intent intent = getIntent();
        if (intent != null) {
            path = intent.getData();
        }

        if (path == null) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(FileOpenRetainFragment.newInstance(), READ_FILE_FRAGMENT).commit();
        }


        setContentView(R.layout.file_layout);
    }

    @Override
    public void readFile(Uri uri, String password) {
        FileOpenRetainFragment fragment = (FileOpenRetainFragment) getSupportFragmentManager().findFragmentByTag(READ_FILE_FRAGMENT);

        if (fragment != null) {
            fragment.readFile(uri, password);
        }
    }

    @Override
    public void onFileRead(List<Entry> entries) {
        FileEntriesFragment fileEntriesFragment = (FileEntriesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fileEntriesFragment);

        if (fileEntriesFragment != null) {
            fileEntriesFragment.setEntries(entries);
        }
    }

    @Override
    public void onFileReadFail(String error) {
        ErrorDialogFragment.newInstance(error).show(getSupportFragmentManager(), ERROR_DIALOG);
    }

    @Override
    public void onErrorDialogClose() {
        finish();
    }
}
