package com.epam.esm.persistence;

import com.epam.esm.persistence.util.EncryptDecriptUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

public class Runner {
    public static void main(String[] args) throws InvalidAlgorithmParameterException,
            BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        EncryptDecriptUtil util = new EncryptDecriptUtil();
        System.out.println(util.encryptToString("1234567890"));
        System.out.println(util.decrypt(util.encrypt("1234567890")));
    }
}
