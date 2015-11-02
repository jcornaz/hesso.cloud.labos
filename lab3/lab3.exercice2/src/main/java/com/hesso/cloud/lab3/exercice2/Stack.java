package com.hesso.cloud.lab3.exercice2;

public class Stack {
    
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

    public void run() {
        this.mongo.run();
        this.restClient.run();
        this.restServer.run();
    }
    
    public void end() {
        this.restServer.end();
        this.restClient.end();
        this.mongo.end();
    }
}
