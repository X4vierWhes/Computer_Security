package org.example.minidns.server;

import org.example.minidns.utils.Client;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniDnsServerImpl implements MiniDnsServerInterface{

    Map<String, String> serverMap;

    public MiniDnsServerImpl(){
        serverMap = new HashMap<>();
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
