package org.nebuloss.utils;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class SharedPreferencesEncryption{

    private Cipher cipher;
    private SecretKey secretKey;
    private AlgorithmParameterSpec algorithmParameterSpec;

    private static SharedPreferencesEncryption instance=null;

    private byte[] SECRET={83, 104, 97, 105, 50, 65, 113, 117, 101, 105};
    private String SALT="A99BC8325634E303";
    

    private SharedPreferencesEncryption() throws Exception{
        secretKey=SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(new String(SECRET).toCharArray()));
        cipher = Cipher.getInstance(secretKey.getAlgorithm());
        algorithmParameterSpec = new PBEParameterSpec(HexUtils.parseHex(SALT), 0x13);
    }

    public static SharedPreferencesEncryption getInstance() throws Exception{
        if (instance==null){
            instance=new SharedPreferencesEncryption();
        }
        return instance;
    }

    public String encode(String input) throws Exception{
        cipher.init(Cipher.ENCRYPT_MODE, secretKey,algorithmParameterSpec);
        byte[] resultBytes=cipher.doFinal(input.getBytes());
        return HexUtils.printHex(resultBytes);
    }

    public String decode(String input) throws Exception{
        cipher.init(Cipher.DECRYPT_MODE, secretKey,algorithmParameterSpec);
        byte[] resultBytes=cipher.doFinal(HexUtils.parseHex(input));
        return new String(resultBytes);
    }
}