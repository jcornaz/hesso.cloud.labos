package com.hesso.cloud.lab3.exercice2.model;

import com.hesso.cloud.lab3.exercice2.CloudNode;
import com.hesso.cloud.lab3.exercice2.CloudProvider;
import java.util.Set;
import org.jclouds.compute.RunNodesException;

public abstract class Server {

    private final String imageID;
    private final CloudProvider provider;
    CloudNode node;
    private String name;

    public Server(CloudProvider provider, String imageID) {
        this.provider = provider;
        this.imageID = imageID;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }

    public synchronized void run() {
        try {
            System.out.println("Instanciating " + this.getName() + " ...");
            this.node = this.provider.createNode(this.imageID, this.getName());
            System.out.println("instanciated" + this.getName());

            this.afterInstanciation();
        } catch (RunNodesException e) {
            e.printStackTrace();
        }
    }

    public synchronized void end() {
        new Thread(() -> {
                System.out.println("Destroying " + Server.this.getName() + ") ...");
                this.node.destroy();
            System.out.println(Server.this.getName() + " destroyed");
        }).start();
    }

    public synchronized String getPrivateIP() {
        System.out.println(this.getName() + " is waiting for an ip address...");
        Set<String> addresses = this.node.getPrivateAddresses();
                
        while(addresses.isEmpty())
        {
            try {
                this.wait(1000);
            } catch (InterruptedException ex) {
                break;
            }
            
            addresses = this.node.getPrivateAddresses();
        }
        
        return addresses.iterator().next();
    }

    public void execute(String command) {
        System.out.println("execute \"nohup + command + \" </dev/null >logfile.log 2>&1 &\" on " + this.getName());
        this.node.execute( "nohup " + command + " </dev/null >logfile.log 2>&1 &");
    }

    public void attachPublicIP(String publicIP) {
        System.out.println( "Attaching " + publicIP + " to " + this.getName());
        this.node.attachPublicIP(publicIP);
    }

    public void detachPublicIPs() {
        System.out.println("Detaching ips from " + this.getName());
        this.node.getPublicAddresses().forEach((ip) -> this.node.detachPublicIP(ip));
    }

    protected abstract void afterInstanciation();
}
