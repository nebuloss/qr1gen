package org.nebuloss.qr1gen;

import org.nebuloss.utils.InvalidFormatException;
import org.nebuloss.utils.QrCodeGenerator;
import org.nebuloss.widgets.IconTextButton;
import org.nebuloss.widgets.QrCodeGeneratorLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class QrCodeActivity extends Activity {

    private QrCodeGenerator generator;
    private QrCodeGeneratorLayout qrCodeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferencesHelper helper=SharedPreferencesHelper.getInstance();

        try{
            generator=new QrCodeGenerator(helper.getId(), helper.getNumber());
        }catch(InvalidFormatException e){
            startSettingsActivity();
            finish();
        }

        // Create a LinearLayout programmatically
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.LTGRAY);

        qrCodeLayout=new QrCodeGeneratorLayout(this, generator);
        layout.addView(qrCodeLayout);

        IconTextButton settingsButton = new IconTextButton(this, android.R.drawable.ic_menu_preferences, "Settings");
        layout.addView(settingsButton); // Add ImageView to layout

        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettingsActivity();
            }
        });

        IconTextButton exitButton = new IconTextButton(this, android.R.drawable.ic_lock_power_off, "Exit");
        layout.addView(exitButton); // Add ImageView to layout

        exitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
                finishAffinity();
            }
        });

        // Set the layout as the content view
        setContentView(layout);
    }

    @Override
    protected void onResume(){
        super.onResume();
        qrCodeLayout.setDebugMode(SharedPreferencesHelper.getInstance().getDebugMode());
    }

    private void startSettingsActivity(){
        Intent intent = new Intent(QrCodeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (qrCodeLayout!=null) qrCodeLayout.stopGeneration();
        super.onDestroy();
    }
}
