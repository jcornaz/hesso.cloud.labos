package com.hesso.cloud.lab3.exercice2;

import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.Template;

public class RESTServer extends Server {

    private MongoDB mongo;
    private final String publicIP;

    RESTServer(ComputeService client, Template template, Image image, String publicIP) {
        super(client, template, image);
        this.setName("RESTServer");
        this.publicIP = publicIP;
    }

    void setMongo(MongoDB mongo) {
        this.mongo = mongo;
    }

    @Override
    protected void afterInstanciation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
