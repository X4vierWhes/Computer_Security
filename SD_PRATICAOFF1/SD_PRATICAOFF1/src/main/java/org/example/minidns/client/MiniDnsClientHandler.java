package org.example.minidns.client;

import org.example.minidns.gateway.GatewayInterface;
import org.example.minidns.security.AES;
import org.example.minidns.security.HMAC;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class MiniDnsClientHandler extends UnicastRemoteObject implements ClientCallbackInterface{

    private Scanner cin;
    private AES aes;
    public static final String YELLOW = "\u001b[33m";

    public MiniDnsClientHandler() throws NoSuchAlgorithmException, RemoteException {
        super();
        cin = new Scanner(System.in);
        aes = new AES(192);
    }

    public void loop(GatewayInterface stub) throws Exception {
        ClientCallbackInterface callbackInterface = new MiniDnsClientHandler();

        stub.registerClient(callbackInterface);
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
                        System.err.println(aes.decrypt(encryptedResponse[0]).replaceAll("-", " "));
                    }
                }
            }
        }
    }

    private boolean checkHmac(String [] msg) throws Exception {
        return HMAC.hMac(msg[0]).equals(msg[1]);
    }

    @Override
    public void notifyUpdate(String updateMessage) throws Exception {
        String[] encryptMsg = updateMessage.split("/");
        if(checkHmac(encryptMsg)){
            String decryptMsg = aes.decrypt(encryptMsg[0]).replaceAll("-", " ");
            System.out.println("Update: " + YELLOW + decryptMsg);
        }
    }
}
