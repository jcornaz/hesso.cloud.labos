package com.hesso.cloud.lab3.exercice2;

public class RESTClient extends Server {

    private MongoDB mongo;
    private final String publicIP;

    RESTClient(CloudProvider provider, String imageID, String publicIP) {
        super(provider, imageID);
        this.setName("RESTCLient");
        this.publicIP = publicIP;
    }

    void setMongo(MongoDB mongo) {
        this.mongo = mongo;
    }

    @Override
    protected void afterInstanciation() {
        this.attachPublicIP(this.publicIP);
        this.execute("python restclient.py " + this.mongo.getPrivateIP());
        this.detachPublicIPs();
    }
}
