package com.github.marmaladesky;

import java.util.List;

import com.github.marmaladesky.data.Entry;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class RevelationBrowserFragment extends Fragment {

	private RelativeLayout root;

	public static RevelationBrowserFragment newInstance(String uuidList) {
		RevelationBrowserFragment f = new RevelationBrowserFragment();

		Bundle args = new Bundle();
		args.putString("uuidList", uuidList);
		f.setArguments(args);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		List<Entry> groupUuid;
		View v = inflater.inflate(R.layout.revelation_structure_browser, container, false);
		try {
			if (savedInstanceState == null || savedInstanceState.getString("uuidList") == null)
				groupUuid = ((ARevelation) getActivity()).rvlData.getEntryGroupById(getArguments().getString("uuidList"));
			else
				groupUuid = ((ARevelation) getActivity()).rvlData.getEntryGroupById(savedInstanceState.getString("uuidList"));

			ListView simple = (ListView) v.findViewById(R.id.rootList);
			root = (RelativeLayout) v.findViewById(R.id.rootRvlBrowser);
			NodeArrayAdapter itemsAdapter = new NodeArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, groupUuid);
			simple.setOnItemClickListener(new ListListener());
			simple.setAdapter(itemsAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;

	}
	
	private class ListListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                Entry n = (Entry) parent.getItemAtPosition(position);
                if (!n.type.equals("folder")) {
                    EntryFragment nextFrag = EntryFragment.newInstance(n.getUuid());
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.list, nextFrag)
                            .addToBackStack(null).commit();
                } else {
					RevelationBrowserFragment nextFrag = RevelationBrowserFragment.newInstance(n.getUuid());
					getFragmentManager()
							.beginTransaction()
							.replace(R.id.list, nextFrag)
							.addToBackStack(null).commit();
				}
            } catch(Exception e) {
                e.printStackTrace();
                throw e;
            }
		}

	}
	
	private void showListView(List<Entry> nodes) {
		ListView child = new ListView(root.getContext());
		child.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		child.setAdapter(
				new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, new String[] {"One","Two","Three"}));
		root.addView(child);
		child.bringToFront();
		child.setBackgroundResource(android.R.color.black);
		// child.setX(child.getX() + 40); Not now
		child.setAdapter(new NodeArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, nodes));
		child.setOnItemClickListener(new ListListener());
	}

}
