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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.marmalade.aRevelation.R;
import com.github.marmalade.aRevelation.io.Entry;
import com.github.marmalade.aRevelation.io.Field;

import java.util.List;

/**
 * Author: <a href="mailto:alexey.kislin@gmail.com">Alexey Kislin</a>
 * Date: 9/4/13
 * Time: 2:00 AM
 */
public class EntryFieldsFragment extends ListFragment implements AdapterView
        .OnItemLongClickListener {

    private static final String ENTRY = "entry";
    private Entry entry;
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
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }

        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService
                (Context.CLIPBOARD_SERVICE);
        Field field = mAdapter.getItem(position);
        ClipData clip = ClipData.newPlainText("aRevelation data", field.getValue());
        clipboard.setPrimaryClip(clip);

        Toast.makeText(activity, activity.getString(R.string.field_copy_clipboard,
                field.getFieldName(activity)), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO Implement behaviour
        return false;
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
