/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hesso.cloud.lab3.exercice2;

import java.io.File;
import java.io.IOException;
import org.jclouds.ContextBuilder;
import org.jclouds.aws.ec2.compute.AWSEC2TemplateOptions;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.ec2.domain.InstanceType;

/**
 *
 * @author jonathan
 */
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

        System.out.println("Creating a template...");
        TemplateBuilder builder = client.templateBuilder()
                .osFamily(OsFamily.UBUNTU)
                .hardwareId(InstanceType.T2_MICRO)
                .locationId("eu-central-1")
                .options(AWSEC2TemplateOptions.Builder.keyPair("id_hesso_amazonws").inboundPorts(22, 80));
        
       Stack stack = Stack.load("../amazon-stack.yml", client);
       
       stack.run();
    }
}
