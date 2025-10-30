package org.example.minidns.gateway;

import org.example.minidns.utils.Client;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GatewayInterface extends Remote {
    boolean update(String name, String ip) throws RemoteException;

    void put(Client client) throws RemoteException;

    List<String> getAll() throws RemoteException;

    boolean delete(Client client) throws RemoteException;

    String sendMsg(String msg) throws Exception;

    void init() throws RemoteException, NotBoundException;
}
