package com.hesso.cloud.lab3.exercice2;

import com.hesso.cloud.lab3.exercice2.model.MongoDB;
import com.hesso.cloud.lab3.exercice2.model.RESTServer;
import com.hesso.cloud.lab3.exercice2.model.Stack;
import com.hesso.cloud.lab3.exercice2.model.RESTClient;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class StackFileReader {

    public static Stack load(String yamlFile, CloudProvider provider) throws IOException {
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

        provider.setNetworkID((String) config.get("net_id"));
        String mongoImageId = ((Map<String, String>) config.get("mongodb")).get("image_id");
        String restClientImageId = ((Map<String, String>) config.get("rest_client")).get("image_id");
        String restServerImageId = ((Map<String, String>) config.get("rest_server")).get("image_id");
        String restClientPublicIP = ((Map<String, String>) config.get("rest_client")).get("public_ip");
        String restServerPublicIP = ((Map<String, String>) config.get("rest_server")).get("public_ip");

        MongoDB mongo = new MongoDB(provider, mongoImageId);
        RESTClient restClient = new RESTClient(provider, restClientImageId, restClientPublicIP );
        RESTServer restServer = new RESTServer(provider, restServerImageId, restServerPublicIP);
        
        return new Stack(mongo, restClient, restServer);
    }
}
