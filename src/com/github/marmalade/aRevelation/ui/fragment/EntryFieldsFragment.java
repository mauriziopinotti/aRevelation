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
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.github.marmalade.aRevelation.R;
import com.github.marmalade.aRevelation.io.Entry;
import com.github.marmalade.aRevelation.io.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: <a href="mailto:alexey.kislin@gmail.com">Alexey Kislin</a>
 * Date: 9/4/13
 * Time: 2:00 AM
 */
public class EntryFieldsFragment extends ListFragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private static final String ENTRY = "entry";
    private static final String BLOCKED = "blocked";
    private Entry entry;
    private boolean isBlocked;
    FieldsAdapter mAdapter;

    public static EntryFieldsFragment newInstance(Entry entry) {
        EntryFieldsFragment fragment = new EntryFieldsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ENTRY, entry);

        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * This constructor is used on restore if the process was killed.
     * You shouldn't remove it.
     */
    public EntryFieldsFragment() {
    }

    ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState != null) {
            entry = savedInstanceState.getParcelable(ENTRY);
            isBlocked = savedInstanceState.getBoolean(BLOCKED);
        } else {
            Bundle arguments = getArguments();
            if (arguments != null) {
                entry = arguments.getParcelable(ENTRY);
            }
        }

        if (entry != null) {
            mAdapter = new FieldsAdapter(getActivity(), 0, entry.getFields());
            setListAdapter(mAdapter);
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
        outState.putParcelable(ENTRY, entry);
        blockAccess();
        outState.putBoolean(BLOCKED, isBlocked);
    }


    public void blockAccess() {
//        isBlocked = true;
//        data.clear();
//        mAdapter.notifyDataSetChanged();
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
//                switch (which){
//                    case DialogInterface.BUTTON_POSITIVE:
//                        if(password.equals(d.mPasswordEditText.getEditableText().toString())) {
//                            showRevelationEntry(entry, activity);
//                            isBlocked = false;
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



    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        final CharSequence[] items= ClickActionItems.getCharSequences();
//        builder.setItems(items,new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(items[which].equals(ClickActionItems.copy.toString())) {
//                    ClipboardManager clipboard = (ClipboardManager) activity.getSystemService
// (Context.CLIPBOARD_SERVICE);
//                    Map<String, String> item = (Map<String, String>)lv.getAdapter().getItem
// (position);
//                    if(item.values().size() == 2) {
//                        ClipData clip = ClipData.newPlainText("aRevelation data",
// item.get(ROW_DATA_IDENTIFIER));
//                        clipboard.setPrimaryClip(clip);
//                    }
//                }
//            }
//        });
//
//        Dialog d = builder.create();
//        d.show();
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO Implement behaviour
        return false;
    }


    private static enum ClickActionItems {
        copy;

        @Override
        public String toString() {
            switch (this) {
                case copy:
                    return "Copy";
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

    private static class FieldsAdapter extends ArrayAdapter<Field> {

        public FieldsAdapter(Context context, int resource, List<Field> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            Context context = getContext();
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(android.R.layout
                        .simple_list_item_2, parent, false);
            } else {
                view = convertView;
            }

            Field field = getItem(position);
            ((TextView) view.findViewById(android.R.id.text1)).setText(field.getFieldName(context));
            ((TextView) view.findViewById(android.R.id.text2)).setText(field.getValue());

            return view;
        }
    }

}
