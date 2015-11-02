package com.hesso.cloud.lab3.exercice2;

import com.hesso.cloud.lab3.exercice2.amazon.AmazonProvider;
import com.hesso.cloud.lab3.exercice2.model.Stack;
import com.hesso.cloud.lab3.exercice2.switchengine.SwitchEngineProvider;
import java.io.IOException;
import org.jclouds.compute.RunNodesException;

public class RunInstances {

    public static void main(String[] args) throws RunNodesException, IOException {

        String provider = args[0];
        String identity = args[1];
        String key = args[2];

        Stack stack = null;
        if (provider.equalsIgnoreCase("switch")) {
            stack = StackFileReader.load("../amazon-stack.yml", new AmazonProvider(identity, key));
        } else if (provider.equalsIgnoreCase("amazon")) {
            stack = StackFileReader.load("../switch-engine-stack.yml", new SwitchEngineProvider(identity, key));
        } else {
            System.err.println("not supported provider : " + provider);
        }

        if (stack != null) {
            System.out.println("Start instances ...");
            stack.run();

            char pressedChar;
            do {
                System.out.println("Press 'A' to abort : ");
                pressedChar = (char) System.in.read();
            } while (pressedChar != 'A');
        }
    }
}
