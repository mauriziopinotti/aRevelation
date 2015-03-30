package com.github.marmaladesky;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.github.marmaladesky.data.FieldWrapper;

public class EditFieldDialog extends DialogFragment {

    private EditText value;
    private FieldWrapper field;
    private FeedbackListener listener;

    public static EditFieldDialog newInstance(String fieldUuid, FeedbackListener listener) {
        EditFieldDialog d = new EditFieldDialog();
        Bundle args = new Bundle();
        args.putString("fieldUuid", fieldUuid);
        args.putSerializable("listener", listener);
        d.setArguments(args);
        return d;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            if (savedInstanceState == null && getArguments() != null) {
                field = ((ARevelation) getActivity()).rvlData.getFieldById(getArguments().getString("fieldUuid"));
                listener = (FeedbackListener) getArguments().getSerializable("listener");
            } else if (savedInstanceState != null) {
                field = ((ARevelation) getActivity()).rvlData.getFieldById(savedInstanceState.getString("fieldUuid"));
                listener = (FeedbackListener) savedInstanceState.getSerializable("listener");
            } else {
                throw new IllegalArgumentException("Need saved state.");
            }
        } catch(Exception e) {
            throw new IllegalArgumentException("Need saved state.", e);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.edit_field_dialog, null);
        builder.setView(v);
        value = (EditText) v.findViewById(R.id.edit_field_value);
        value.setText(field.getFieldValue());

        builder
                .setPositiveButton(R.string.open,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    field.setFieldValue(value.getText().toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener.doSomething();
    }
}
