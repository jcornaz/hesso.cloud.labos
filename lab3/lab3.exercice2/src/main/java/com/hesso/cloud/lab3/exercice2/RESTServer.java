package com.hesso.cloud.lab3.exercice2;

public class RESTServer extends Server {

    private MongoDB mongo;
    private final String publicIP;

    RESTServer(CloudProvider provider, String imageID, String publicIP) {
        super(provider, imageID);
        this.setName("RESTServer");
        this.publicIP = publicIP;
    }

    void setMongo(MongoDB mongo) {
        this.mongo = mongo;
    }

    @Override
    protected void afterInstanciation() {
        this.attachPublicIP(this.publicIP);
        this.execute("python restserver.py " + this.mongo.getPrivateIP());
    }
}
