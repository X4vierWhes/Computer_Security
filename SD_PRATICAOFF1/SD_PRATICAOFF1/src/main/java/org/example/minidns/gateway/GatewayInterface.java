package org.example.minidns.gateway;

import org.example.minidns.client.ClientCallbackInterface;
import org.example.minidns.utils.Client;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GatewayInterface extends Remote {

    String sendMsg(String msg) throws Exception;

    void init() throws RemoteException, NotBoundException;

    void registerClient(ClientCallbackInterface client) throws RemoteException;
}
