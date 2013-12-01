package com.github.marmalade.aRevelation.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;

import com.github.marmalade.aRevelation.R;

/**
 * Created by sviro on 11/12/13.
 */
public class ErrorDialogFragment extends DialogFragment {

    public static interface OnErrorDialogCloseListener {
        public void onErrorDialogClose();
    }

    private static final String ERROR_MSG = "error_msg";
    private String mError;
    private OnErrorDialogCloseListener mListener;

    public static ErrorDialogFragment newInstance(String error) {
        ErrorDialogFragment fragment = new ErrorDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ERROR_MSG, error);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnErrorDialogCloseListener) activity;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Activity " + activity + " must implement OnErrorDialogCloseListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mError = arguments.getString(ERROR_MSG);
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Builder builder = new Builder(getActivity());
        builder.setTitle(R.string.error_title).setNeutralButton(android.R.string.ok, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    mListener.onErrorDialogClose();
                }
            }
        });
        if (!TextUtils.isEmpty(mError)) {
            builder.setMessage(mError);
        } else {
            builder.setMessage(R.string.unexpected_error);
        }

        return builder.create();
    }
}
