package com.github.marmalade.aRevelation.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.marmalade.aRevelation.AskPasswordDialogFragment;
import com.github.marmalade.aRevelation.FileEntriesFragment;
import com.github.marmalade.aRevelation.FileEntriesFragment.Entry;
import com.github.marmalade.aRevelation.FileEntriesFragment.ReadFileCallback;
import com.github.marmalade.aRevelation.R;
import com.github.marmalade.aRevelation.ui.fragment.FileOpenFragment;
import com.github.marmalade.aRevelation.ui.fragment.FileOpenFragment.OnReadFileListener;

import java.io.File;
import java.util.List;

/**
 * Created by sviro on 10/27/13.
 */
public class FileActivity extends Activity implements OnReadFileListener, ReadFileCallback {

    private static final String READ_FILE_FRAGMENT = "read_file_fragment";

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
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(FileOpenFragment.newInstance(), READ_FILE_FRAGMENT).commit();
        }


        setContentView(R.layout.file_layout);
    }

    @Override
    public void readFile(Uri uri, String password) {
        FileOpenFragment fragment = (FileOpenFragment) getFragmentManager().findFragmentByTag(READ_FILE_FRAGMENT);

        if (fragment != null) {
            fragment.readFile(uri, password);
        }
    }

    @Override
    public void onFileRead(List<Entry> entries) {
        FileEntriesFragment fileEntriesFragment = (FileEntriesFragment) getFragmentManager()
                .findFragmentById(R.id.fileEntriesFragment);

        if (fileEntriesFragment != null) {
            fileEntriesFragment.setEntries(entries);
        }
    }

    @Override
    public void onFileReadFail() {
        //TODO show dialog with error
        finish();
    }
}
