package com.example.exercise2.cargo.controller;

import com.example.exercise2.cargo.model.Container;
import com.example.exercise2.cargo.repository.ContainerRepository;
import com.example.exercise2.cargo.service.ContainerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContainerController.class)
public class ContainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContainerService containerService;

    @Test
    void test_given_containerNumber_OOCU129283_when_searched_should_return_container_with_containerNumber_OOCU129283() throws Exception {
        String containerNumber = "OOCU129283";

        mockFindByContainerNumber(containerBuilder(containerNumber));

        mockMvc.perform(get("/api/container/search")
                        .param("containerNumber", containerNumber))
                .andExpect(jsonPath("$[0].containerNumber").value("OOCU129283"))
                .andExpect(jsonPath("$[0].origin").value("Manila"))
                .andExpect(jsonPath("$[0].destination").value("Cebu"))
                .andExpect(jsonPath("$[0].weight").value(22.2))
                .andExpect(status().isOk());

        verify(containerService, times(1)).findByContainerNumber(anyString());
    }

    @Test
    void test_given_incomplete_containerNumber_OOC_when_searched_should_return_multiple_containerNumber_that_contains_OOCU() throws Exception {
        mockFindByContainerNumber(
                containerBuilder("OOCU129283"),
                containerBuilder("OOCS788596"),
                containerBuilder("OOCS788516")
        );

        mockMvc.perform(get("/api/container/search")
                        .param("containerNumber", "OOC"))
                .andExpect(jsonPath("$[0].containerNumber").value("OOCU129283"))
                .andExpect(jsonPath("$[1].containerNumber").value("OOCS788596"))
                .andExpect(jsonPath("$[2].containerNumber").value("OOCS788516"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(status().isOk());

        verify(containerService, times(1)).findByContainerNumber(anyString());
    }

    @Test
    void test_given_non_existing_containerNumber_OOCU654_when_searched_should_return_empty_array() throws Exception {
        when(containerService.findByContainerNumber(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/container/search")
                        .param("containerNumber", "OOCU654"))
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(status().isOk());

        verify(containerService, times(1)).findByContainerNumber(anyString());
    }

    public void mockFindByContainerNumber(Container... container) {
        when(containerService.findByContainerNumber(anyString())).thenReturn(List.of(container));
    }

    public Container containerBuilder(String containerNumber) {
        return new Container(1L, containerNumber, "Manila", "Cebu", 22.2);
    }
}