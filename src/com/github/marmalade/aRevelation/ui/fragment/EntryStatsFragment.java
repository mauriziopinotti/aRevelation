package com.github.marmalade.aRevelation.ui.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.marmalade.aRevelation.R;
import com.github.marmalade.aRevelation.io.Entry;

/**
 * Created by sviro on 12/14/13.
 */
public class EntryStatsFragment extends Fragment {

    private static final String ENTRY = "entry";
    private Entry mEntry;
    private TextView mDescTextView;
    private TextView mNotesTextView;

    public static EntryStatsFragment newInstance(Entry entry) {
        EntryStatsFragment fragment = new EntryStatsFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ENTRY, entry);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mEntry = arguments.getParcelable(ENTRY);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            if (mEntry != null) {
                mDescTextView.setText(mEntry.getDescription());
                mNotesTextView.setText(mEntry.getNotes());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.entry_stats_layout, container, false);
        mDescTextView = (TextView) view.findViewById(R.id.descTextView);
        mNotesTextView = (TextView) view.findViewById(R.id.notesTextView);

        return view;
    }
}
