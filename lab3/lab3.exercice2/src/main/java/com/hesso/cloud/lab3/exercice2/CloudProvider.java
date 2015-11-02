package com.hesso.cloud.lab3.exercice2;

import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.Image;

public interface CloudProvider {

    public CloudNode createNode(String imageID, String name) throws RunNodesException;

    public Iterable<? extends Image> listImages();
    
}
