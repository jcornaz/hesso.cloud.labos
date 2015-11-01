package com.hesso.cloud.lab3.exercice2;

import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.Template;

public class RESTClient extends Server {

    private MongoDB mongo;
    private final String publicIP;

    RESTClient(ComputeService client, Template template, Image image, String publicIP) {
        super(client, template, image);
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
