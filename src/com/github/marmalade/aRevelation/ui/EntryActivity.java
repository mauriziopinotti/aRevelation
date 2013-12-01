package com.github.marmalade.aRevelation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.github.marmalade.aRevelation.ui.fragment.EntryFragment;
import com.github.marmalade.aRevelation.io.Entry;
import com.github.marmalade.aRevelation.R;

/**
 * Created by sviro on 10/27/13.
 */
public class EntryActivity extends FragmentActivity {

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
            entry = intent.getParcelableExtra(ENTRY);
            password = intent.getStringExtra(PASSWORD);
        }

        if (entry == null || password == null) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainLayout,
                    new EntryFragment(entry, password), ENTRY_FRAGMENT).commit();
        }
    }
}
