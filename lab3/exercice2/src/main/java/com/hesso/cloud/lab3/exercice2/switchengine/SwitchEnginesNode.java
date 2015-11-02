package com.hesso.cloud.lab3.exercice2.switchengine;

import com.google.common.base.Optional;
import com.hesso.cloud.lab3.exercice2.AbstractNode;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.extensions.FloatingIPApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;

class SwitchEnginesNode extends AbstractNode {

    private final FloatingIPApi fipApi;

    public SwitchEnginesNode(ComputeService client, NovaApi api, NodeMetadata node, String region) {
        super(client, node, region);
        this.fipApi = api.getFloatingIPApi(region).get();
    }

    @Override
    public void attachPublicIP(String publicIP) {
        this.fipApi.addToServer(publicIP, this.node.getId());
    }

    @Override
    public void detachPublicIP(String ip) {
        this.fipApi.removeFromServer(ip, this.node.getId());
    }
}
