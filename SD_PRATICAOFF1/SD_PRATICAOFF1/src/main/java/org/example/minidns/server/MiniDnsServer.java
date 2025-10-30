package org.example.minidns.server;

import org.example.minidns.security.AES;
import org.example.minidns.security.HMAC;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MiniDnsServer {

    private AES aes;
    private HMAC hmac;

    public static void main(String[] args){
        try {
            MiniDnsServerImpl refObjRemoto = new MiniDnsServerImpl();
            MiniDnsServerInterface sklt = (MiniDnsServerInterface) UnicastRemoteObject.exportObject(refObjRemoto, 6000);
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Registry registro = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
            registro.bind("MiniDns", sklt);

            System.err.println("MiniDnsServer iniciado!");
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
