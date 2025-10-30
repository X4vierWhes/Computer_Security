package org.example.minidns.server;

import org.example.minidns.client.ClientCallbackInterface;
import org.example.minidns.security.AES;
import org.example.minidns.security.HMAC;
import org.example.minidns.utils.CalculatorEngine;
import org.example.minidns.utils.Client;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MiniDnsServerImpl implements MiniDnsServerInterface{

    private final Map<String, String> serverMap;
    private AES aes;
    String [] commands = {"put", "calc", "get", "delete", "update"};
    private final List<ClientCallbackInterface> registeredClients;

    public MiniDnsServerImpl() throws NoSuchAlgorithmException {
        serverMap = new HashMap<>() {{
            put("servidor1", "192.168.0.10");
            put("servidor2", "192.168.0.20");
            put("servidor3", "192.168.0.30");
            put("servidor4", "192.168.0.40");
            put("servidor5", "192.168.0.50");
            put("servidor6", "192.168.0.60");
            put("servidor7", "192.168.0.70");
            put("servidor8", "192.168.0.80");
            put("servidor9", "192.168.0.90");
            put("servidor10", "192.168.0.100");
        }};

        /*
        serverMap = new HashMap<>() {{
            put("servidor11", "192.168.0.10");
            put("servidor12", "192.168.0.20");
            put("servidor13", "192.168.0.30");
            put("servidor14", "192.168.0.40");
            put("servidor15", "192.168.0.50");
            put("servidor16", "192.168.0.60");
            put("servidor17", "192.168.0.70");
            put("servidor18", "192.168.0.80");
            put("servidor19", "192.168.0.90");
            put("servidor20", "192.168.0.100");
        }};
        */

        registeredClients = new ArrayList<>();
        aes = new AES(192);

    }

    @Override
    public boolean update(String name, String ip) {
        String aux = serverMap.get(name);
        if(aux != null){
            serverMap.put(name, ip);
            return true;
        }
        return false;
    }

    @Override
    public void put(Client client) {
        serverMap.put(client.getName(), client.getIp());
    }

    @Override
    public List<String> getAll() {
        return serverMap.entrySet().stream().map( e -> e.getKey() + " -> " + e.getValue()).toList();
    }

    @Override
    public String get(String key) throws RemoteException {
        String value = serverMap.get(key);
        //System.err.println(value);
        return key + " -> " + value;
    }
    @Override
    public boolean delete(Client client) {
        return serverMap.remove(client.getName(), client.getIp());
    }

    @Override
    public String sendMsg(String msg) throws Exception {
        System.err.println("Mensagem encriptada: " + msg);
        String [] newMsg = msg.split("/");

        if(checkHmac(newMsg)){
            System.out.println("HMAC IGUAL");
            String decryptMsg = aes.decrypt(newMsg[0]);
            //System.out.println(decryptMsg);
            return respondMsg(checkCommand(decryptMsg));
        }

        return respondMsg("Error: Chaves contraditórias");
    }

    @Override
    public void notifyClients(String updateMessage) throws RemoteException {
        List<ClientCallbackInterface> clientsToRemove = new ArrayList<>();

        synchronized (registeredClients) {
            for (ClientCallbackInterface client : registeredClients) {
                try {
                    client.notifyUpdate(updateMessage);
                } catch (RemoteException e) {
                    clientsToRemove.add(client);
                    System.err.println("Cliente desconectado ou falhou na notificação.");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            registeredClients.removeAll(clientsToRemove);
        }
    }

    @Override
    public String respondMsg(String msg) throws RemoteException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String encryptMsg = aes.encrypt(msg);
        System.out.println("Mensagem de resposta: " + msg);
        try {
            String updateMsg = encryptMsg + "/" + HMAC.hMac(encryptMsg);
            notifyClients(updateMsg);
            return encryptMsg + "/" + HMAC.hMac(encryptMsg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerClient(ClientCallbackInterface client) throws RemoteException {
        registeredClients.add(client);
    }

    @Override
    public boolean checkServer(String key) throws RemoteException {
        return false;
    }

    private boolean checkHmac(String [] msg) throws Exception {
        return HMAC.hMac(msg[0]).equals(msg[1]);
    }

    private String checkCommand(String decryptMsg) throws RemoteException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String[] command = decryptMsg.split("-");
        //System.err.println(command[0]);

        for(String a : commands){
            if(a.equalsIgnoreCase(command[0])){
                return makeComand(a, decryptMsg);
            }
        }
        return "Error: Comando inexistente";
    }

    private String makeComand(String command, String msg) throws RemoteException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        System.err.println("Entrei na linha 122, MiniDNSServerImpl");
        switch (command){
            case "get":
                String[] get = msg.split("-");
                get[1] = get[1].toLowerCase();
                if(!get[1].equalsIgnoreCase("all")) {
                    return get(get[1]);
                }else{
                    List<String> all = getAll();
                    return all.toString();
                }
            case "put":
                String[] put = msg.split("-");
                System.err.println(Arrays.toString(put));
                put(new Client(put[1], put[2]));
                return "Put: " + put[1] + ", " + put[2];
            case "update":
                String[] update = msg.split("-");
                System.err.println(Arrays.toString(update));
                update(update[1], update[2]);
                return "Update: Name: " + update[1] + " IP: " + update[2];
            case "delete":
                String[] delete = msg.split("-");
                System.err.println(Arrays.toString(delete));
                delete(new Client(delete[1], delete[2]));
                return "Delete: Name: " + delete[1] + " Ip: " + delete[2];
            case "calc": String[] calc = msg.split("-");
                //System.err.println(Arrays.toString(calc));
                StringBuilder expression = new StringBuilder();
                for(int i = 1; i < calc.length; i++){
                    expression.append(calc[i]);
                }
                System.err.println(expression);
                double result = CalculatorEngine.calcExpression(String.valueOf(expression));
                return "Resultado da expressão (" + expression + ") = " + result;
        }

        return "Error: Não foi possível realizar comando";
    }
}
