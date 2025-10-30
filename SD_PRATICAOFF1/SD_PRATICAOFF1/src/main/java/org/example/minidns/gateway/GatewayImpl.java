package org.example.minidns.gateway;

import org.example.minidns.server.MiniDnsServerInterface;
import org.example.minidns.utils.Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class GatewayImpl implements GatewayInterface{

    private MiniDnsServerInterface miniDnsServerInterface;

    @Override
    public boolean update(String name, String ip) throws RemoteException {
        return miniDnsServerInterface.update(name, ip);
    }

    @Override
    public void put(Client client) throws RemoteException {
        miniDnsServerInterface.put(client);
    }

    @Override
    public List<String> getAll(String key) throws RemoteException {
        return miniDnsServerInterface.getAll(key);
    }

    @Override
    public boolean delete(Client client) throws RemoteException {
        return miniDnsServerInterface.delete(client);
    }

    @Override
    public void sendMsg(String msg) throws RemoteException {

    }

    @Override
    public void init() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", Registry.REGISTRY_PORT);
        miniDnsServerInterface = (MiniDnsServerInterface) registry.lookup("MiniDns");
        miniDnsServerInterface.notifyClients();
    }
}
