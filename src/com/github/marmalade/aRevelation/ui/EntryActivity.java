package com.github.marmalade.aRevelation.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.github.marmalade.aRevelation.EntryFragment;
import com.github.marmalade.aRevelation.FileEntriesFragment.Entry;
import com.github.marmalade.aRevelation.R;

/**
 * Created by sviro on 10/27/13.
 */
public class EntryActivity extends Activity {

    public final static String ENTRY_FRAGMENT = "EntryFragment";

    public static final String PASSWORD = "pasword";
    public static final String ENTRY = "entry";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        String password = null;
        Entry entry = null;
        Intent intent = getIntent();
        if (intent != null) {
            entry = (Entry) intent.getSerializableExtra(ENTRY);
            password = intent.getStringExtra(PASSWORD);
        }

        if (entry == null || password == null) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainLayout,
                    new EntryFragment(entry, password), ENTRY_FRAGMENT).commit();
        }
    }
}
