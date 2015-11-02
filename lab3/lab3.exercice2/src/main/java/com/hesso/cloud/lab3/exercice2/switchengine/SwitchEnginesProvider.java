package com.hesso.cloud.lab3.exercice2.switchengine;

import com.hesso.cloud.lab3.exercice2.CloudNode;
import com.hesso.cloud.lab3.exercice2.CloudProvider;
import org.jclouds.ContextBuilder;
import org.jclouds.aws.ec2.AWSEC2Api;
import org.jclouds.aws.ec2.compute.AWSEC2TemplateOptions;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;

public class SwitchEnginesProvider implements CloudProvider {

    private final String region;
    private final ComputeService client;
    private final TemplateBuilder templateBuilder;
    private String netID;

    public SwitchEnginesProvider(String identity, String key) {
        this.region = "LS";

        System.out.println("Accessing to the SwitchEngines API...");
        this.client = ContextBuilder.newBuilder("openstack-nova")
                .endpoint("https://keystone.cloud.switch.ch:5000/v2.0/tokens")
                .credentials(String.format("%s:%s", identity, identity), key)
                .buildView(ComputeServiceContext.class)
                .getComputeService();

        System.out.println("Create a template builder for SwitchEngines ...");
        this.templateBuilder = client.templateBuilder()
                .osFamily(OsFamily.UBUNTU)
                .hardwareId("c1.micro")
                .locationId(this.region)
                .options(AWSEC2TemplateOptions.Builder.keyPair("id_hesso_amazonws").inboundPorts(22, 80));
    }

    @Override
    public CloudNode createNode(String imageID, String name) throws RunNodesException {
        Template template = this.templateBuilder.imageId(imageID).build();
        template.getOptions().networks(this.netID);
        NodeMetadata node = this.client.createNodesInGroup(name, 1, template).iterator().next();
        return new SwitchEnginesNode(this.client, node);
    }

    @Override
    public Iterable<? extends Image> listImages() {
        return this.client.listImages();
    }

    @Override
    public void setNetworkID(String netID) {
        this.netID = netID;
    }
}
