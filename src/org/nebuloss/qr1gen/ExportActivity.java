package org.nebuloss.qr1gen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class ExportActivity extends Activity {

    private static final int PICK_FILE_REQUEST_CODE = 1;  // Request code for file picker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Trigger file picker
        openFilePicker();
    }

    private void openFilePicker() {
        // Create an intent to open a file picker
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/xml");
        intent.putExtra(Intent.EXTRA_TITLE, "backup.xml");
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
                        SharedPreferencesHelper.getInstance().exportSharedPreferences(uri);;
                    }catch (Exception e){
                        Toast.makeText(this, "Failed to export config: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        finish();
    }
}
