package org.example.minidns.client;

import org.example.minidns.gateway.GatewayInterface;

import java.util.Scanner;

public class MiniDnsClientHandler {

    Scanner cin;

    public MiniDnsClientHandler(){
        cin = new Scanner(System.in);
    }

    public void loop(GatewayInterface stub){
        System.out.println("""
                EXEMPLOS DE COMANDOS:
                GET (Name)
                PUT (Name, ip)
                DELETE (Name, ip)
                UPDATE (Name, ip)
                CALC (EXPRESSION(1+5))
                EXIT
                INICIE:
                """);
        while (true){
            String msg = cin.next();

            if(msg.equalsIgnoreCase("exit")){
                break;
            }
        }
    }
}
