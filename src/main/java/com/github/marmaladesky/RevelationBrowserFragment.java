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
import com.github.marmaladesky.data.RevelationData;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class RevelationBrowserFragment extends Fragment {
	
	private ListView simple;
	
	private RelativeLayout root;

    String rvlXml;

	public static RevelationBrowserFragment newInstance(String rvlXml) {
        RevelationBrowserFragment f = new RevelationBrowserFragment();

        Bundle args = new Bundle();
        args.putString("rvlXml", rvlXml);
        f.setArguments(args);

        return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState   ) {

        if(savedInstanceState == null || savedInstanceState.getString("rvlXml") == null)
            rvlXml = getArguments().getString("rvlXml");
         else
            rvlXml = savedInstanceState.getString("rvlXml");

		View v = inflater.inflate(R.layout.revelation_structure_browser, container, false);
		simple = (ListView) v.findViewById(R.id.rootList);
		root = (RelativeLayout) v.findViewById(R.id.rootRvlBrowser);

        RevelationData example;

        try {
            Serializer serializer = new Persister();
            example = serializer.read(RevelationData.class, rvlXml, false);
            System.out.println(example);

		
            NodeArrayAdapter itemsAdapter = new NodeArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, example.list);

            simple.setOnItemClickListener(new ListListener());

            simple.setAdapter(itemsAdapter);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
		
		
		return v;
	}
	
	private class ListListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                Entry n = (Entry) parent.getItemAtPosition(position);
                if (!n.type.equals("folder")) {
                    EntryFragment nextFrag = EntryFragment.newInstance(n);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainContainer, nextFrag)
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
		child.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		child.setAdapter(
				new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, new String[] {"One","Two","Three"}));
		root.addView(child);
		child.bringToFront();
		child.setBackgroundResource(android.R.color.black);
		// child.setX(child.getX() + 40); Not now
		child.setAdapter(new NodeArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, nodes));
		child.setOnItemClickListener(new ListListener());
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(rvlXml != null) outState.putSerializable("rvlXml", rvlXml); // TODO save parsed data, not source string
    }

}
