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

    private char[] SECRET = {0x53, 0x68, 0x61, 0x69, 0x32, 0x41, 0x71, 0x75, 0x65, 0x69};
    private byte[] SALT = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03};
    

    private SharedPreferencesEncryption() throws Exception{
        secretKey=SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(SECRET));
        cipher = Cipher.getInstance(secretKey.getAlgorithm());
        algorithmParameterSpec = new PBEParameterSpec(SALT, 0x13);
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