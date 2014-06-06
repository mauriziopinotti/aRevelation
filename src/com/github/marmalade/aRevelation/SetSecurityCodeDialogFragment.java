package com.github.marmalade.aRevelation;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Author: <a href="mailto:alexey.kislin@gmail.com">Alexey Kislin</a>
 * Date: 6/1/14
 * Time: 11:26 PM
 */
public class SetSecurityCodeDialogFragment extends Fragment {

    public SetSecurityCodeDialogFragment() {}

    private String code;

    EditText editText;

    TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.set_security_code_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = (EditText) view.findViewById(R.id.security_code_text_edit);
        tv = (TextView) view.findViewById(R.id.security_code_text_view);
        editText.setOnEditorActionListener(listener);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {

        private boolean isFirst = true;

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (isFirst) {
                tv.setText(getResources().getString(R.string.repeat_sec));
                code = editText.getText().toString();
                editText.getText().clear();
                isFirst = false;
            } else {
                if(editText.getText().toString().equals(code)) {
                    SharedPreferences settings = getActivity().getSharedPreferences(((MainActivity)getActivity()).PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("securityCodeHash", code);
                    editor.commit();

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                    SetSecurityCodeDialogFragment.this.
                            getFragmentManager().
                            beginTransaction().
                            remove(SetSecurityCodeDialogFragment.this).
                            commit();
                } else {
                    editText.getText().clear();
                    tv.setText(getResources().getString(R.string.create_sec));
                    isFirst = true;
                }
                handled = true;
            }
            return handled;
        }
    };

}
