package com.epam.esm.persistence.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptDecriptUtil {

    private Cipher deCipher;
    private Cipher enCipher;
    private SecretKeySpec key;
    private IvParameterSpec ivSpec;
    private static byte[] keyBytes = {1, 2, 3, 4, 5, 6, 7, 8};
    private static byte[] ivBytes = {9, 10, 11, 12, 13, 14, 15, 16};

    public EncryptDecriptUtil() {
        this(keyBytes, ivBytes);
    }

    public EncryptDecriptUtil(byte[] keyBytes,   byte[] ivBytes) {
        ivSpec = new IvParameterSpec(ivBytes);
        try {
            DESKeySpec dkey = new  DESKeySpec(keyBytes);
            key = new SecretKeySpec(dkey.getKey(), "DES");
            deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public byte[] encrypt(String obj) throws InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        byte[] input = convertToByteArray(obj);
        enCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        return enCipher.doFinal(input);
    }

    public String decrypt(byte[]  encrypted) throws InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        deCipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        return convertFromByteArray(deCipher.doFinal(encrypted));
    }

    private String convertFromByteArray(byte[] byteObject) {
       return new String(Base64.getEncoder().encode(byteObject), StandardCharsets.US_ASCII);
    }

    private byte[] convertToByteArray(String complexObject) {
        return Base64.getDecoder().decode(complexObject.getBytes(StandardCharsets.US_ASCII));
    }

    public String decryptToString(String input) throws InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {
        return decrypt(convertToByteArray(input));
    }

    public String encryptToString(String input) throws InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        return convertFromByteArray(encrypt(input));
    }
}