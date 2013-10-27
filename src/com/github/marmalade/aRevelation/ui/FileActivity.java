package com.github.marmalade.aRevelation.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.github.marmalade.aRevelation.FileEntriesFragment;
import com.github.marmalade.aRevelation.R;

/**
 * Created by sviro on 10/27/13.
 */
public class FileActivity extends Activity {
    public final static String FILE_ENTRIES_FRAGMENT = "FileEntriesFragment";

    public static final String DECRYPTED_DATA = "decrypted_data";
    public static final String PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        String decryptedXML = null;
        String password = null;
        Intent intent = getIntent();
        if (intent != null) {
            decryptedXML = intent.getStringExtra(DECRYPTED_DATA);
            password = intent.getStringExtra(PASSWORD);
        }

        if (decryptedXML == null || password == null) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.mainLayout,
                    FileEntriesFragment.newInstance(decryptedXML, password),
                    FILE_ENTRIES_FRAGMENT)
                    .commit();

        }

    }
}
