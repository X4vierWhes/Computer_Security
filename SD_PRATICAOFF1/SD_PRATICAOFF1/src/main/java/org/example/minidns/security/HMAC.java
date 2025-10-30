package org.example.minidns.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMAC {
    //public static final String ALG = "HmacSHA256";
    //public static final String ALG = "HmacSHA224";
    //public static final String ALG = "HmacSHA384";
    public static final String ALG = "HmacSHA512";
    private static final String chave = "whesley";

    public static String hMac(String mensagem) throws Exception {
        Mac shaHMAC = Mac.getInstance(ALG);
        SecretKeySpec chaveMAC =
                new SecretKeySpec(
                        chave.getBytes("UTF-8"),
                        ALG);
        shaHMAC.init(chaveMAC);
        byte[] bytesHMAC =
                shaHMAC
                        .doFinal(mensagem.getBytes("UTF-8"));
        return byte2Hex(bytesHMAC);
    }

    public static String byte2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(byte b: bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
