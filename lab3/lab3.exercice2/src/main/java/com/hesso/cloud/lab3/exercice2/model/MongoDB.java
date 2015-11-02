
package com.hesso.cloud.lab3.exercice2.model;

import com.hesso.cloud.lab3.exercice2.CloudProvider;

public class MongoDB extends Server {

    public MongoDB(CloudProvider provider, String imageID) {
        super(provider, imageID);
        this.setName("MongoDB");
    }

    @Override
    protected void afterInstanciation() {
        // Nothing special to do
    }
}
