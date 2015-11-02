package com.hesso.cloud.lab3.exercice2;

import java.util.Collection;
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
import org.jclouds.ec2.domain.InstanceType;

class AmazonProvider implements CloudProvider {
    private ComputeService client;
    private TemplateBuilder templateBuilder;
    private final AWSEC2Api api;
    private final String region;


    public AmazonProvider(String identity, String key) {
        this.region = "aws-ec2";
        
        System.out.println("Accessing to the Amazon API...");
        this.client = ContextBuilder.newBuilder(this.region)
                .credentials(identity, key)
                .buildView(ComputeServiceContext.class)
                .getComputeService();
        
        this.api = ContextBuilder.newBuilder("aws-ec2")
                .credentials(identity, key)
                .buildApi(ComputeServiceContext.class)
                .unwrapApi(AWSEC2Api.class);

        System.out.println("Create a template builder for amazon ...");
        this.templateBuilder = client.templateBuilder()
                .osFamily(OsFamily.UBUNTU)
                .hardwareId(InstanceType.T2_MICRO)
                .locationId("eu-central-1")
                .options(AWSEC2TemplateOptions.Builder.keyPair("id_hesso_amazonws").inboundPorts(22, 80));
    }

    @Override
    public CloudNode createNode(String imageID, String name) throws RunNodesException {
        Template template = this.templateBuilder.imageId(this.region + "/" + imageID).build();
        NodeMetadata node = this.client.createNodesInGroup(name, 1, template).iterator().next();
        return new AmazonNode(this.client, this.api, this.region, node);
    }

    @Override
    public Collection<? extends Image> listImages() {
        return this.client.listImages();
    }
}
