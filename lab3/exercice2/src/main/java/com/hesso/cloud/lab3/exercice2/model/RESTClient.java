package com.hesso.cloud.lab3.exercice2.model;

import com.hesso.cloud.lab3.exercice2.CloudProvider;

public class RESTClient extends Server {

    private MongoDB mongo;
    private final String publicIP;

    public RESTClient(CloudProvider provider, String imageID, String publicIP) {
        super(provider, imageID);
        this.setName("RESTCLient");
        this.publicIP = publicIP;
    }

    public void setMongo(MongoDB mongo) {
        this.mongo = mongo;
    }

    @Override
    protected void afterInstanciation() {
        this.attachPublicIP(this.publicIP);
        this.execute("python restclient.py " + this.mongo.getPrivateIP());
        this.detachPublicIPs();
    }
}
