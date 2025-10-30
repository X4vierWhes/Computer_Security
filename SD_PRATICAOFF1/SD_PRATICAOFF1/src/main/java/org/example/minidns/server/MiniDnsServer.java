package org.example.minidns.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;

public class MiniDnsServer {
    public static void main(String[] args){
        System.setProperty("java.security.policy", "java.policy");
        try {
            MiniDnsServerImpl refObjRemoto = new MiniDnsServerImpl();
            MiniDnsServerInterface sklt = (MiniDnsServerInterface) UnicastRemoteObject.exportObject(refObjRemoto, 6000);
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Registry registro = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
            registro.bind("MiniDns", sklt);

            System.err.println("MiniDnsServer iniciado!");
        } catch (RemoteException | AlreadyBoundException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
