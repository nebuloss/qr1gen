package org.nebuloss.utils;

public class HexUtils {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String printHex(byte[] input) {
        char[] result = new char[input.length * 2];
        int inputLenght = input.length;
        for (int i = 0; i < inputLenght; i++) {
            int value = input[i] & 0xFF;
            result[i<<1] = HEX_ARRAY[value >>> 4];
            result[(i<<1) + 1] = HEX_ARRAY[value & 15];
        }
        return new String(result);
    }

    public static byte[] parseHex(String input){
        int inputLength = input.length();
        byte[] output = new byte[inputLength>>1];
        for (int i = 0; i < inputLength; i += 2) {
            output[i >>1] = (byte) (Character.digit(input.charAt(i + 1), 16) + (Character.digit(input.charAt(i), 16) << 4));
        }
        return output;
    }
}
