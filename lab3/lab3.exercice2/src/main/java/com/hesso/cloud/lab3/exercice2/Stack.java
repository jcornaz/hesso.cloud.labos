package com.hesso.cloud.lab3.exercice2;

public class Stack extends Thread {
    
    private final MongoDB mongo;
    private final RESTClient restClient;
    private final RESTServer restServer;

    public Stack(MongoDB mongo, RESTClient restClient, RESTServer restServer) {
        this.mongo = mongo;
        this.restClient = restClient;
        this.restServer = restServer;
        
        this.restClient.setMongo(this.mongo);
        this.restServer.setMongo(this.mongo);
    }

    @Override
    public void run() {
        this.mongo.start();
        this.restClient.start();
        this.restServer.start();
    }
    
    public void destroy() {
        this.restServer.destroy();
        this.restClient.destroy();
        this.mongo.destroy();
    }
}
