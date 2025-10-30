package org.example.minidns.client;

import org.example.minidns.gateway.GatewayInterface;
import org.example.minidns.security.AES;
import org.example.minidns.security.HMAC;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class MiniDnsClientHandler {

    private Scanner cin;
    private AES aes;

    public MiniDnsClientHandler() throws NoSuchAlgorithmException {
        cin = new Scanner(System.in);
        aes = new AES(192);
    }

    public void loop(GatewayInterface stub) throws Exception {
        System.out.print("""
                EXEMPLOS DE COMANDOS:
                GET (Name) or GET (ALL)
                PUT (Name, ip)
                DELETE (Name, ip)
                UPDATE (Name, ip)
                CALC (EXPRESSION)
                EXIT
                INICIE:
                """);
        while (true){
            String msg = cin.nextLine();

            if(msg.equalsIgnoreCase("exit")){
                break;
            }else if(!msg.equalsIgnoreCase("/")){
                String encrypted = aes.encrypt(msg);
                //System.err.println(encrypted);
                String response = stub.sendMsg( encrypted + "/" + HMAC.hMac(encrypted));
                if(response != null){
                    String[] encryptedResponse = response.split("/");
                    if(checkHmac(encryptedResponse)){
                        System.err.println(aes.decrypt(encryptedResponse[0]));
                    }
                }
            }
        }
    }

    private boolean checkHmac(String [] msg) throws Exception {
        return HMAC.hMac(msg[0]).equals(msg[1]);
    }
}
