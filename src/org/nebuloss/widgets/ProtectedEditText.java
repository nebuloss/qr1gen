package org.nebuloss.widgets;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProtectedEditText extends LinearLayout{

    private EditText editText;
    private ImageView editSaveIcon;
    private boolean editStatus = false;
    private View.OnClickListener listener=null;;

    public ProtectedEditText(Context context) {
        super(context);

        setOrientation(HORIZONTAL);
        setPadding(20, 20, 20, 20);
        setBackgroundColor(Color.LTGRAY);

        // Create EditText
        editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setBackgroundColor(Color.WHITE);
        

        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        editText.setLayoutParams(editParams);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    triggerSaveMode();
                    return true; // Consume the action
                }
                return false;
            }
        });

        addView(editText); // Add EditText to layout

        // Create ImageView for the "Edit/Save" button
        editSaveIcon = new ImageView(context);
        editSaveIcon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        addView(editSaveIcon); // Add ImageView to layout

        triggerSaveMode();

        // Handle the button click to toggle between Edit and Save
        editSaveIcon.setOnClickListener(v->{
            editStatus = !editStatus;
            if (editStatus){
                triggerEditMode();
            }else {
                triggerSaveMode();
            }
            if (listener!=null) listener.onClick(v); 
        });

    }

    public EditText getEditText(){
        return editText;
    }

    public boolean isEditable(){
        return editStatus;
    }

    public String getText(){
        return editText.getText().toString();
    }

    public void setText(String value){
        editText.setText(value);
    }

    // Helper method to show the keyboard
    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    // Method to trigger edit mode programmatically
    public void triggerEditMode() {
        editText.setEnabled(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus(); // Set focus to EditText
        showKeyboard(editText); // Show the keyboard
        // Place the cursor at the end of the text
        editText.setSelection(editText.getText().length());

        editSaveIcon.setImageResource(android.R.drawable.ic_menu_save); // Change to Save icon
        editStatus = true;
    }

    public void triggerSaveMode() {
        // Save logic
        editText.setEnabled(false);
        editText.setFocusable(false);

        editSaveIcon.setImageResource(android.R.drawable.ic_menu_edit); // Change to Edit icon
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
}
