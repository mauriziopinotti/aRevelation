package com.github.marmalade.aRevelation.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.marmalade.aRevelation.AskPasswordDialogFragment;
import com.github.marmalade.aRevelation.FileEntriesFragment;
import com.github.marmalade.aRevelation.R;

import java.io.File;

/**
 * Created by sviro on 10/27/13.
 */
public class FileActivity extends Activity {

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

        setContentView(R.layout.file_layout);
    }
}
