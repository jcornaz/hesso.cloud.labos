package com.hesso.cloud.lab3.exercice2;

import java.util.Set;

public interface CloudNode {

    public void destroy();

    public Set<String> getPrivateAddresses();

    public Set<String> getPublicAddresses();

    public void execute(String string);

    public void attachPublicIP(String publicIP);

    public void detachPublicIP(String ip);

}
