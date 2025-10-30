package org.example.minidns.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallbackInterface extends Remote {
    void notifyUpdate(String updateMessage) throws Exception;
}