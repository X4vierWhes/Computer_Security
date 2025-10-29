package org.example.minidns.server;

import org.example.minidns.utils.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface MiniDnsServerInterface extends Remote {

    boolean update(String name, String ip) throws RemoteException;

    void put(Client client) throws RemoteException;

    Map<String, String> getAll(String key) throws RemoteException;

    boolean delete(Client client) throws RemoteException;
}
