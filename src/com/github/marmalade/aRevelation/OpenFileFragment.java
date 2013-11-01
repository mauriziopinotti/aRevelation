/*
 * Copyright 2013 Aleksey Kislin
 * Copyright 2013 Michal Švirec
 *
 * This file is part of aRevelation.
 *
 * aRevelation is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aRevelation is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aRevelation.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.marmalade.aRevelation;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.marmalade.aRevelation.ui.FileActivity;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;

/**
 * Author: <a href="mailto:alexey.kislin@gmail.com">Alexey Kislin</a>
 * Date: 30.08.13
 * Time: 2:10
 */
public class OpenFileFragment extends ListFragment implements IBackPressedListener {

    private static final String DEFAULT_PATH = "/";

    private static final String PATH = "mPath";

    // Current mPath of a showed menu
    private String mPath;

    private ArrayList<FileWrapper> mFilesBrowserItems = new ArrayList<FileWrapper>();
    private ArrayAdapter<FileWrapper> mFilesBrowserAdapter;

    public static OpenFileFragment newInstance(String path) {
        OpenFileFragment fragment = new OpenFileFragment();

        Bundle bundle = new Bundle();
        bundle.putString(PATH, path);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                mPath = arguments.getString(PATH);
            }

            if (mPath == null) {
                mPath = DEFAULT_PATH;
            }

        } else {
            mPath = savedInstanceState.getString(PATH);
        }

        mFilesBrowserAdapter = new ArrayAdapter<FileWrapper>(this.getActivity(),
                android.R.layout.simple_list_item_1, mFilesBrowserItems);

        setListAdapter(mFilesBrowserAdapter);
        setLocation(new FileWrapper(mPath));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PATH, mPath);
    }

    private void setLocation(FileWrapper path) {
        this.mPath = path.getFile().getAbsolutePath();
        mFilesBrowserItems.clear();
        if (path.getFile().getParent() != null) {
            mFilesBrowserItems.add(new FileWrapper(path.getFile().getParentFile(), "..."));
        }
        File[] sortedChildren = path.getFile().listFiles();
        Arrays.sort(sortedChildren);
        for (File childFile : sortedChildren) {
            mFilesBrowserItems.add(new FileWrapper(childFile));
        }
        mFilesBrowserAdapter.notifyDataSetChanged();
//        lv.setSelection(0);         // Go to the top
    }


    private void openFile(final File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.open_file_title)
                .setMessage(getString(R.string.open_file_message, file.getName()))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), FileActivity.class);
                        intent.putExtra(FileActivity.PATH, file.getAbsolutePath());
                        startActivity(intent);

//                        askPassword(file);
                    }
                })
                .show();
    }

    private void askPassword(final File file) {
        final AskPasswordDialogFragment d = new AskPasswordDialogFragment();

        AskPasswordDialogFragment.AskPasswordOnClickListener dialogClickListener = d.new
                AskPasswordOnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                tryToOpenFile(file, d.editText.getEditableText().toString
                                        ());
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // Nothing to do
                                break;
                        }
                    }
                };

        d.setOnClickListener(dialogClickListener);
        d.show(getFragmentManager(), null);
    }


    private void tryToOpenFile(File file, String password) {
        try {
            RandomAccessFile f = new RandomAccessFile(file.getAbsoluteFile(), "r");
            byte[] fileData = new byte[(int) f.length()];
            f.read(fileData);
            String decryptedXML = Cryptographer.decrypt(fileData, password);

//            Intent intent = new Intent(getActivity().getApplicationContext(), FileActivity.class);
//            intent.putExtra(FileActivity.DECRYPTED_DATA, decryptedXML);
//            intent.putExtra(FileActivity.PASSWORD, password);
//            startActivity(intent);
        } catch (BadPaddingException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setTitle("Error")
                    .setMessage("Invalid password");
            builder.show();
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setTitle("Error")
                    .setMessage(e.getMessage());
            builder.show();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        FileWrapper clickedFile = mFilesBrowserItems.get(position);
        if (clickedFile.getFile().isDirectory() && clickedFile.getFile().canRead()) {
            setLocation(clickedFile);
        } else if (clickedFile.getFile().isFile() && clickedFile.getFile().canRead()) {
            openFile(clickedFile.getFile());
        }
    }

    private static class FileWrapper {

        private File file;

        private String name;

        private boolean isBackElement;

        FileWrapper(File file) {
            this.file = file;
            this.name = file.getName();
        }

        FileWrapper(String file) {
            this(new File(file));
        }

        FileWrapper(File file, String name) {
            this.file = file;
            this.name = name;
            isBackElement = true;
        }

        File getFile() {
            return file;
        }

        @Override
        public String toString() {
            return name;
        }

        public boolean isBackElement() {
            return isBackElement;
        }
    }


    @Override
    public void onBackPressed() {
        if (mFilesBrowserItems.get(0).isBackElement)
            setLocation(mFilesBrowserItems.get(0));
        else
            getFragmentManager().popBackStack();
    }
}
