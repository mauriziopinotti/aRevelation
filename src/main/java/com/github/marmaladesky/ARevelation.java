package com.github.marmaladesky;

import android.app.Activity;
import android.os.Bundle;

import java.text.DateFormat;

public class ARevelation extends Activity {

    public static DateFormat dateFormatter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.MEDIUM, getResources().getConfiguration().locale);
    }

}
