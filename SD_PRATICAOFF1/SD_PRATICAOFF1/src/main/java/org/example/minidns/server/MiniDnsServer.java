package org.example.minidns.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;

public class MiniDnsServer {

    private static final int SERVER_A_PORT = 6000;
    private static final int SERVER_B_PORT = 6010;

    public static void main(String[] args){
        System.setProperty("java.security.policy", "java.policy");

        try {

            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Registry registro = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);

            //LocateRegistry.createRegistry(Registry.REGISTRY_PORT + 100);
            //Registry registro = LocateRegistry.getRegistry(Registry.REGISTRY_PORT + 100);


            initServer(registro, "MiniDns-A", SERVER_A_PORT);
            //initServer(registro, "MiniDns-B", SERVER_B_PORT);

        } catch (RemoteException e) {
            System.err.println("Erro ao criar o RMI Registry: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Método genérico para iniciar e registrar uma instância do servidor.
     */
    private static void initServer(Registry registry, String name, int port){
        try {
            // 1. Cria uma nova instância da implementação do servidor
            MiniDnsServerImpl refObjRemoto = new MiniDnsServerImpl();

            // 2. Exporta o objeto remoto na porta especificada
            MiniDnsServerInterface sklt = (MiniDnsServerInterface)
                    UnicastRemoteObject.exportObject(refObjRemoto, port);

            // 3. Registra o objeto no Registry sob o nome único
            registry.bind(name, sklt);

            System.err.println(name + " iniciado e registrado na porta " + port);

        } catch (RemoteException | AlreadyBoundException | NoSuchAlgorithmException e) {
            System.err.println("Erro ao iniciar/registrar " + name + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}