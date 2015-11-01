package com.hesso.cloud.lab3.exercice2;

import java.util.Set;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;

public abstract class Server extends Thread {

    private final Image image;
    private final ComputeService client;
    private final Template template;
    private Set<? extends NodeMetadata> meta;

    public Server(ComputeService client, Template template, Image image) {
        this.client = client;
        this.template = template;
        this.image = image;
    }

    @Override
    public synchronized void run() {
        try {
            System.out.println("Instanciating " + this.getName() + " ...");
            this.meta = this.client.createNodesInGroup(this.getName(), 1, this.template);
            System.out.println("instanciated" + this.getName());

            this.afterInstanciation();
        } catch (RunNodesException e) {
            e.printStackTrace();
        }
    }

    public synchronized void destroy() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (NodeMetadata metadata : Server.this.meta) {
                    System.out.println("Destroying " + metadata.getId() + " (" + Server.this.getName() + ") ...");
                    Server.this.client.destroyNode(metadata.getId());
                }
                System.out.println(Server.this.getName() + " destroyed");
            }
        }
        ).start();
    }

    public String getPrivateIP() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void execute(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void attachPublicIP(String publicIP) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void detachPublicIPs() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected abstract void afterInstanciation();
}
