package org.example.minidns.gateway;

import org.example.minidns.client.ClientCallbackInterface;
import org.example.minidns.server.MiniDnsServerInterface;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GatewayImpl implements GatewayInterface {

    //private MiniDnsServerInterface miniDnsServerInterface;
    private List<MiniDnsServerInterface> miniDnsServerReplicas;
    private String[] names = {"MiniDns-A", "MiniDns-B"};
    private final AtomicInteger currentServerIndex = new AtomicInteger(0);

    private MiniDnsServerInterface getNextReplica() throws RemoteException {
        if (miniDnsServerReplicas == null || miniDnsServerReplicas.isEmpty()) {
            throw new RemoteException("Nenhuma réplica está conectada.");
        }


        for (int i = 0; i < miniDnsServerReplicas.size(); i++) {

            int index = currentServerIndex.getAndIncrement() % miniDnsServerReplicas.size();

            MiniDnsServerInterface server = miniDnsServerReplicas.get(index);

            try {
                return server;
            } catch (Exception e) {
                System.err.println("AVISO: Réplica no índice " + index + " falhou. Tentando próxima...");
            }
        }

        // Se todas as réplicas falharem após o ciclo
        throw new RemoteException("Todas as réplicas do servidor falharam ao processar a requisição.");
    }

    @Override
    public String sendMsg(String msg) throws Exception {
        // Tenta obter o próximo servidor e enviar a mensagem
        MiniDnsServerInterface server = getNextReplica();
        return server.sendMsg(msg);
    }

    @Override
    public void init() throws RemoteException, NotBoundException {


        miniDnsServerReplicas = new ArrayList<>();

        boolean foundAny = false;
        int nextRegistry = 0;
        for (String name : names) {
            try {
                Registry registry = LocateRegistry.getRegistry("localhost", Registry.REGISTRY_PORT + nextRegistry);
                MiniDnsServerInterface replica = (MiniDnsServerInterface) registry.lookup(name);
                miniDnsServerReplicas.add(replica);
                System.out.println("Gateway conectado à réplica: " + name);
                foundAny = true;
            } catch (NotBoundException e) {
                System.err.println("AVISO: Réplica não encontrada no Registry: " + name);
            }

            nextRegistry = 100;
        }

        if (!foundAny) {
            throw new NotBoundException("Nenhuma réplica do MiniDns foi encontrada. Verifique se MiniDns-A e MiniDns-B estão rodando.");
        }
    }

    @Override
    public void registerClient(ClientCallbackInterface client) throws RemoteException {
        if (miniDnsServerReplicas.isEmpty()) {
            throw new RemoteException("Nenhuma réplica conectada para registro.");
        }

        for (MiniDnsServerInterface replica : miniDnsServerReplicas) {
            replica.registerClient(client);
        }
    }
}
