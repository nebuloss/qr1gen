package org.nebuloss.utils;

import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    public static String getEncryptedString(SharedPreferences preferences,String key,String defaultValue){
        String result=defaultValue;
        String encryptedValue=preferences.getString(key,null);
        if (encryptedValue!=null){
            try{
                result=SharedPreferencesEncryption.getInstance().decode(encryptedValue);
            }catch(Exception e){}
        }
        return result;
    }

    public static String getEncryptedString(SharedPreferences preferences,String key){
        return getEncryptedString(preferences, key,null);
    }

    public static boolean putEncryptedString(SharedPreferences preferences,String key,String value){
        boolean status;
        try{
            String encryptedValue=SharedPreferencesEncryption.getInstance().encode(value);
            preferences.edit().putString(key, encryptedValue).apply();
            status=true;
        }catch(Exception e){
            status=false;
        }
        return status;
    }


    public static String searchKey(SharedPreferences preferences,String ...keywords){
        String result=null;

        for (String key:preferences.getAll().keySet()){
            result=key;
            for (String keyword:keywords){
                if (!key.contains(keyword)){
                    result=null;
                    break;
                }
            }
            if (result!=null) break;
        }

        return result;
    }
    
    public static String searchKeyDefault(SharedPreferences preferences,String ...keywords){
        String result=searchKey(preferences, keywords);
        if (result==null) result=String.join(".",keywords);
        return result;
    }
}
