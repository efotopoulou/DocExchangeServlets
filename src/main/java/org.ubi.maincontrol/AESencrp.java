package org.ubi.maincontrol;

import java.lang.String;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.*;
import sun.misc.*;

public class AESencrp {

    private static final String ALGO = "AES";

    public static String encrypt(String Data, String OTP) throws Exception {
        Key key = generateKey(OTP);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    public static String decrypt(String encryptedData, String OTP) throws Exception {
        Key key = generateKey(OTP);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
    private static Key generateKey(String OTP) throws Exception {
        Key key = new SecretKeySpec(OTP.getBytes(), ALGO);
        return key;
    }

}


