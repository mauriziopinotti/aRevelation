package com.github.marmaladesky;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.github.marmaladesky.data.RevelationData;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;

public class ARevelation extends Activity {

    DateFormat dateFormatter;

    RevelationData rvlData;
    String password;
    String currentFile;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.MEDIUM, getResources().getConfiguration().locale);
    }

    public void saveChanges(View view) throws Exception {

        Serializer serializer = new Persister();

        OutputStream stream = new OutputStream() {
            private StringBuilder string = new StringBuilder();
            @Override
            public void write(int i) throws IOException {
                this.string.append((char) i );
            }
            public String toString() {
                return this.string.toString();
            }
        };

        serializer.write(rvlData, stream);

        byte[] encrypted = Cryptographer.encrypt(stream.toString(), password);

        OutputStream fop = getContentResolver().openOutputStream(Uri.parse(currentFile));
        fop.write(encrypted);
        fop.close();
    }

}
