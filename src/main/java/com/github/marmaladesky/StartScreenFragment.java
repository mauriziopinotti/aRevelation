package com.github.marmaladesky;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.github.marmaladesky.data.RevelationData;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class StartScreenFragment extends Fragment {

	static final int REQUEST_FILE_OPEN = 1;

	@TargetApi(19)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)  {
		View v = inflater.inflate(R.layout.start_screen, container, false);

        Button btnOpen = (Button) v.findViewById(R.id.btnOpen);
        Button btnOption = (Button) v.findViewById(R.id.btnOption);

		btnOpen.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String action = Build.VERSION.SDK_INT >= 19 ? Intent.ACTION_OPEN_DOCUMENT : Intent.ACTION_GET_CONTENT;
				Intent intent = new Intent(action);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("application/*");
				startActivityForResult(intent, REQUEST_FILE_OPEN);

			}
		});
        btnOption.setOnClickListener(new OptionButtonListener());

        ((ARevelation) getActivity()).checkButton();

		return v;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_FILE_OPEN && data != null) {
			System.out.println("Image openned, result code is " + resultCode
					+ ", file is " + data.getData());
			try {
				(AskPasswordDialog.newInstance(data.getData().toString())).show(getFragmentManager(), "Tag");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static class AskPasswordDialog extends DialogFragment {

        public String file;

        public static AskPasswordDialog newInstance(String file) {
            AskPasswordDialog f = new AskPasswordDialog();
            Bundle args = new Bundle();
            args.putString("file", file);
            f.setArguments(args);
            return f;
        }

		@SuppressLint("InflateParams") // Passing null is normal for dialogs
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
            if(savedInstanceState != null && savedInstanceState.getString("file") != null)
                file = savedInstanceState.getString("file");
            else
                file = getArguments().getString("file");

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater inflater = getActivity().getLayoutInflater();

			builder.setView(inflater.inflate(R.layout.ask_password_dialog, null));
			builder
				.setPositiveButton(R.string.open,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) { /* See onStart() */ }
					})
							
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						}
				});
            return builder.create();
		}

		@Override
		public void onStart() {
			super.onStart();
			AlertDialog d = (AlertDialog)getDialog();
			if(d != null) {
				Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
			    positiveButton.setOnClickListener(new View.OnClickListener() {
			    	public void onClick(View v) {
			    		Boolean wantToCloseDialog = false;
			            EditText password = (EditText) getDialog().findViewById(R.id.password);
									
						String result;

						try {
							InputStream iStream = getActivity().getContentResolver().openInputStream(Uri.parse(file));
							byte[] inputData = getBytes(iStream);
							result = Cryptographer.decrypt(inputData, password.getText().toString());

							try {
								Serializer serializer = new Persister();
								((ARevelation)getActivity()).rvlData = serializer.read(RevelationData.class, result, false);
							} catch (Exception e) {
								e.printStackTrace();
							}
							RevelationBrowserFragment nextFrag = RevelationBrowserFragment.newInstance(((ARevelation)getActivity()).rvlData.getUuid());
							((ARevelation)getActivity()).password = password.getText().toString();
							((ARevelation)getActivity()).currentFile = file;

							getActivity().getFragmentManager().beginTransaction()
							.replace(R.id.mainContainer, nextFrag)
							.addToBackStack(null).commit();
							wantToCloseDialog = true;
						}
						catch(Exception e) {
							System.out.println(e.getMessage());
							TextView t = (TextView) getDialog().findViewById(R.id.message);
							t.setText(e.getMessage());
						}
						
						if(wantToCloseDialog)
							dismiss(); //else dialog stays open
						}
			    });
			}
		}

        private byte[] getBytes(InputStream inputStream) throws IOException {
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString("file", file);
        }
    }

    private static class OptionButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }
}
