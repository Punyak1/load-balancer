package com.project.loadbalancer.service;

import java.util.List;

public interface LoadBalancerService {
    String forwardRequest() throws Exception;

    void registerServer(String serverUrl);

    void removeServer(String serverUrl);

    List<String> getServerHealth();

    String getAlgorithmName();
}