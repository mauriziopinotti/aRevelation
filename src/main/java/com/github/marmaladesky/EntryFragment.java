package com.github.marmaladesky;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.github.marmaladesky.data.Entry;
import com.github.marmaladesky.data.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EntryFragment extends Fragment {

    private static final String ROW_HEADER_IDENTIFIER = "First Line";
    private static final String ROW_DATA_IDENTIFIER = "Second Line";

    private Entry entry;

    public static EntryFragment newInstance(Entry node) {
        EntryFragment f = new EntryFragment();

        Bundle args = new Bundle();
        args.putSerializable("entry", node);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState == null || savedInstanceState.getSerializable("entry") == null)
            entry = (Entry) getArguments().getSerializable("entry");
        else
            entry = (Entry) savedInstanceState.getSerializable("entry");


        View v = inflater.inflate(R.layout.revelation_entry_browser, container, false);

        ListView simple = (ListView) v.findViewById(R.id.entryList);

        ArrayList<Map<String, String>> data = new ArrayList<>();

        HashMap<String, String> values = new HashMap<>();

        values.put(ROW_HEADER_IDENTIFIER, getString(R.string.name));
        values.put(ROW_DATA_IDENTIFIER, entry.getName());
        data.add(values);
        values = new HashMap<String, String>();
        values.put(ROW_HEADER_IDENTIFIER, getString(R.string.description));
        values.put(ROW_DATA_IDENTIFIER, entry.getDescription());
        data.add(values);

        for(Field f : entry.fields) {
            values = new HashMap<>();
            values.put(ROW_HEADER_IDENTIFIER, getGenericHeader(f.id));
            values.put(ROW_DATA_IDENTIFIER, f.getValue());
            data.add(values);
        }
        values = new HashMap<String, String>();
        values.put(ROW_HEADER_IDENTIFIER, getString(R.string.notes));
        values.put(ROW_DATA_IDENTIFIER, entry.getNotes());
        data.add(values);

        values = new HashMap<String, String>();
        values.put(ROW_HEADER_IDENTIFIER, getString(R.string.updated));
        values.put(ROW_DATA_IDENTIFIER, Integer.toString(entry.updated));
        data.add(values);

        SimpleAdapter itemsAdapter = new SimpleAdapter(
                this.getActivity(), data,
                android.R.layout.simple_list_item_2,
                new String[] {ROW_HEADER_IDENTIFIER, ROW_DATA_IDENTIFIER },
                new int[] {android.R.id.text1, android.R.id.text2 });

        simple.setAdapter(itemsAdapter);

        return v;
    }

    private String getGenericHeader(String name) {
        switch (name) {
            case "generic-hostname" : return getString(R.string.generic_hostname);
            case "generic-username" : return getString(R.string.generic_username);
            case "generic-password" : return getString(R.string.generic_password);
            case "generic-database" : return getString(R.string.generic_database);
            case "generic-domain"   : return getString(R.string.generic_domain);
            case "generic-url"   : return getString(R.string.generic_url);
        }
        return name;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(entry != null) outState.putSerializable("entry", entry);
    }
}
