package com.hesso.cloud.lab3.exercice2.amazon;

import com.hesso.cloud.lab3.exercice2.CloudNode;
import java.util.Set;
import org.jclouds.aws.ec2.AWSEC2Api;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.ec2.features.ElasticIPAddressApi;

public class AmazonNode implements CloudNode{
    private final ComputeService client;
    private final NodeMetadata node;
    private final AWSEC2Api api;
    private final String region;
    private final ElasticIPAddressApi eipApi;

    AmazonNode(ComputeService client, AWSEC2Api api, String region, NodeMetadata node) {
        this.client = client;
        this.region = region;
        this.api = api;
        this.eipApi = this.api.getElasticIPAddressApi().get();
        this.node = node;
    }

    @Override
    public void destroy() {
        this.client.destroyNode(this.node.getId());
    }

    @Override
    public Set<String> getPrivateAddresses() {
        return this.node.getPrivateAddresses();
    }

    @Override
    public Set<String> getPublicAddresses() {
        return this.node.getPublicAddresses();
    }

    @Override
    public void execute(String command) {
        this.client.runScriptOnNode(this.node.getId(), command);
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
