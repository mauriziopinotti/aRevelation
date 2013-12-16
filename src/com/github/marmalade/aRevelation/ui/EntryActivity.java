package com.github.marmalade.aRevelation.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateUtils;

import com.github.marmalade.aRevelation.ui.fragment.EntryFieldsFragment;
import com.github.marmalade.aRevelation.io.Entry;
import com.github.marmalade.aRevelation.R;
import com.github.marmalade.aRevelation.ui.fragment.EntryStatsFragment;

/**
 * Created by sviro on 10/27/13.
 */
public class EntryActivity extends FragmentActivity {

    public static final String ENTRY = "entry";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_layout);

        Entry entry = null;
        Intent intent = getIntent();
        if (intent != null) {
            entry = intent.getParcelableExtra(ENTRY);
        }

        if (entry == null) {
            finish();
            return;
        }

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(entry.getName());
        actionBar.setSubtitle(entry.getTypeFormatted() + " - Updated " + DateUtils.getRelativeDateTimeString(this, entry.getUpdated(), DateUtils.MINUTE_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0));

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.entry_fields, EntryFieldsFragment.newInstance(entry));
            fragmentTransaction.replace(R.id.entry_stats, EntryStatsFragment.newInstance(entry));

            fragmentTransaction.commit();
        }
    }
}
