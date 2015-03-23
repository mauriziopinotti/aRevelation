package com.github.marmaladesky;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TwoLineListItem;
import com.github.marmaladesky.data.Entry;
import com.github.marmaladesky.data.Field;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EntryFragment extends Fragment {

    private static final String ROW_HEADER_IDENTIFIER = "First Line";
    private static final String ROW_DATA_IDENTIFIER = "Second Line";

    private static final String HEADER_KEY = "header";
    private static final String PASSWORD_KEY = "password";

    private Entry entry;

    private ListView simple;

    public static EntryFragment newInstance(Entry node) {
        EntryFragment f = new EntryFragment();

        Bundle args = new Bundle();
        args.putSerializable("entry", node);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState == null || savedInstanceState.getSerializable("entry") == null)
            entry = (Entry) getArguments().getSerializable("entry");
        else
            entry = (Entry) savedInstanceState.getSerializable("entry");


        View v = inflater.inflate(R.layout.revelation_entry_browser, container, false);

        simple = (ListView) v.findViewById(R.id.entryList);

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

        DateFormat dateFormatter = ARevelation.dateFormatter;
        values = new HashMap<String, String>();
        values.put(ROW_HEADER_IDENTIFIER, getString(R.string.updated));
        values.put(ROW_DATA_IDENTIFIER, dateFormatter.format(new Date(entry.updated*1000L))); // In python world it is seconds
        data.add(values);

        SimpleAdapter itemsAdapter = new SimpleAdapter(
                this.getActivity(), data,
                android.R.layout.simple_list_item_2,
                new String[] {ROW_HEADER_IDENTIFIER, ROW_DATA_IDENTIFIER },
                new int[] {android.R.id.text1, android.R.id.text2 }) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                try {
                    HashMap<String, String> o = (HashMap<String, String>) simple.getItemAtPosition(position);
                    if(getString(R.string.generic_password).equals(o.get(ROW_HEADER_IDENTIFIER))) {
                        TwoLineListItem v = (TwoLineListItem)super.getView(position, convertView, parent);
                        v.getText2().setTransformationMethod(PasswordTransformationMethod.getInstance());
                        return v;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return super.getView(position, convertView, parent);
            }
        };

        simple.setAdapter(itemsAdapter);
        simple.setOnItemClickListener(new PasswordOnClickListener());
        return v;
    }

    private String getGenericHeader(String name) {
        String packageName = getActivity().getPackageName();
        int resId = getResources().getIdentifier(name.replace('-', '_'), "string", packageName);
        if(resId == 0) return name;
        else return getString(resId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(entry != null) outState.putSerializable("entry", entry);
    }

    public class PasswordOnClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TwoLineListItem item = (TwoLineListItem) view;

            String header = item.getText1().getText().toString();
            String password = item.getText2().getText().toString();
            ItemDialogFragment.newInstance(header, password).show(getFragmentManager(), "Tag");
        }
    }

    public static class ItemDialogFragment extends DialogFragment {

        public String header;
        public String password;

        public static ItemDialogFragment newInstance(String header, String password) {
            ItemDialogFragment f = new ItemDialogFragment();
            Bundle args = new Bundle();
            args.putString(HEADER_KEY, header);
            args.putString(PASSWORD_KEY, password);
            f.setArguments(args);
            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if(savedInstanceState != null && savedInstanceState.getString(HEADER_KEY) != null)
                header = savedInstanceState.getString(HEADER_KEY);
            else
                header = getArguments().getString(HEADER_KEY);

            if(savedInstanceState != null && savedInstanceState.getString(PASSWORD_KEY) != null)
                password = savedInstanceState.getString(PASSWORD_KEY);
            else
                password = getArguments().getString(PASSWORD_KEY);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(new String[] { "Copy" }, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("pass", password);
                            clipboard.setPrimaryClip(clip);
                        }
                    });
            return builder.create();
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString(HEADER_KEY, header);
            outState.putString(PASSWORD_KEY, password);
        }
    }

}
