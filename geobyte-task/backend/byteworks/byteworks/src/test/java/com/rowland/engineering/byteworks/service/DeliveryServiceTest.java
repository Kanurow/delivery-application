package com.rowland.engineering.byteworks.service;

import com.rowland.engineering.byteworks.dto.*;
import com.rowland.engineering.byteworks.model.Delivery;
import com.rowland.engineering.byteworks.model.DeliveryGenerated;
import com.rowland.engineering.byteworks.repository.DeliveryGeneratedRepository;
import com.rowland.engineering.byteworks.repository.DeliveryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private DeliveryGeneratedRepository deliveryGeneratedRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    @Captor
    private ArgumentCaptor<Delivery> deliveryCaptor;

    @Test
    @DisplayName("Should create a new delivery route with right arguments")
    void createDelivery() {
        CreateDeliveryRequest createDeliveryRequest = new CreateDeliveryRequest("Abuja", 26.0,70.6);

        Delivery savedDelivery = Delivery.builder()
                .id(1L)
                .location(createDeliveryRequest.getLocation())
                .clearingCost(createDeliveryRequest.getClearingCost())
                .distance(createDeliveryRequest.getDistance())
                .build();
        when(deliveryRepository.save(ArgumentMatchers.any(Delivery.class))).thenReturn(savedDelivery);

        ResponseEntity<String> response = deliveryService.createDelivery(createDeliveryRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Delivery with ID: " + savedDelivery.getId() +
                " has been created successfully with location name " + savedDelivery.getLocation()));

        verify(deliveryRepository, times(1)).save(argThat(
                delivery -> delivery.getLocation().equals(createDeliveryRequest.getLocation())
                        && delivery.getClearingCost().equals(createDeliveryRequest.getClearingCost())
                        && delivery.getDistance().equals(createDeliveryRequest.getDistance())
        ));
    }

    @Test
    void updateDeliveryDetails() {
        // Arrange
        Long deliveryId = 1L;
        String newLocation = "New Location";
        Double newClearingCost = 50.0;
        Double newDistance = 75.0;

        Delivery foundDelivery = new Delivery(1L,"Old Location", 30.0, 50.0);
        foundDelivery.setId(deliveryId);

        Delivery updatedDelivery = Delivery.builder()
                .id(deliveryId)
                .location(newLocation)
                .clearingCost(newClearingCost)
                .distance(newDistance)
                .build();

        when(deliveryRepository.getReferenceById(deliveryId)).thenReturn(foundDelivery);
        when(deliveryRepository.save(updatedDelivery)).thenReturn(updatedDelivery);

        // Act
        ResponseEntity<String> response = deliveryService.updateDeliveryDetails(deliveryId, newLocation, newClearingCost, newDistance);

        // Assert
        verify(deliveryRepository, times(1)).getReferenceById(deliveryId);
        verify(deliveryRepository, times(1)).save(updatedDelivery);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Delivery with id 1 has been updated to location: New Location distance: 75.0 and clearing cost: 50.0", response.getBody());
    }

    @Test
    @DisplayName("Returns all created delivery in database")
    void getAllDeliveries() {
        List<Delivery> mockDeliveries = Arrays.asList(
                new Delivery(1L, "Lagos", 40.0, 80.0),
                new Delivery(2L, "Abuja", 45.0, 40.0)
        );
        when(deliveryRepository.findAll()).thenReturn(mockDeliveries);

        List<DeliveryResponse> result = deliveryService.getAllDeliveries();

        assertNotNull(result);
        assertEquals(mockDeliveries.size(), result.size());

        for (int i = 0; i < mockDeliveries.size(); i++) {
            DeliveryResponse response = result.get(i);
            Delivery mockDelivery = mockDeliveries.get(i);

            assertEquals(mockDelivery.getId(), response.getId());
            assertEquals(mockDelivery.getLocation(), response.getLocation());
            assertEquals(mockDelivery.getDistance(), response.getDistance());
            assertEquals(mockDelivery.getClearingCost(), response.getClearingCost());
        }

        verify(deliveryRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Successfully deletes delivery from database")
    void deleteDelivery() {
        doNothing().when(deliveryRepository).deleteById(anyLong());

        ApiResponse result = deliveryService.deleteDelivery(1L);

        assertNotNull(result);
        assertEquals("Delivery has been deleted", result.getMessage());

        verify(deliveryRepository, times(1)).deleteById(eq(1L));
    }

    @Test
    @DisplayName("Test the positive case of where product was found")
    void getDeliveryFound() {
        Delivery mockDelivery = new Delivery();
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(mockDelivery));

        Optional<Delivery> result = deliveryService.getDelivery(1L);

        assertTrue(result.isPresent());
        assertEquals(mockDelivery, result.get());

        verify(deliveryRepository, times(1)).findById(eq(1L));
    }

    @Test
    @DisplayName("Test the negative cast of where delivery is not found")
    void getDeliveryNotFound() {
        when(deliveryRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Delivery> result = deliveryService.getDelivery(1L);

        assertTrue(result.isEmpty());

        verify(deliveryRepository, times(1)).findById(eq(1L));
    }

    @Test
    void generateDelivery() {
        // Arrange
        Delivery origin = new Delivery(1L,"Abuja", 40.3, 53.0);
        Delivery destination = new Delivery(2L,"Abia", 59.3, 21.0);
        Delivery path = new Delivery(3L,"Lagos", 34.3, 34.0);

        List<Delivery> allDeliveries = new ArrayList<>();
        allDeliveries.add(origin);
        allDeliveries.add(destination);
        allDeliveries.add(path);

        when(deliveryRepository.findAll()).thenReturn(allDeliveries);
        when(deliveryRepository.findByLocation("Abuja")).thenReturn(origin);
        when(deliveryRepository.findByLocation("Abia")).thenReturn(destination);
        when(deliveryRepository.findByLocation("Lagos")).thenReturn(path);

        ApiResponse response = deliveryService.generateDelivery(createDeliveryRequest("Abuja", "Abia", "Lagos"));

        assertNotNull(response);
        assertEquals("Delivery route generated successfully", response.getMessage());
        verify(deliveryGeneratedRepository, times(1)).save(ArgumentMatchers.any(DeliveryGenerated.class));
    }

    private DeliveryRequest createDeliveryRequest(String origin, String destination, String path) {
        DeliveryRequest deliveryRequest = new DeliveryRequest();
        deliveryRequest.setOrigin(origin);
        deliveryRequest.setDestination(destination);
        deliveryRequest.setPath(path);
        return deliveryRequest;
    }








    @Test
    @DisplayName("Return all generated deliveries")
    public void getAllGeneratedDeliveries() {
        // Create an instance of DeliveryGenerated
        DeliveryGenerated deliveryGenerated1 = new DeliveryGenerated();
        deliveryGenerated1.setId(1L);
        deliveryGenerated1.setCheapestRouteLocation("Abuja");
        deliveryGenerated1.setCheapestRouteDistance(38.0);
        deliveryGenerated1.setCheapestRouteClearingCost(50.0);
        deliveryGenerated1.setMostCostlyRouteLocation("Lagos");
        deliveryGenerated1.setMostCostlyRouteDistance(20.0);
        deliveryGenerated1.setMostCostlyRouteClearingCost(70.0);
        deliveryGenerated1.setPathLocation("Enugu");
        deliveryGenerated1.setPathDistance(15.0);
        deliveryGenerated1.setPathClearingCost(60.0);
        deliveryGenerated1.setDestinationLocation("DestinationLocation");
        deliveryGenerated1.setDestinationDistance(25.0);
        deliveryGenerated1.setDestinationClearingCost(80.0);
        deliveryGenerated1.setOriginLocation("OriginLocation");

        List<DeliveryGenerated> mockDeliveryGenerated = new ArrayList<>();
        mockDeliveryGenerated.add(deliveryGenerated1);


        when(deliveryGeneratedRepository.findAll()).thenReturn(mockDeliveryGenerated);

        List<DeliveryGeneratedResponse> generatedDeliveries = deliveryService.getAllGeneratedDeliveries();

        assertEquals(mockDeliveryGenerated.size(), generatedDeliveries.size());
        assertEquals(mockDeliveryGenerated.get(0).getId(), generatedDeliveries.get(0).getId());
        assertEquals(mockDeliveryGenerated.get(0).getCheapestRouteClearingCost(), generatedDeliveries.get(0).getCheapestRouteClearingCost());
        assertEquals(mockDeliveryGenerated.get(0).getDestinationClearingCost(), generatedDeliveries.get(0).getDestinationClearingCost());
        assertEquals(mockDeliveryGenerated.get(0).getMostCostlyRouteClearingCost(), generatedDeliveries.get(0).getMostCostlyRouteClearingCost());
    }
}