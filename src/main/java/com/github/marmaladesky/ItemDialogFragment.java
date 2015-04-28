package com.github.marmaladesky;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import com.github.marmaladesky.data.FieldWrapper;

public class ItemDialogFragment extends DialogFragment {


    private static final String HEADER_KEY = "header";
    private static final String PASSWORD_KEY = "password";
    private static final String FIELD_KEY = "field";
    private static final String LISTENER_KEY = "listener";

    public String header;
    public String password;
    public FieldWrapper field;
    private FeedbackListener listener;

    public static ItemDialogFragment newInstance(String header, String password, String fieldUuid, FeedbackListener listener) {
        ItemDialogFragment f = new ItemDialogFragment();
        Bundle args = new Bundle();
        args.putString(HEADER_KEY, header);
        args.putString(PASSWORD_KEY, password);
        args.putString(FIELD_KEY, fieldUuid);
        args.putSerializable(LISTENER_KEY, listener);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            if (savedInstanceState == null && getArguments() != null) {
                header = getArguments().getString(HEADER_KEY);
                password = getArguments().getString(PASSWORD_KEY);
                field = ((ARevelation) getActivity()).rvlData.getFieldById(getArguments().getString(FIELD_KEY));
                listener = (FeedbackListener) getArguments().getSerializable(LISTENER_KEY);
            } else if (savedInstanceState != null) {
                header = savedInstanceState.getString(HEADER_KEY);
                password = savedInstanceState.getString(PASSWORD_KEY);
                field = ((ARevelation) getActivity()).rvlData.getFieldById(savedInstanceState.getString(FIELD_KEY));
                listener = (FeedbackListener) savedInstanceState.getSerializable(LISTENER_KEY);
            } else {
                throw new IllegalArgumentException("Need saved state.");
            }
        } catch(Exception e) {
            throw new IllegalArgumentException("Need saved state.", e);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(new String[]{"Copy", "Edit"}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("pass", password);
                    clipboard.setPrimaryClip(clip);
                } else if (which == 1) {
                    try {
                        EditFieldDialog.newInstance(field.getUuid(), listener).show(getFragmentManager(), "ItemDialogFragment");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(HEADER_KEY, header);
        outState.putString(PASSWORD_KEY, password);
        outState.putSerializable(FIELD_KEY, field);
        outState.putSerializable(LISTENER_KEY, listener);

    }
}
