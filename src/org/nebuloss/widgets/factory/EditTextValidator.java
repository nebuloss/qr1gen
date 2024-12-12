package org.nebuloss.widgets.factory;

import java.util.function.Consumer;

import org.nebuloss.widgets.ProtectedEditText;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class EditTextValidator {
    private EditText editText;
    private Consumer<String> validator;


    public EditTextValidator(EditText editText,Consumer<String> validator){
        this.editText=editText;
        this.validator=validator;

        validateText();

        // Set text change listener for edit mode
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                validateText();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    public EditTextValidator(ProtectedEditText protectedEditText,Consumer<String> validator){
        this(protectedEditText.getEditText(),validator);
    }

    private void validateText(){
        String txt=editText.getText().toString();
        try{
            validator.accept(txt);
            editText.setBackgroundColor(Color.GREEN);
        }catch(Exception e){
            editText.setBackgroundColor(Color.RED);
        }
    }
}
