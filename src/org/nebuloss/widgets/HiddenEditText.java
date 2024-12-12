package org.nebuloss.widgets;

import android.content.Context;
import android.text.method.PasswordTransformationMethod;

public class HiddenEditText extends ProtectedEditText{
    public HiddenEditText(Context context){
        super(context);
    }

    @Override
    public void triggerEditMode(){
        getEditText().setTransformationMethod(null);
        super.triggerEditMode();
    }

    @Override
    public void triggerSaveMode(){
        super.triggerSaveMode();
        getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
}
