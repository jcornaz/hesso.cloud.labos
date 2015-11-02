
package com.hesso.cloud.lab3.exercice2.switchengine;

import com.hesso.cloud.lab3.exercice2.CloudNode;
import com.hesso.cloud.lab3.exercice2.CloudProvider;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.Image;

public class SwitchEngineProvider implements CloudProvider {

    public SwitchEngineProvider(String identity, String key) {
    }

    @Override
    public CloudNode createNode(String imageID, String name) throws RunNodesException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterable<? extends Image> listImages() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
