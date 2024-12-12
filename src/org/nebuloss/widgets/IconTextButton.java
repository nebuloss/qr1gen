package org.nebuloss.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconTextButton extends LinearLayout {

    private ImageView iconView;
    private TextView textView;
    private GradientDrawable backgroundDrawable;

    public IconTextButton(Context context, int iconResId, String text) {
        super(context);
        setGravity(Gravity.CENTER);

        // Set the orientation of the layout to horizontal
        setOrientation(HORIZONTAL);

        // Create a custom drawable for the background with padding
        backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setCornerRadius(10); // Rounded corners
        backgroundDrawable.setStroke(3, Color.GRAY);
        setBackground(backgroundDrawable);

        MarginLayoutParams params = new MarginLayoutParams(
            MarginLayoutParams.MATCH_PARENT,
            MarginLayoutParams.WRAP_CONTENT
        );
        params.setMargins(20,10,20,10);
        setLayoutParams(params);

        // Set internal padding
        setPadding(20, 10, 20, 10);

        // Create and configure the ImageView for the icon
        iconView = new ImageView(context);
        iconView.setImageResource(iconResId); // Set the provided icon resource
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        iconParams.setMargins(0, 0, 10, 0); // Add some margin to the right of the icon
        iconView.setLayoutParams(iconParams);
        addView(iconView); // Add the ImageView to the layout

        // Create and configure the TextView for the text
        textView = new TextView(context);
        textView.setText(text); // Set the provided text
        textView.setTextSize(16); // Set the text size
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(textParams);

        setEnabled(true);
        addView(textView); // Add the TextView to the layout
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        // Change background color based on enabled state
        if (enabled) {
            backgroundDrawable.setColor(Color.GRAY); // Enabled background color
            textView.setTextColor(Color.BLACK); // Enabled text color
            iconView.setAlpha(1.0f); // Enabled icon alpha
        } else {
            backgroundDrawable.setColor(Color.LTGRAY); // Disabled background color
            textView.setTextColor(Color.DKGRAY); // Disabled text color
            iconView.setAlpha(0.5f); // Disabled icon transparency
        }
    }

    // Optionally, you can provide setters to update the icon and text
    public void setIcon(int iconResId) {
        iconView.setImageResource(iconResId);
    }

    public void setText(String text) {
        textView.setText(text);
    }
}

