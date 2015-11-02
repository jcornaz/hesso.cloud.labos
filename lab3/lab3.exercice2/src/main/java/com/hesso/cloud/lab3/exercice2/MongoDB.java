
package com.hesso.cloud.lab3.exercice2;

public class MongoDB extends Server {

    MongoDB(CloudProvider provider, String imageID) {
        super(provider, imageID);
        this.setName("MongoDB");
    }

    @Override
    protected void afterInstanciation() {
        // Nothing special to do
    }
}
