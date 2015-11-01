package com.hesso.cloud.lab3.exercice2;

import java.io.File;
import java.io.IOException;
import org.jclouds.ContextBuilder;
import org.jclouds.aws.ec2.compute.AWSEC2TemplateOptions;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.Template;
import org.jclouds.ec2.domain.InstanceType;

public class RunInstances {

    public static void main(String[] args) throws RunNodesException, IOException {

        String keyPair = args[0];
        String identity = args[1];
        String key = args[2];

        System.out.println("keypair : " + new File(keyPair).exists());

        System.out.println("Accessing to the Amazon API...");
        ComputeService client = ContextBuilder.newBuilder("aws-ec2")
                .credentials(identity, key)
                .buildView(ComputeServiceContext.class)
                .getComputeService();

        System.out.println("Build a template...");
        Template template = client.templateBuilder()
                .osFamily(OsFamily.UBUNTU)
                .hardwareId(InstanceType.T2_MICRO)
                .locationId("eu-central-1")
                .options(AWSEC2TemplateOptions.Builder.keyPair("id_hesso_amazonws").inboundPorts(22, 80))
                .build();

        Stack stack = StackFileReader.load("../amazon-stack.yml", client, template);

        System.out.println("Start instances ...");
        stack.start();

        char pressedChar;
        do {
            System.out.println("Press 'A' to abort : ");
            pressedChar = (char) System.in.read();
        } while (pressedChar != 'A');

        stack.destroy();
    }
}
