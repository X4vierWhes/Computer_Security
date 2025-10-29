package org.example.minidns.utils;

import java.io.Serializable;

public class Client  implements Serializable {

    private String name;
    private String ip;

    public Client(String name, String ip){
        this.name = name;
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
