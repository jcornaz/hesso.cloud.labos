/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hesso.cloud.lab3.exercice2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.TemplateBuilder;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author jonathan
 */
public class Stack {

    public static Stack load(String yamlFile, ComputeService client) throws IOException {
        FileReader reader = null;
        Map<String, Object> config = null;
                
        try {
            reader = new FileReader(new File(yamlFile));
            config = (Map<String, Object>) new Yaml().load(reader);
            reader.close();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
            }
        }
        
        String mongoImageId = ((Map<String, String>) config.get("mongodb")).get("image_id");
        String restClientImageId = ((Map<String, String>) config.get("rest_client")).get("image_id");
        String restServerImageId = ((Map<String, String>) config.get("rest_server")).get("image_id");
        
        Image mongoImage = null;
        Image restClientImage = null;
        Image restServerImage = null;
        
        for( Image image : client.listImages() )
        {
            if( image.getId().equals(mongoImageId))
            {
                mongoImage = image;
            } else if( image.getId().equals(restClientImageId))
            {
                restClientImage = image;
            } else if( image.getId().equals(restServerImageId))
            {
                restServerImage = image;
            }
            
            if( restServerImage != null && restClientImage != null && mongoImage != null)
            {
                break;
            }
        }
        
        MongoDB mongo = null;
        RESTClient restClient = null;
        RESTServer restServer = null;

        return new Stack(mongo, restClient, restServer);
    }
    private final MongoDB mongo;
    private final RESTClient restClient;
    private final RESTServer restServer;

    public Stack(MongoDB mongo, RESTClient restClient, RESTServer restServer) {
        this.mongo = mongo;
        this.restClient = restClient;
        this.restServer = restServer;
    }

    public void run() {
        // TODO
    }
}
