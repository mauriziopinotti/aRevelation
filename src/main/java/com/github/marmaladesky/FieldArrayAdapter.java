package com.github.marmaladesky;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.github.marmaladesky.data.Field;

import java.util.List;

public class FieldArrayAdapter extends ArrayAdapter<Field> {

    public FieldArrayAdapter(Context context, int resource, List<Field> objects) { super(context, resource, objects); }

}
