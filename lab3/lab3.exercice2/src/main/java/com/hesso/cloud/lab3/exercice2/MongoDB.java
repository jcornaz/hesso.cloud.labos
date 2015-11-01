
package com.hesso.cloud.lab3.exercice2;

import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.Template;

public class MongoDB extends Server {

    MongoDB(ComputeService client, Template template, Image image) {
        super(client, template, image);
        this.setName("MongoDB");
    }

    @Override
    protected void afterInstanciation() {
        // Nothing special to do
    }
}
