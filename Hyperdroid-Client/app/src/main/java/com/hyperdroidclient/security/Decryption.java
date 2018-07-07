package com.hyperdroidclient.security;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by archish on 27/11/17.
 */

public class Decryption {

    public static String decryptPassword(String hash) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String username = "admin@gmail.com";
        String password = "admin@123";
        String SALT2 = "deliciously salty";

        Cipher cipher = Cipher.getInstance("AES");
        byte[] key = Base64.encode((SALT2 + username + password).getBytes(),Base64.DEFAULT);
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        String secKey = Base64.encodeToString(secretKeySpec.getEncoded(),Base64.DEFAULT);
        Log.d("Client Key :",secKey);
        byte[] decordedValue = Base64.decode(hash.getBytes(),Base64.DEFAULT);
        byte[] original = cipher.doFinal(decordedValue);
        return new String(original);
    }


}
