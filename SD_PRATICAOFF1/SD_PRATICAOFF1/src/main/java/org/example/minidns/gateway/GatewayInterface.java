package org.example.minidns.gateway;

import org.example.minidns.server.MiniDnsServerInterface;
import org.example.minidns.utils.Client;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface GatewayInterface extends Remote {
    boolean update(String name, String ip) throws RemoteException;

    void put(Client client) throws RemoteException;

    List<String> getAll(String key) throws RemoteException;

    boolean delete(Client client) throws RemoteException;

    void sendMsg(String msg) throws RemoteException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    void init() throws RemoteException, NotBoundException;
}
