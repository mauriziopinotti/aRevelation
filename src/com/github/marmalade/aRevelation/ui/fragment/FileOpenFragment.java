package com.github.marmalade.aRevelation.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;

import com.github.marmalade.aRevelation.Cryptographer;
import com.github.marmalade.aRevelation.FileEntriesFragment.Entry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.MathContext;
import java.util.List;

import javax.crypto.BadPaddingException;

/**
 * Created by sviro on 11/8/13.
 */
public class FileOpenFragment extends Fragment {

    public static FileOpenFragment newInstance() {
        return new FileOpenFragment();
    }

    public static interface OnReadFileListener {
        public void onFileRead(List<Entry> entries);
        public void onFileReadFail();
    }

    private ReadFileTask mTask;
    private OnReadFileListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mTask = new ReadFileTask(getActivity().getApplicationContext());
    }

    public void readFile(final Uri uri, final String password) {
        if (mTask == null || mTask.getStatus() == Status.FINISHED) {
            mTask = new ReadFileTask(getActivity().getApplicationContext());
        }

        if (mTask.getStatus() != Status.RUNNING) {
            mTask.execute(new FileEntry(uri, password));
            return;
        }

        if (mListener != null) {
            mListener.onFileReadFail();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnReadFileListener) activity;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Activity " + activity + " must implement OnReadFileListener", e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    private static List<Entry> tryToOpenFile(Context context, FileEntry fileEntry) throws Exception {
        InputStream inputStream = null;
        ByteArrayOutputStream bos = null;
        try {
            inputStream = context.getContentResolver().openInputStream(fileEntry.getUri());
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            String decryptedXML = Cryptographer.decrypt(bos.toByteArray(), fileEntry.getPassword());
            return Entry.parseDecryptedXml(decryptedXML);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    //do nothing
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
        }
    }

    private class ReadFileTask extends AsyncTask<FileEntry, Void, List<Entry>> {

        private final Context mContext;

        private ReadFileTask(Context context) {
            mContext = context;
        }

        @Override
        protected List<Entry> doInBackground(FileEntry... params) {
            FileEntry fileEntry = params[0];
            if (fileEntry != null) {
                try {
                    return tryToOpenFile(mContext, fileEntry);
                } catch (Exception e) {
                    //TODO handle
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Entry> entries) {
            if (mListener != null) {
                if (entries != null) {
                    mListener.onFileRead(entries);
                } else {
                    mListener.onFileReadFail();
                }

            }
        }
    }

    private static class FileEntry {

        private Uri uri;
        private String password;
        private int error;

        private FileEntry(Uri uri, String password) {
            this.uri = uri;
            this.password = password;
        }

        public Uri getUri() {
            return uri;
        }

        public String getPassword() {
            return password;
        }
    }
}
