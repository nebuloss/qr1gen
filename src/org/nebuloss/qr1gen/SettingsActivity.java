package org.nebuloss.qr1gen;

import org.nebuloss.utils.QrCodeGenerator;
import org.nebuloss.widgets.CheckboxWithTextView;
import org.nebuloss.widgets.HiddenEditText;
import org.nebuloss.widgets.IconTextButton;
import org.nebuloss.widgets.ProtectedEditText;
import org.nebuloss.widgets.factory.EditTextValidator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

    private ProtectedEditText idEditor;
    private HiddenEditText numberEditor;
    private CheckboxWithTextView debugView;

    private IconTextButton generateButton;
    @SuppressWarnings("unused")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a LinearLayout programmatically
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.LTGRAY);

        TextView idText = new TextView(this);
        idText.setPadding(20, 20, 20, 20); // Add padding for spacing
        idText.setText("ID");
        layout.addView(idText);

        idEditor = new ProtectedEditText(this);
        new EditTextValidator(idEditor, QrCodeGenerator::checkId);

        idEditor.setOnClickListener(v->{
            if (!idEditor.isEditable()){
                updateGenerateButton();
                SharedPreferencesHelper.getInstance().setId(idEditor.getText());
            }
        });

        layout.addView(idEditor); // Add to a layout (like LinearLayout)


        TextView numberText = new TextView(this);
        numberText.setPadding(20, 20, 20, 20); // Add padding for spacing
        numberText.setText("Number");
        layout.addView(numberText);

        numberEditor = new HiddenEditText(this);
        new EditTextValidator(numberEditor, QrCodeGenerator::checkNumber);

        numberEditor.setOnClickListener(v->{
            if (!numberEditor.isEditable()){
                updateGenerateButton();
                SharedPreferencesHelper.getInstance().setNumber(numberEditor.getText());
            }
        });

        layout.addView(numberEditor); // Add to a layout (like LinearLayout)
        
        generateButton=new IconTextButton(this, android.R.drawable.ic_media_play, "Start generation");
        
        generateButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, QrCodeActivity.class);
            startActivity(intent);
        });

        layout.addView(generateButton);

        IconTextButton importButton=new IconTextButton(this, android.R.drawable.stat_sys_upload, "Import");
        
        importButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ImportActivity.class);
            startActivity(intent);
        });

        layout.addView(importButton);

        IconTextButton exportButton=new IconTextButton(this, android.R.drawable.stat_sys_download, "Export");
        
        exportButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ExportActivity.class);
            startActivity(intent);
        });

        layout.addView(exportButton);

        debugView=new CheckboxWithTextView(this, "debug mode");
        layout.addView(debugView);

        debugView.getCheckBox().setOnClickListener(v->{
            SharedPreferencesHelper.getInstance().setDebugMode(debugView.getCheckBox().isChecked());
        });

        updadateInformations();
        // Set the layout as the content view
        setContentView(layout);
    }

    @Override
    protected void onResume(){
        super.onResume();
        updadateInformations();
    }

    private void updadateInformations(){
        idEditor.setText(SharedPreferencesHelper.getInstance().getId());
        numberEditor.setText(SharedPreferencesHelper.getInstance().getNumber());
        debugView.getCheckBox().setChecked(SharedPreferencesHelper.getInstance().getDebugMode());
        
        updateGenerateButton();
    }


    private void updateGenerateButton(){
        try{
            QrCodeGenerator.checkId(idEditor.getText());
            QrCodeGenerator.checkNumber(numberEditor.getText());
            generateButton.setEnabled(true);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            generateButton.setEnabled(false);
        }
    }

}
