package org.example.minidns.client;

import org.example.minidns.gateway.GatewayInterface;
import org.example.minidns.security.AES;
import org.example.minidns.security.HMAC;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class MiniDnsClientHandler {

    private Scanner cin;
    private AES aes;
    private HMAC hmac;

    public MiniDnsClientHandler() throws NoSuchAlgorithmException {
        cin = new Scanner(System.in);
        aes = new AES(192);
    }

    public void loop(GatewayInterface stub) throws Exception {
        System.out.println("""
                EXEMPLOS DE COMANDOS:
                GET
                PUT (Name, ip)
                DELETE (Name, ip)
                UPDATE (Name, ip)
                CALC (EXPRESSION(1+5))
                EXIT
                INICIE:
                """);
        while (true){
            String msg = cin.next();

            if(msg.equalsIgnoreCase("exit")){
                break;
            }else {
                String encrypted = aes.encrypt(msg);
                stub.sendMsg( encrypted + "/" + HMAC.hMac(encrypted));
            }
        }
    }
}
