package com.project.loadbalancer.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
public class RoundRobinLoadBalancerServiceTest {

    @Autowired
    private RoundRobinLoadBalancerService roundRobinLoadBalancerService;

    @Test
    public void testRegisterServer() {
        String serverUrl = "http://www.example1.com";
        roundRobinLoadBalancerService.registerServer(serverUrl);
        Assertions.assertTrue(roundRobinLoadBalancerService.getServerHealth().stream()
                .anyMatch(s -> s.equals("ServerInstance{url='http://www.example1.com'}" + " - Healthy: false")));
    }

    @Test
    public void testRemoveServer() {
        String serverUrl = "http://www.example2.com";
        roundRobinLoadBalancerService.registerServer(serverUrl);
        Assertions.assertTrue(roundRobinLoadBalancerService.getServerHealth().stream()
                .anyMatch(s -> s.equals( "ServerInstance{url='http://www.example2.com'}" + " - Healthy: false")));
        roundRobinLoadBalancerService.removeServer(serverUrl);
        Assertions.assertFalse(roundRobinLoadBalancerService.getServerHealth().stream()
                .anyMatch(s -> s.equals("ServerInstance{url='http://www.example2.com'}" + " - Healthy: false")));
    }

    @Test
    public void testForwardRequest() throws Exception {
        assertThrows(Exception.class,()->roundRobinLoadBalancerService.forwardRequest());

    }

}
