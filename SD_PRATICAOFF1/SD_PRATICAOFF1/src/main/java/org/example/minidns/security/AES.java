package org.example.minidns.security;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class AES {
    private KeyGenerator keyGenerator;
    private SecretKey key;
    private IvParameterSpec vi;
    private String msg;
    private String encryptedMsg;

    public AES(int keySize) throws NoSuchAlgorithmException {
        generateKey(keySize);
    }

    public void generateKey(int t) throws NoSuchAlgorithmException {
        this.keyGenerator = KeyGenerator.getInstance("AES");
        this.keyGenerator.init(t);
        this.key = keyGenerator.generateKey();
        System.out.println(Arrays.toString(key.getEncoded()));
    }

    public static IvParameterSpec generateVI(){
        byte[] vi = new byte[16];
        new SecureRandom().nextBytes(vi);
        return new IvParameterSpec(vi);
    }

    public String encrypt(String openMsg) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptMsgBytes;
        Cipher encryptor;

        this.msg = openMsg;

        encryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");

        this.vi = generateVI();

        encryptor.init(Cipher.ENCRYPT_MODE, this.key, this.vi);

        encryptMsgBytes = encryptor.doFinal(this.msg.getBytes());

        encryptedMsg = encoder(encryptMsgBytes);

        return  encryptedMsg;
    }

    private String encoder(byte[] encryptMsgBytes) {
        return Base64
                .getEncoder()
                .encodeToString(encryptMsgBytes);
    }

    private byte[] decoder(String encoderMsg){
        return Base64.
                getDecoder().
                decode(encoderMsg);
    }

    public String decrypt(String encodeMsg) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedMsgBytes = decoder(encodeMsg);
        Cipher decryptor;

        decryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decryptor.init(Cipher.DECRYPT_MODE, this.key, this.vi);

        byte[] decryptedMsgBytes = decryptor.doFinal(encryptedMsgBytes);

        return new String(decryptedMsgBytes);
    }
}