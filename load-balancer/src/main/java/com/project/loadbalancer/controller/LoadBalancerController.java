package com.project.loadbalancer.controller;

import com.project.loadbalancer.service.LoadBalancerService;
import com.project.loadbalancer.service.LoadBalancerServiceFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LoadBalancerController {

    private final LoadBalancerServiceFactory serviceFactory;
    private LoadBalancerService loadBalancerService;

    public LoadBalancerController(LoadBalancerServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        this.loadBalancerService = serviceFactory.getService("round-robin"); // Default algorithm
    }

    @GetMapping("/request")
    public ResponseEntity<String> handleRequest() {
        try {
            String response = loadBalancerService.forwardRequest();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerServer(@RequestParam String serverUrl) {
        try {
            loadBalancerService.registerServer(serverUrl);
            return ResponseEntity.ok("Server registered: " + serverUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeServer(@RequestParam String serverUrl) {
        loadBalancerService.removeServer(serverUrl);
        return ResponseEntity.ok("Server removed: " + serverUrl);
    }

    @GetMapping("/health")
    public ResponseEntity<List<String>> getHealthStatus() {
        return ResponseEntity.ok(loadBalancerService.getServerHealth());
    }

    @PostMapping("/algorithm")
    public ResponseEntity<String> switchAlgorithm(@RequestParam String algorithm) {
        this.loadBalancerService = serviceFactory.getService(algorithm);
        return ResponseEntity.ok("Switched to algorithm: " + algorithm);
    }
}
