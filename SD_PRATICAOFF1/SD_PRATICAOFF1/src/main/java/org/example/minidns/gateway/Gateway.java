package org.example.minidns.gateway;

import org.example.minidns.security.AES;
import org.example.minidns.security.HMAC;
import org.example.minidns.server.MiniDnsServerImpl;
import org.example.minidns.server.MiniDnsServerInterface;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Gateway {
    private AES aes;
    private HMAC hmac;

    public static void main(String[] args){
        System.setProperty("java.security.policy", "java.policy");
        try {
            GatewayImpl refObjRemoto = new GatewayImpl();
            GatewayInterface sklt = (GatewayInterface) UnicastRemoteObject.exportObject(refObjRemoto, 5000);
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT + 1);
            Registry registro = LocateRegistry.getRegistry(Registry.REGISTRY_PORT + 1);
            registro.bind("Gateway", sklt);

            System.err.println("Gateway iniciado!");

            sklt.init();
        } catch (RemoteException | AlreadyBoundException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
