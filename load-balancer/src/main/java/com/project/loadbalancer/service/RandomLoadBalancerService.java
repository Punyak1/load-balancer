package com.project.loadbalancer.service;

import com.project.loadbalancer.server.ServerInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

// Random Load Balancer Implementation
@Service
public class RandomLoadBalancerService implements LoadBalancerService {

    private final List<ServerInstance> servers = new CopyOnWriteArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(RandomLoadBalancerService.class);

    @Override
    public String forwardRequest() throws Exception {
        if (servers.isEmpty()) {
            throw new Exception("No available servers.");
        }

        ServerInstance selectedServer = selectServer();
        logger.info("Forwarding request to server: {}", selectedServer.getUrl());
        return sendRequestToServer(selectedServer);
    }

    @Override
    public void registerServer(String serverUrl) {
        synchronized (servers) {
            if (servers.stream().anyMatch(server -> server.getUrl().equals(serverUrl))) {
                throw new IllegalArgumentException("Server already registered: " + serverUrl);
            }
            servers.add(new ServerInstance(serverUrl));
            logger.info("Registered new server: {}", serverUrl);
        }
    }

    @Override
    public void removeServer(String serverUrl) {
        synchronized (servers) {
            if (servers.removeIf(server -> server.getUrl().equals(serverUrl))) {
                logger.info("Removed server: {}", serverUrl);
            } else {
                logger.warn("Attempted to remove non-existent server: {}", serverUrl);
            }
        }
    }

    @Override
    public List<String> getServerHealth() {
        return servers.stream().map(instance -> {
            boolean isHealthy = checkHealth(instance.getUrl());
            return instance.toString() + " - Healthy: " + isHealthy;
        }).collect(Collectors.toList());
    }

    private boolean checkHealth(String url) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForObject(url + "/health", String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getAlgorithmName() {
        return "random";
    }

    private ServerInstance selectServer() {
        return servers.get(new Random().nextInt(servers.size()));
    }

    private String sendRequestToServer(ServerInstance server) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.getForObject(server.getUrl(), String.class);
        } catch (Exception e) {
            logger.error("Failed to connect to server: {}", server.getUrl(), e);
            throw new Exception("Failed to connect to server: " + server.getUrl());
        }
    }
}