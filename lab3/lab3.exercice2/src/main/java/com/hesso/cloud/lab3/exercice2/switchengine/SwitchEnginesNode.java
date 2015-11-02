package com.hesso.cloud.lab3.exercice2.switchengine;

import com.hesso.cloud.lab3.exercice2.AbstractNode;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.NodeMetadata;

class SwitchEnginesNode extends AbstractNode {

    public SwitchEnginesNode(ComputeService client, NodeMetadata node) {
        super(client, node);
    }

    @Override
    public void attachPublicIP(String publicIP) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void detachPublicIP(String ip) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
