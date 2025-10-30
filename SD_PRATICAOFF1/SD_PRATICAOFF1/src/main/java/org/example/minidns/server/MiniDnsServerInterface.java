package org.example.minidns.server;

import org.example.minidns.utils.Client;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public interface MiniDnsServerInterface extends Remote {

    boolean update(String name, String ip) throws RemoteException;

    void put(Client client) throws RemoteException;

    List<String> getAll() throws RemoteException;

    String get(String key) throws RemoteException;

    boolean delete(Client client) throws RemoteException;

    String sendMsg(String msg) throws Exception;

    void notifyClients() throws RemoteException;

    String respondMsg(String msg) throws RemoteException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
}
