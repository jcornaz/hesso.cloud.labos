/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hesso.cloud.lab3.exercice2;

import java.util.Set;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.NodeMetadata;

/**
 *
 * @author jonathan
 */
public abstract class AbstractNode implements CloudNode {

    protected ComputeService client;
    protected NodeMetadata node;

    protected AbstractNode(ComputeService client, NodeMetadata node) {
        this.client = client;
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
}
