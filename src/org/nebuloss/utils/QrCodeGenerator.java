package org.nebuloss.utils;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.nebuloss.models.IStringGenerator;

public class QrCodeGenerator implements IStringGenerator{

    private String id;
    private String number;

    private final static String QR_VERSION="QR1";

    public static void checkId(String id) throws InvalidFormatException {
        if (id==null) throw new InvalidFormatException("ID is null");
        if (id.isEmpty()) throw new InvalidFormatException("ID is empty");
        if (!id.matches("\\d+")) throw new InvalidFormatException("ID must contain only numeric characters.");
    }
    
    public static void checkNumber(String number) throws InvalidFormatException {
        if (number==null) throw new InvalidFormatException("Number is null");
        if (number.isEmpty()) throw new InvalidFormatException("Number is empty");
        if (!number.matches("[0-9a-f]+")) throw new InvalidFormatException("Number must contain only lowercase hexadecimal characters.");
    }

    public QrCodeGenerator(String id,String number) throws InvalidFormatException{
        checkNumber(number);
        checkId(id);

        this.id=id;
        this.number=number;
    }

    public String generate() throws Exception{
        Charset charset = Charset.forName("UTF-8");
        
        long t=getTimeStamp();

        SecretKeySpec secretKeySpec = new SecretKeySpec(number.getBytes(charset), "HmacSHA256");

        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        hmacSHA256.init(secretKeySpec);
  
        String dataToSign = QR_VERSION + "+" + t + "+" + id;
        byte[] signedData = hmacSHA256.doFinal(dataToSign.getBytes(charset));
        
        String sig = HexUtils.printHex(signedData).subSequence(0, 6).toString().toLowerCase(Locale.ROOT);
       
        return String.format("{\"id\":\"%s\",\"sg\":\"%s\",\"t\":\"%d\",\"v\":\"%s\"}",
            id,
            sig,
            t,
            QR_VERSION
        );
    }

    private static long getTimeStamp(){
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }
}