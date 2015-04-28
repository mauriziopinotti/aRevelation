package com.github.marmaladesky;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.github.marmaladesky.data.RevelationData;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;

public class ARevelation extends Activity {

    private static final String ARGUMENT_RVLDATA = "rvlData";
    private static final String ARGUMENT_PASSWORD = "password";
    private static final String ARGUMENT_FILE = "file";

    DateFormat dateFormatter;

    RevelationData rvlData;
    String password;
    String currentFile;

    Button saveButton;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        if(savedInstanceState != null) {
            rvlData = (RevelationData) savedInstanceState.getSerializable(ARGUMENT_RVLDATA);
            password = savedInstanceState.getString(ARGUMENT_PASSWORD);
            currentFile = savedInstanceState.getString(ARGUMENT_FILE);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Only at the startup
        if(getFragmentManager().getBackStackEntryCount() < 1) {
            StartScreenFragment newFragment = new StartScreenFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.mainContainer, newFragment)
                    .commit();
        }

        dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.MEDIUM, getResources().getConfiguration().locale);
        saveButton = (Button)this.findViewById(R.id.saveButton);
    }

    public void saveChanges(View view) throws Exception {
        rvlData.save(currentFile, password, getContentResolver());
        checkButton();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARGUMENT_RVLDATA, rvlData);
        outState.putString(ARGUMENT_PASSWORD, password);
        outState.putString(ARGUMENT_FILE, currentFile);
    }

    public void checkButton() {
        if (rvlData != null && rvlData.isEdited())
            saveButton.setVisibility(View.VISIBLE);
        else
            saveButton.setVisibility(View.GONE);
    }

}
