package org.example.minidns.server;

import org.example.minidns.security.AES;
import org.example.minidns.security.HMAC;
import org.example.minidns.utils.Client;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniDnsServerImpl implements MiniDnsServerInterface{

    private final Map<String, String> serverMap;
    private AES aes;
    private HMAC hmac;

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
    public List<String> getAll(String key) {
        return serverMap.entrySet().stream().map( e -> e.getKey() + " -> " + e.getValue()).toList();
    }

    @Override
    public boolean delete(Client client) {
        return serverMap.remove(client.getName(), client.getIp());
    }

    @Override
    public void sendMsg(String msg) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String [] newMsg = msg.split("/");
        System.out.println(Arrays.toString(newMsg));

        for(String a : newMsg){
            System.out.println(aes.decrypt(a));
        }

    }

    @Override
    public void notifyClients() {

    }

    @Override
    public void respondMsg(String msg) throws RemoteException {

    }
}
