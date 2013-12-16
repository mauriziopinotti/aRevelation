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
import com.github.marmalade.aRevelation.ui.fragment.AskPasswordDialogFragment.OnPasswordSubmitListener;
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
    private static final String BLOCKED = "isBlocked";
    private static final String FILE = "file";

    private ArrayList<Entry> entries;
    private ArrayAdapter<Entry> entryArrayAdapter;
    private boolean isBlocked;
    private Uri mUri;


    /**
     * This constructor is used on restore if the process was killed.
     * You shouldn't remove it.
     */
    public FileEntriesFragment() {
    }

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
            isBlocked = savedInstanceState.getBoolean(BLOCKED);
            Log.w("aRevelation", String.valueOf(isBlocked));
        } else {
            entries = new ArrayList<>();
            askPassword();
        }

        entryArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, entries);
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
    public void onStart() {
        super.onStart();
//        try {
//            if(isBlocked) {
//                restoreAccess();
//            } else {
//                entries = Entry.parseDecryptedXml(decryptedXML);
//                updateEntries();
//                lv.setSelectionFromTop(savedScrollBarPosition, top);
//            }
//        } catch (Exception e) {
//            //TODO Process error
//            e.printStackTrace();
//        }
        // Restore previous position
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        blockAccess();
        outState.putParcelableArrayList(ENTRIES, entries);
        outState.putBoolean(BLOCKED, isBlocked);
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
                        EntryType.TYPE_NON_REAL, new ArrayList<Entry>(entries));
                entryArrayAdapter = new ArrayAdapter<Entry>(activity,
                        android.R.layout.simple_list_item_1, new ArrayList<Entry>(selectedEntry
                        .getChildren()));
                entryArrayAdapter.insert(nonReal, 0);
                entries = new ArrayList<Entry>();
                entries.add(nonReal);
                entries.addAll(selectedEntry.getChildren());
                setListAdapter(entryArrayAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
            entryArrayAdapter.notifyDataSetChanged();
        } else if (selectedEntry.getType() == EntryType.TYPE_NON_REAL) {
            entryArrayAdapter = new ArrayAdapter<Entry>(activity,
                    android.R.layout.simple_list_item_1, new ArrayList<Entry>(selectedEntry
                    .getChildren()));
            entries = new ArrayList<Entry>(selectedEntry.getChildren());
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


    /**
     * Block access to decrypted data on application exit (home button pressed)
     */
    public void blockAccess() {
        // The mAdapter could be null on restore access if cancel button is pressed
        if (entryArrayAdapter != null)
            entryArrayAdapter.clear();
        isBlocked = true;
    }


    /**
     * Restore access on application open
     */
    private void restoreAccess() {
//        final AskPasswordDialogFragment d = new AskPasswordDialogFragment();
//
//        AskPasswordDialogFragment.AskPasswordOnClickListener dialogClickListener =  d.new
// AskPasswordOnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case DialogInterface.BUTTON_POSITIVE:
//                        if(password.equals(d.mPasswordEditText.getEditableText().toString())) {
//                            try {
//                                entries = Entry.parseDecryptedXml(decryptedXML);
//                                updateEntries();
//                                lv.setSelectionFromTop(savedScrollBarPosition, top);
//                                isBlocked = false;
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            restoreAccess();
//                        }
//                        break;
//
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        ((MainActivity)getActivity()).reload(); // Go to file menu
//                        break;
//                }
//            }
//        };
//
//        d.setOnClickListener(dialogClickListener);
//        d.setCancelable(false);
//        d.show(getFragmentManager(), null);
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
        if (entryArrayAdapter.getCount() > 0 && entryArrayAdapter.getItem(0).getType() == EntryType.TYPE_NON_REAL)
            getListView().performItemClick(null, 0, 0);
        else {
            getFragmentManager().popBackStack();
        }
    }
}
