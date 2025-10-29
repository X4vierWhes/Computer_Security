package org.example.minidns.server;

import org.example.minidns.utils.Client;

import java.util.HashMap;
import java.util.Map;

public class MiniDnsServerImpl implements MiniDnsServerInterface{

    Map<String, String> serverMap;

    public MiniDnsServerImpl(){
        serverMap = new HashMap<>();
    }


    @Override
    public boolean update(String name, String ip) {
        return false;
    }

    @Override
    public void put(Client client) {
        serverMap.put(client.getName(), client.getIp());
    }

    @Override
    public Map<String, String> getAll(String key) {
        return Map.of();
    }

    @Override
    public boolean delete(Client client) {
        return false;
    }
}
