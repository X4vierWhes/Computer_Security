package org.example.minidns.security;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
        //this.keyGenerator = KeyGenerator.getInstance("AES");
        //this.keyGenerator.init(t);
        //this.key = keyGenerator.generateKey();
        //this.vi = generateVI();
        //System.out.println(Arrays.toString(key.getEncoded()));
        this.key = new SecretKeySpec("1234567891234567".getBytes(), "AES");
        this.vi = new IvParameterSpec(new byte[] {
                (byte)0x1A, (byte)0x2B, (byte)0x3C, (byte)0x4D,
                (byte)0x5E, (byte)0x6F, (byte)0x70, (byte)0x81,
                (byte)0x92, (byte)0xA3, (byte)0xB4, (byte)0xC5,
                (byte)0xD6, (byte)0xE7, (byte)0xF8, (byte)0x09
        });
    }

    public static IvParameterSpec generateVI(){
        byte[] vi = new byte[16];
        new SecureRandom().nextBytes(vi);
        return new IvParameterSpec(vi);
    }

    public String encrypt(String openMsg) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptMsgBytes;
        Cipher encryptor;

        this.msg = openMsg.replaceAll("\\s", "-").
                replaceAll(",", "-");

        encryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");

        encryptor.init(Cipher.ENCRYPT_MODE, this.key, this.vi);

        encryptMsgBytes = encryptor.doFinal(this.msg.getBytes());

        encryptedMsg = encoder(encryptMsgBytes);

        return  encryptedMsg;
    }

    private String encoder(byte[] encryptMsgBytes) {
        return Base64
                .getUrlEncoder()
                .encodeToString(encryptMsgBytes);
    }

    private byte[] decoder(String encoderMsg){
        return Base64.
                getUrlDecoder().
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