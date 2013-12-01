/*
 * Copyright 2013 Aleksey Kislin
 * Copyright 2013 Michal Švirec
 *
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
package com.github.marmalade.aRevelation.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.github.marmalade.aRevelation.R;

/**
 * Author: <a href="mailto:alexey.kislin@gmail.com">Alexey Kislin</a>
 * Date: 10/1/13
 * Time: 8:21 PM
 */
public class AskPasswordDialogFragment extends DialogFragment implements OnClickListener {

    private OnPasswordSubmitListener mListener;

    public static interface OnPasswordSubmitListener {
        public void onPasswordSubmit(String password);

        public void onPasswordCancel();
    }

    EditText mPasswordEditText;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnPasswordSubmitListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Parent fragment must implement " +
                    "OnPasswordSubmitListener", e);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.input_password_title);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.ask_password_dialog, null);
        builder.setView(v)
                .setPositiveButton(R.string.submit, this)
                .setNegativeButton(android.R.string.cancel, this);
        mPasswordEditText = (EditText) v.findViewById(R.id.inputPasswordEditText);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mListener != null) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    mListener.onPasswordSubmit(mPasswordEditText.getText().toString());
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    mListener.onPasswordCancel();
                    break;
            }

        }
    }

}
