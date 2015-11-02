package com.hesso.cloud.lab3.exercice2;

import java.io.IOException;
import org.jclouds.compute.RunNodesException;

public class RunInstances {

    public static void main(String[] args) throws RunNodesException, IOException {

        String provider = args[0];
        String identity = args[1];
        String key = args[2];

        // System.out.println("keypair : " + new File(keyPair).exists());


        Stack stack = StackFileReader.load("../amazon-stack.yml", new AmazonProvider(identity, key));

        System.out.println("Start instances ...");
        stack.run();

        char pressedChar;
        do {
            System.out.println("Press 'A' to abort : ");
            pressedChar = (char) System.in.read();
        } while (pressedChar != 'A');

        stack.end();
    }
}
