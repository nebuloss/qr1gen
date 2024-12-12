package org.nebuloss.testapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.nebuloss.utils.SharedPreferencesUtils;
import org.nebuloss.utils.StreamUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class SharedPreferencesHelper {

    private final String PREFS_NAME = "AppPrefs";
    private final String UUID_KEY= "uuid";
    private final String OLD_UUID_KEY= "old_uuid";
    private final String DEBUG_KEY= "debug";

    private SharedPreferences primarySharedPreferences;
    private SharedPreferences secondarySharedPreferences;

    private String provider;
    
    private String idKey;
    private String id;

    private String numberKey;
    private String number;
    
    private static SharedPreferencesHelper instance=new SharedPreferencesHelper();

    public static SharedPreferencesHelper getInstance(){
        return instance;
    }

    private SharedPreferencesHelper(){
        primarySharedPreferences=App.getGlobalContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String uuid=primarySharedPreferences.getString(UUID_KEY, null);

        if (uuid==null){
            uuid=generateUUID();
            primarySharedPreferences.edit().putString(UUID_KEY, uuid).apply();
        }
        loadSecondarySharedPreference(uuid);
        cleanOldPreferences();
    }

    private String generateUUID(){
        return UUID.randomUUID().toString();
    }

    private File getFileFromUUID(String uuid){
        return new File(String.format("%s/shared_prefs/%s_%s.xml",App.getGlobalContext().getApplicationInfo().dataDir,PREFS_NAME,uuid));
    }

    private void cleanOldPreferences(){
        Set<String> oldUUIDSet=primarySharedPreferences.getStringSet(OLD_UUID_KEY, new HashSet<String>());
        for (String uuid:oldUUIDSet){
            getFileFromUUID(uuid).delete();
        }   
    }

    private void loadSecondarySharedPreference(String uuid){
        secondarySharedPreferences=App.getGlobalContext().getSharedPreferences(PREFS_NAME+"_"+uuid, Context.MODE_PRIVATE);

        String providerKey=SharedPreferencesUtils.searchKeyDefault(secondarySharedPreferences,"provider");
        provider=secondarySharedPreferences.getString(providerKey, App.getGlobalContext().getPackageName());

        idKey=SharedPreferencesUtils.searchKeyDefault(secondarySharedPreferences,provider,"id");
        id=SharedPreferencesUtils.getEncryptedString(secondarySharedPreferences, idKey,"");

        numberKey=SharedPreferencesUtils.searchKeyDefault(secondarySharedPreferences,provider,"number");
        number=SharedPreferencesUtils.getEncryptedString(secondarySharedPreferences, numberKey,"");
    }

    
    public String getNumber() {
        return number;
    }

    public String getId() {
        return id;
    }

    public void setNumber(String number) {
        this.number = number;
        SharedPreferencesUtils.putEncryptedString(secondarySharedPreferences, numberKey, number);
    }

    public void setId(String id) {
        this.id = id;
        SharedPreferencesUtils.putEncryptedString(secondarySharedPreferences, idKey, id);
    }

    public void importSharedPreferences(Uri uri) throws IOException{
        InputStream inputStream = App.getGlobalContext().getContentResolver().openInputStream(uri);
        File outFile;
        String uuid;
        do{
            uuid=generateUUID();
            outFile=getFileFromUUID(uuid);
        }while(outFile.exists());

        FileOutputStream outputStream=new FileOutputStream(outFile);
        StreamUtils.copyStream(inputStream, outputStream);

        

        inputStream.close();
        outputStream.close();

        String oldUUID=primarySharedPreferences.getString(UUID_KEY,null);
        primarySharedPreferences.edit().putString(UUID_KEY, uuid).apply();

        if (oldUUID!=null){
            Set<String> oldUUIDSet=primarySharedPreferences.getStringSet(OLD_UUID_KEY, new HashSet<String>());
            oldUUIDSet.add(oldUUID);
            primarySharedPreferences.edit().putStringSet(OLD_UUID_KEY, oldUUIDSet).apply();
        }

        loadSecondarySharedPreference(uuid);
    }

    public void exportSharedPreferences(Uri uri) throws IOException{
        String uuid=primarySharedPreferences.getString(UUID_KEY,null);

        if (uuid!=null){
            File inputFile=getFileFromUUID(uuid);

            if (inputFile.exists()){
                FileInputStream inputStream = new FileInputStream(inputFile);
                OutputStream outputStream = App.getGlobalContext().getContentResolver().openOutputStream(uri);

                StreamUtils.copyStream(inputStream, outputStream);

                inputStream.close();
                outputStream.close();
            }
        }
    }

    public void setDebugMode(boolean mode){
        primarySharedPreferences.edit().putBoolean(DEBUG_KEY, mode).apply();
    }

    public boolean getDebugMode(){
        return primarySharedPreferences.getBoolean(DEBUG_KEY,false);
    }

}
