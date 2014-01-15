/*
 * Copyright 2013 Aleksey Kislin
 * Copyright 2013 Michal Švirec
 *
 * This file is part of aRevelation.
 *
 * aRevelation is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aRevelation is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aRevelation.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.marmalade.aRevelation.ui.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.marmalade.aRevelation.IBackPressedListener;
import com.github.marmalade.aRevelation.R;
import com.github.marmalade.aRevelation.ui.fragment.AskPasswordDialogFragment
        .OnPasswordSubmitListener;
import com.github.marmalade.aRevelation.io.Entry;
import com.github.marmalade.aRevelation.io.Entry.EntryType;
import com.github.marmalade.aRevelation.ui.EntryActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: <a href="mailto:alexey.kislin@gmail.com">Alexey Kislin</a>
 * Date: 8/31/13
 * Time: 8:54 PM
 */
public class FileEntriesFragment extends ListFragment implements
        AdapterView.OnItemLongClickListener, IBackPressedListener, OnPasswordSubmitListener {

    private static final String ASK_PASSWORD_DIALOG = "ask_password_dialog";

    public static interface ReadFileCallback {
        public void readFile(Uri uri, String password);
    }

    private static final String ENTRIES = "entries";

    private ArrayList<Entry> entries;
    private ArrayAdapter<Entry> entryArrayAdapter;
    private Uri mUri;

    public static FileEntriesFragment newInstance() {
        FileEntriesFragment fragment = new FileEntriesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUri = getActivity().getIntent().getData();

        if (savedInstanceState != null) {
            Log.w("aRevelation", "savedInstanceState");
            entries = savedInstanceState.getParcelableArrayList(ENTRIES);
        } else {
            entries = new ArrayList<>();
            askPassword();
        }

        entryArrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, entries);
        setListAdapter(entryArrayAdapter);
    }

    private void askPassword() {
        final AskPasswordDialogFragment d = new AskPasswordDialogFragment();
        d.show(getChildFragmentManager(), ASK_PASSWORD_DIALOG);
    }

    @Override
    public void onPasswordSubmit(String password) {
        Activity activity = getActivity();
        if (activity != null && activity instanceof ReadFileCallback) {
            ((ReadFileCallback) activity).readFile(mUri, password);
            setListShown(false);
        }
    }

    @Override
    public void onPasswordCancel() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setOnItemLongClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ENTRIES, entries);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        Entry selectedEntry = entryArrayAdapter.getItem(position);
        //TODO remake selction of the folder
        if (selectedEntry.getType() == EntryType.TYPE_FOLDER) {
            try {
                Entry nonReal = new Entry("...", null, null, null, null,
                        EntryType.TYPE_NON_REAL, new ArrayList<>(entries));
                entryArrayAdapter = new ArrayAdapter<>(activity,
                        android.R.layout.simple_list_item_1, new ArrayList<>(selectedEntry
                        .getChildren()));
                entryArrayAdapter.insert(nonReal, 0);
                entries = new ArrayList<>();
                entries.add(nonReal);
                entries.addAll(selectedEntry.getChildren());
                setListAdapter(entryArrayAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
            entryArrayAdapter.notifyDataSetChanged();
        } else if (selectedEntry.getType() == EntryType.TYPE_NON_REAL) {
            entryArrayAdapter = new ArrayAdapter<>(activity,
                    android.R.layout.simple_list_item_1, new ArrayList<>(selectedEntry
                    .getChildren()));
            entries = new ArrayList<>(selectedEntry.getChildren());
            setListAdapter(entryArrayAdapter);
            entryArrayAdapter.notifyDataSetChanged();
        } else {
            Intent intent = new Intent(activity.getApplicationContext(), EntryActivity.class);
            intent.putExtra(EntryActivity.ENTRY, selectedEntry);
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final Activity activity = getActivity();
        if (activity == null) {
            return true;
        }

        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService
                (Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("pass",
                entryArrayAdapter.getItem(position).getSecretFieldData());
        clipboard.setPrimaryClip(clip);

        Toast.makeText(activity, R.string.secret_data_copy_clipboard, Toast.LENGTH_SHORT).show();

        return true;
    }

    private void updateEntries() {
        entryArrayAdapter.notifyDataSetChanged();
    }

    public void setEntries(List<Entry> entries) {
        setListShown(true);

        this.entries.clear();
        this.entries.addAll(entries);

        updateEntries();
    }

    @Override
    public void onBackPressed() {
        if (entryArrayAdapter.getCount() > 0 && entryArrayAdapter.getItem(0).getType() ==
                EntryType.TYPE_NON_REAL) {
            getListView().performItemClick(null, 0, 0);
        }
    }

}
