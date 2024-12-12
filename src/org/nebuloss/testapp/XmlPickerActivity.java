package org.nebuloss.testapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.io.InputStream;

import org.nebuloss.utils.SharedPreferenceParser;

public class XmlPickerActivity extends Activity {

    private static final int REQUEST_CODE_OPEN_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openFilePicker();
    }

    // Method to open the file picker
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE); // Only show openable files
        intent.setType("*/*"); // Allow all file types
        startActivityForResult(intent, REQUEST_CODE_OPEN_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_OPEN_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData(); // Get the URI of the selected file
                parseSharedPreferencesFile(fileUri);
            }
        }
        finish();
    }

    private void parseSharedPreferencesFile(Uri fileUri) {
        try {
            // Open the input stream of the selected XML file
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            parseSharedPreferencesKeys(new SharedPreferenceParser(inputStream));
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, String.format("Failed to load xml file: %s", e.getMessage()), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseSharedPreferencesKeys(SharedPreferenceParser sharedPreferenceParser) throws Exception{
        String id=sharedPreferenceParser.getString("id");
        String number=sharedPreferenceParser.getString("number");

        SharedPreferencesHelper.getInstance().setId(id);
        SharedPreferencesHelper.getInstance().setNumber(number);
    }
}

