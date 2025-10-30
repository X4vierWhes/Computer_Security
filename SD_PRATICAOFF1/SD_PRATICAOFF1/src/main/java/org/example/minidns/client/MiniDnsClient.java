package org.example.minidns.client;

import org.example.minidns.gateway.GatewayInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MiniDnsClient {
    protected static GatewayInterface gatewayInterface;

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", Registry.REGISTRY_PORT + 1);
        gatewayInterface = (GatewayInterface) registry.lookup("Gateway");

        MiniDnsClientHandler handler = new MiniDnsClientHandler();

        handler.loop(gatewayInterface);

    }
}
