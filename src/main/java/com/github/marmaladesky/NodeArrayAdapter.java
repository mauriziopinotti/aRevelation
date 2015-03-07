package com.github.marmaladesky;


import java.util.List;

import com.github.marmaladesky.data.Entry;

import android.content.Context;
import android.widget.ArrayAdapter;

public class NodeArrayAdapter extends ArrayAdapter<Entry> {

	public NodeArrayAdapter(Context context, int resource, List<Entry> objects) { super(context, resource, objects); }
	
}
