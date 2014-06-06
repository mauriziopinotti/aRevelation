package com.github.marmalade.aRevelation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Author: <a href="mailto:alexey.kislin@gmail.com">Alexey Kislin</a>
 * Date: 6/6/14
 * Time: 7:53 PM
 */
public class AskCodeDialogFragment extends AskPasswordDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.ask_code_dialog, null);
        builder.setView(v)
                .setPositiveButton("Submit", onClickListener)
                .setNegativeButton("Cancel", onClickListener);
        editText = (EditText) v.findViewById(R.id.inputPasswordEditText);
        return builder.create();
    }

}
