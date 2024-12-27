package com.project.loadbalancer.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Factory to provide appropriate LoadBalancerService
@Component
public class LoadBalancerServiceFactory {

    private final Map<String, LoadBalancerService> services;

    public LoadBalancerServiceFactory(List<LoadBalancerService> services) {
        this.services = services.stream().collect(Collectors.toMap(LoadBalancerService::getAlgorithmName, s -> s));
    }

    public LoadBalancerService getService(String algorithm) {
        return services.getOrDefault(algorithm, services.get("round-robin"));
    }
}
