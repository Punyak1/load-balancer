package com.project.loadbalancer.controller;

import com.project.loadbalancer.service.LoadBalancerService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoadBalancerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean("random")
    private LoadBalancerService loadBalancerService;

    @Test
    public void testRegisterServer() throws Exception {
        doNothing().when(loadBalancerService).registerServer(ArgumentMatchers.anyString());
        mockMvc.perform(post("/api/register").param("serverUrl", "http://example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("Server registered: http://example.com"));
    }

    @Test
    public void testRemoveServer() throws Exception {
        mockMvc.perform(post("/api/remove").param("serverUrl", "http://example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("Server removed: http://example.com"));

    }

    @Test
    public void testSwitchAlgorithm() throws Exception {
        mockMvc.perform(post("/api/algorithm").param("algorithm", "random"))
                .andExpect(status().isOk())
                .andExpect(content().string("Switched to algorithm: random"));
    }

    @Test
    public void testForwardRequest() throws Exception {
        mockMvc.perform(get("/api/request"))
                .andExpect(status().is5xxServerError());
    }
}
