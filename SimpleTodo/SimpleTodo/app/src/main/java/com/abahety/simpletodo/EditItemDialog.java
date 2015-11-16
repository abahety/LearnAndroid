package com.abahety.simpletodo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;


public class EditItemDialog extends DialogFragment implements View.OnClickListener {
    private EditText etItem;
    private static String keyToPass = "text";
    private static String keyPos = "pos";


    public interface EditItemDialogListener{
        public void saveItem (String name, int pos);
    }

    EditItemDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (EditItemDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public EditItemDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditItemDialog newInstance(String textToEdit, int pos) {
        EditItemDialog frag = new EditItemDialog();
        Bundle args = new Bundle();
        args.putString(keyToPass, textToEdit);
        args.putInt(keyPos, pos);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.activity_edit_item_dialog, container);
        dialogView.findViewById(R.id.btnSave).setOnClickListener(this);
        //return inflater.inflate(R.layout.activity_edit_item_dialog, container);
        return dialogView;
}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etItem = (EditText) view.findViewById(R.id.etItem);
        // Fetch arguments from bundle and set text
        String textToEdit = getArguments().getString(keyToPass, "");
        etItem.setText(textToEdit);
        // Show soft keyboard automatically and request focus to field
        etItem.requestFocus();
        getDialog().getWindow().setSoftInputMode(
               WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onClick(View v) {
        String itemName = etItem.getText().toString();
        int pos = getArguments().getInt(keyPos);

        mListener.saveItem(itemName,pos);
        dismiss();
    }
}
