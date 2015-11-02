package com.hesso.cloud.lab3.exercice2.amazon;

import com.hesso.cloud.lab3.exercice2.AbstractNode;
import org.jclouds.aws.ec2.AWSEC2Api;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.ec2.features.ElasticIPAddressApi;

public class AmazonNode extends AbstractNode{
    
    private final AWSEC2Api api;
    private final String region;
    private final ElasticIPAddressApi eipApi;

    AmazonNode(ComputeService client, AWSEC2Api api, String region, NodeMetadata node) {
        super(client, node);
        this.region = region;
        this.api = api;
        this.eipApi = this.api.getElasticIPAddressApi().get();
    }

    @Override
    public void attachPublicIP(String ip) {
        this.eipApi.associateAddressInRegion(this.region, ip, this.node.getId());
    }

    @Override
    public void detachPublicIP(String ip) {
        this.eipApi.disassociateAddressInRegion(this.region, ip);
    }
    
}
