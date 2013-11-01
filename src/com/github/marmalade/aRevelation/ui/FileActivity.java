package com.github.marmalade.aRevelation.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.github.marmalade.aRevelation.AskPasswordDialogFragment;
import com.github.marmalade.aRevelation.FileEntriesFragment;
import com.github.marmalade.aRevelation.R;

import java.io.File;

/**
 * Created by sviro on 10/27/13.
 */
public class FileActivity extends Activity {
    public final static String FILE_ENTRIES_FRAGMENT = "FileEntriesFragment";

    public static final String PATH = "path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        String path = null;
        Intent intent = getIntent();
        if (intent != null) {
            path = intent.getStringExtra(PATH);
        }

        if (path == null) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.mainLayout, FileEntriesFragment.newInstance(path),
                    FILE_ENTRIES_FRAGMENT).commit();
        }

    }
}
