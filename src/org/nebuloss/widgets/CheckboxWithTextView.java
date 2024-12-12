package org.nebuloss.widgets;

import android.content.Context;
import android.graphics.Color;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckboxWithTextView extends LinearLayout {

    private CheckBox checkBox;
    private TextView textView;

    public CheckboxWithTextView(Context context,String text) {
        super(context);

        // Set orientation to horizontal
        setOrientation(HORIZONTAL);
        setPadding(20, 20, 20, 20);

        // Initialize the CheckBox
        checkBox = new CheckBox(context);
        
        LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        checkBoxParams.setMargins(0, 0, 20, 0); // Add margin to the right of the checkbox
        checkBox.setLayoutParams(checkBoxParams);

        // Add the CheckBox to the layout
        addView(checkBox);

        // Initialize the TextView
        textView = new TextView(context);
        textView.setText(text); // Default text
        textView.setTextSize(16); // Text size in sp
        textView.setTextColor(Color.BLACK); // Default text color

        // Add the TextView to the layout
        addView(textView);
    }

    public CheckBox getCheckBox(){
        return checkBox;
    }

    public TextView gTextView(){
        return textView;
    }

    
}
