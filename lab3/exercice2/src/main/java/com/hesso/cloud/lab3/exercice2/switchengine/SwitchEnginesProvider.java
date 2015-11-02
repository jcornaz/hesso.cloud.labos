package com.hesso.cloud.lab3.exercice2.switchengine;

import com.hesso.cloud.lab3.exercice2.CloudNode;
import com.hesso.cloud.lab3.exercice2.CloudProvider;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.keystone.v2_0.config.CredentialTypes;
import org.jclouds.openstack.keystone.v2_0.config.KeystoneProperties;
import org.jclouds.openstack.nova.v2_0.NovaApi;

public class SwitchEnginesProvider implements CloudProvider {

    private final String region;
    private final ComputeService client;
    private final TemplateBuilder templateBuilder;
    private final NovaApi api;

    private String netID;

    public SwitchEnginesProvider(String identity, String key) {
        this.region = "LS";

        Properties overrides = new Properties();
        overrides.setProperty(KeystoneProperties.CREDENTIAL_TYPE, CredentialTypes.PASSWORD_CREDENTIALS);
        overrides.setProperty(Constants.PROPERTY_API_VERSION, "2");

        System.out.println("Accessing to the SwitchEngines API...");

        ContextBuilder contextBuilder = ContextBuilder.newBuilder("openstack-nova")
                .endpoint("https://keystone.cloud.switch.ch:5000/v2.0/tokens")
                .credentials(String.format("%s:%s", identity, identity), key)
                .overrides(overrides)
                .modules(Arrays.asList(new SLF4JLoggingModule()));

        this.client = contextBuilder
                .buildView(ComputeServiceContext.class)
                .getComputeService();

        this.api = contextBuilder.buildApi(NovaApi.class);

        System.out.println("Create a template builder for SwitchEngines ...");
        this.templateBuilder = client.templateBuilder()
                .osFamily(OsFamily.UBUNTU)
                .hardwareId("c1.micro");
    }

    @Override
    public CloudNode createNode(String imageID, String name) throws RunNodesException {
        Template template = this.templateBuilder.imageId(imageID).build();
        template.getOptions().networks(this.netID);
        NodeMetadata node = this.client.createNodesInGroup(name, 1, template).iterator().next();
        return new SwitchEnginesNode(this.client, this.api, node, this.region);
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
