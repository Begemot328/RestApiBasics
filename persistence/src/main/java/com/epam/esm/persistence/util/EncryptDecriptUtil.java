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

/**
 * Sensitive data encryptor-decryptor.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class EncryptDecriptUtil {

    private static final byte[] keyBytes = {1, 2, 3, 4, 5, 6, 7, 8};
    private static final byte[] ivBytes = {9, 10, 11, 12, 13, 14, 15, 16};
    private final Cipher deCipher;
    private final Cipher enCipher;
    private final SecretKeySpec key;
    private final IvParameterSpec ivSpec;

    /**
     * Default constructor.
     */
    public EncryptDecriptUtil()
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this(keyBytes, ivBytes);
    }

    /**
     * Constructor.
     *
     * @param keyBytes Byte array to create {@link DESKeySpec}.
     * @param ivBytes Byte array to create {@link IvParameterSpec}.
     */
    public EncryptDecriptUtil(byte[] keyBytes, byte[] ivBytes)
            throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        ivSpec = new IvParameterSpec(ivBytes);
            DESKeySpec dkey = new DESKeySpec(keyBytes);
            key = new SecretKeySpec(dkey.getKey(), "DES");
            deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    }

    /**
     * Encrypt method.
     *
     * @param obj {@link String} object encrypt.
     * @return Encrypted byte array.
     */
    public byte[] encrypt(String obj) throws InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        byte[] input = convertToByteArray(obj);
        enCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        return enCipher.doFinal(input);
    }

    /**
     * Decrypt method.
     *
     * @param encrypted Byte array to decrypt.
     * @return Decrypted {@link String} object.
     */
    public String decrypt(byte[] encrypted) throws InvalidKeyException, InvalidAlgorithmParameterException,
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

    /**
     * Decrypt to String method.
     *
     * @param input {@link String} to decrypt.
     * @return Decrypted {@link String} object.
     */
    public String decryptToString(String input) throws InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {
        return decrypt(convertToByteArray(input));
    }

    /**
     * Encrypt to String method.
     *
     * @param input {@link String} to encrypt.
     * @return Encrypted {@link String} object.
     */
    public String encryptToString(String input) throws InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        return convertFromByteArray(encrypt(input));
    }
}