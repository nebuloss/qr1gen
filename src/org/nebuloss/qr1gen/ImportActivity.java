package org.nebuloss.qr1gen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class ImportActivity extends Activity {

    private static final int PICK_FILE_REQUEST_CODE = 2;  // Request code for file picker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Trigger file picker to select the file for import
        openFilePicker();
    }

    private void openFilePicker() {
        // Create an intent to open a file picker
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/xml");
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    try{
                        SharedPreferencesHelper.getInstance().importSharedPreferences(uri);
                    }catch (Exception e){
                        Toast.makeText(this, "Failed to import config: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    
                }
            }
        }
        finish();
    }
}
