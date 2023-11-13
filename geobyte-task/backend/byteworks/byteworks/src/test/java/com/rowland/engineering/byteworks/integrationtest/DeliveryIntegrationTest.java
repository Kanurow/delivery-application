package com.rowland.engineering.byteworks.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rowland.engineering.byteworks.dto.CreateDeliveryRequest;
import com.rowland.engineering.byteworks.dto.DeliveryRequest;
import com.rowland.engineering.byteworks.repository.DeliveryRepository;
import com.rowland.engineering.byteworks.service.DeliveryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DeliveryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private DeliveryRepository deliveryRepository;

    @MockBean
    private DeliveryService deliveryService;




    @Test
    @WithMockUser(username = "rowland", password = "rowland", roles = "STAFF")
    @DisplayName("Should return the appropriate HTTP status response ")
    void createDelivery_shouldReturnCreated() throws Exception {
        CreateDeliveryRequest request = new CreateDeliveryRequest();
        request.setLocation("Lagos");
        request.setClearingCost(30.0);
        request.setDistance(40.0);

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/delivery/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "rowland", password = "rowland", roles = "STAFF")
    @DisplayName("Should return the appropriate HTTP status response")
    void generateDelivery_shouldReturnAccepted() throws Exception {
        // Create a sample DeliveryRequest
        DeliveryRequest deliveryRequest = new DeliveryRequest();
        deliveryRequest.setOrigin("Lagos");
        deliveryRequest.setDestination("Abuja");
        deliveryRequest.setPath("Kogi");

        String requestBody = objectMapper.writeValueAsString(deliveryRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/delivery/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isAccepted());
    }


    @Test
    @WithMockUser(username = "rowland", password = "rowland", roles = "STAFF")
    @DisplayName("Should return the appropriate HTTP status response(FOUND)")
    void getAllDeliveries_shouldReturnFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery/getAllDeliveries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    @WithMockUser(username = "rowland", password = "rowland", roles = "STAFF")
    @DisplayName("Should return an array of generated deliveries")
    void getAllGeneratedDeliveries_shouldReturnFound() throws Exception {
        // Perform a GET request to the endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery/getAllGeneratedDeliveries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "rowland", password = "rowland", roles = "STAFF")
    @DisplayName("Should delete delivery by ID provided")
    void deleteDelivery_shouldReturnAccepted() throws Exception {
        Long deliveryIdToDelete = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/delivery/deleteDelivery/{id}", deliveryIdToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    @WithMockUser(username = "rowland", password = "rowland", roles = "STAFF")
    void getDelivery_shouldReturnFound() throws Exception {
        Long deliveryId = 1L;

        // Perform a GET request to the endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery/view/{deliveryId}", deliveryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }


    @Test
    @WithMockUser(username = "rowland", password = "rowland", roles = "STAFF")
    void updateDeliveryInformation_shouldReturnOk() throws Exception {
        Long deliveryIdToUpdate = 2L;

        String location = "Updated Location";
        Double clearingCost = 25.0;
        Double distance = 60.0;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/delivery/updateDeliveryInformation/{id}", deliveryIdToUpdate)
                        .param("location", location)
                        .param("clearingCost", String.valueOf(clearingCost))
                        .param("distance", String.valueOf(distance))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }



}
