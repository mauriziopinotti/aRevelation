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
package com.github.marmalade.aRevelation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        AdapterView.OnItemLongClickListener, IBackPressedListener {

    public static interface ReadFileCallback {
        public void readFile(Uri uri, String password);
    }

    private static final String BLOCKED = "isBlocked";
    private static final String POSITION = "position";
    private static final String TOP = "top";
    private static final String FILE = "file";

    private int savedScrollBarPosition;
    private int top;
    private List<Entry> entries;
    private ArrayAdapter<Entry> entryArrayAdapter;
    private boolean isBlocked;
    private Uri mUri;


    /**
     * This constructor is used on restore if the process was killed.
     * You shouldn't remove it.
     */
    public FileEntriesFragment() {
    }

    ;

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
            isBlocked = savedInstanceState.getBoolean(BLOCKED);
            savedScrollBarPosition = savedInstanceState.getInt(POSITION);
            top = savedInstanceState.getInt(TOP);
            Log.w("aRevelation", String.valueOf(isBlocked));
        } else {
            askPassword();
        }

        entries = new ArrayList<>();
        entryArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, entries);
        setListAdapter(entryArrayAdapter);
    }

    private void askPassword() {
        final AskPasswordDialogFragment d = new AskPasswordDialogFragment();

        AskPasswordDialogFragment.AskPasswordOnClickListener dialogClickListener = d.new
                AskPasswordOnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Activity activity = getActivity();
                        if (activity != null) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    if (activity instanceof ReadFileCallback) {
                                        ((ReadFileCallback) activity).readFile(mUri, d.editText.getEditableText().toString());
                                        setListShown(false);
                                    }
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    if (activity != null) {
                                        activity.finish();
                                    }
                                    break;
                            }

                        }
                    }
                };

        d.setOnClickListener(dialogClickListener);
        d.show(getFragmentManager(), null);
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

//
//    @Override
//    public void onPause() {
//        // Save previous position
//        savedScrollBarPosition = lv.getFirstVisiblePosition();
//        top = (lv.getChildAt(0) == null) ? 0 : lv.getChildAt(0).getTop();
//        super.onPause();
//    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        blockAccess();
        outState.putBoolean(BLOCKED, isBlocked);
        outState.putInt(POSITION, savedScrollBarPosition);
        outState.putInt(TOP, top);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        Entry selectedEntry = entryArrayAdapter.getItem(position);
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
            intent.putExtra(EntryActivity.PASSWORD, "test");
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final Activity activity = getActivity();
        if (activity == null) {
            return true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //TODO get rid of the enum
        final CharSequence[] items = LongClickActionItems.getCharSequences();
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals(LongClickActionItems.copySecretData.toString())) {
                    ClipboardManager clipboard = (ClipboardManager) activity.getSystemService
                            (Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("pass",
                            entryArrayAdapter.getItem(position).getSecretFieldData());
                    clipboard.setPrimaryClip(clip);
                }
            }
        });

        Dialog d = builder.create();
        d.show();
        return true;
    }


    /**
     * Block access to decrypted data on application exit (home button pressed)
     */
    public void blockAccess() {
        // The adapter could be null on restore access if cancel button is pressed
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
//                        if(password.equals(d.editText.getEditableText().toString())) {
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
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        entryArrayAdapter = new ArrayAdapter<Entry>(activity, android.R.layout.simple_list_item_1, entries);
        setListAdapter(entryArrayAdapter);
        entryArrayAdapter.notifyDataSetChanged();
    }

    public void setEntries(List<Entry> entries) {
        setListShown(true);

        this.entries = entries;
        updateEntries();
    }


    /**
     * Menu items of entry manipulating
     */
    private static enum LongClickActionItems {
        copySecretData;

        @Override
        public String toString() {
            switch (this) {
                case copySecretData:
                    return "Copy secret data";
                default:
                    return super.toString();
            }
        }

        static CharSequence[] getCharSequences() {
            CharSequence[] result = new CharSequence[values().length];
            for (int i = 0; i < values().length; i++) {
                result[i] = values()[i].toString();
            }
            return result;
        }
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
