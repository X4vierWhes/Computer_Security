package org.example.minidns.server;

import org.example.minidns.security.AES;
import org.example.minidns.security.HMAC;
import org.example.minidns.utils.Client;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniDnsServerImpl implements MiniDnsServerInterface{

    private final Map<String, String> serverMap;
    private AES aes;
    private HMAC hmac;

    public MiniDnsServerImpl() throws NoSuchAlgorithmException {
        serverMap = new HashMap<>();
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
    public void sendMsg(String msg) {

    }

    @Override
    public void notifyClients() {
        System.err.println("Oi");
    }

    @Override
    public void respondMsg(String msg) throws RemoteException {

    }
}
