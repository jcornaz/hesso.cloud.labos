package com.hesso.cloud.lab3.exercice2;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.Template;
import org.yaml.snakeyaml.Yaml;

public class StackFileReader {

    public static Stack load(String yamlFile, ComputeService client, Template template) throws IOException {
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
        String restClientPublicIP = ((Map<String, String>) config.get("rest_client")).get("public_ip");
        String restServerPublicIP = ((Map<String, String>) config.get("rest_server")).get("public_ip");

        MongoDB mongo = null;
        RESTClient restClient = null;
        RESTServer restServer = null;

        for (Image image : client.listImages()) {
            if (image.getId().equals(mongoImageId)) {
                mongo = new MongoDB(client, template, image);
            } else if (image.getId().equals(restClientImageId)) {
                restClient = new RESTClient(client, template, image, restClientPublicIP );
            } else if (image.getId().equals(restServerImageId)) {
                restServer = new RESTServer(client, template, image, restServerPublicIP);
            }

            if (mongo != null && restClient != null && restServer != null) {
                break;
            }
        }

        if (mongo == null) {
            throw new RuntimeException("Image for mongodb not found : " + mongoImageId);
        }
        if (restClient == null) {
            throw new RuntimeException("Image for the rest client not found : " + restClientImageId);
        }
        if (restServer == null) {
            throw new RuntimeException("Image for the rest server not found : " + restServerImageId);
        }

        return new Stack(mongo, restClient, restServer);
    }
}
