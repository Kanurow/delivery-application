package com.rowland.engineering.byteworks.service;

import com.rowland.engineering.byteworks.dto.*;
import com.rowland.engineering.byteworks.exception.DeliveryNotFoundException;
import com.rowland.engineering.byteworks.model.Delivery;
import com.rowland.engineering.byteworks.model.DeliveryGenerated;
import com.rowland.engineering.byteworks.model.User;
import com.rowland.engineering.byteworks.repository.DeliveryGeneratedRepository;
import com.rowland.engineering.byteworks.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryGeneratedRepository deliveryGeneratedRepository;

    public ResponseEntity<String> createDelivery(CreateDeliveryRequest request) {
        Delivery newDelivery = Delivery.builder()
                .clearingCost(request.getClearingCost())
                .distance(request.getDistance())
                .location(request.getLocation())
                .build();
        Delivery savedDelivery = deliveryRepository.save(newDelivery);
        return ResponseEntity.status(HttpStatus.OK).body("Delivery with location name " +savedDelivery.getLocation() + " has been created");
    }
    public List<DeliveryResponse> getAllDeliveries() {
        List<Delivery> allDelivery = deliveryRepository.findAll();

        return allDelivery.stream().map(deliveries -> {
            DeliveryResponse deliveryResponse = new DeliveryResponse();
            deliveryResponse.setId(deliveries.getId());
            deliveryResponse.setDistance(deliveries.getDistance());
            deliveryResponse.setLocation(deliveries.getLocation());
            deliveryResponse.setClearingCost(deliveries.getClearingCost());

            return deliveryResponse;
        }).collect(Collectors.toList());
    }


    public ResponseEntity<String> updateDeliveryDetails(Long deliveryId,
                                                        String newLocation,
                                                        Double newClearingCost,
                                                        Double newDistance) {
        Delivery foundDelivery = deliveryRepository.getReferenceById(deliveryId);
        Delivery updatedDelivery = Delivery.builder()
                .id(foundDelivery.getId())
                .location(newLocation)
                .clearingCost(newClearingCost)
                .distance(newDistance)
                .build();

        Delivery updatedInfo = deliveryRepository.save(updatedDelivery);

        return ResponseEntity.status(HttpStatus.OK).body("Delivery with id " + foundDelivery.getId() + " has been updated to " +
                "location: "+ updatedInfo.getLocation() +
                " distance: "+ updatedInfo.getDistance()  +
                " and clearing cost: " + updatedInfo.getClearingCost());
    }

    public ApiResponse deleteDelivery(Long id) {
        deliveryRepository.deleteById(id);
        return new ApiResponse(true, "Delivery has been deleted");
    }

    public Delivery getDelivery(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found with ID: " + deliveryId));
    }


    @Transactional
    public ApiResponse generateDelivery(DeliveryRequest deliveryRequest) {
        List<Delivery> allDeliveries = deliveryRepository.findAll();
        Delivery origin = deliveryRepository.findByLocation(deliveryRequest.getOrigin());
        Delivery destination = deliveryRepository.findByLocation(deliveryRequest.getDestination());
        Delivery path = deliveryRepository.findByLocation(deliveryRequest.getPath());

        allDeliveries.remove(origin);
        allDeliveries.remove(destination);

        Delivery cheapestRoute = null;
        Delivery mostCostlyRoute = null;

        double minSum = Double.MAX_VALUE;
        double maxSum = Double.MIN_VALUE;

        double costPerKilometer = 1.0;

        for (Delivery delivery : allDeliveries) {
            double sum = delivery.getClearingCost() + costPerKilometer * delivery.getDistance();

            if (sum < minSum) {
                minSum = sum;
                cheapestRoute = delivery;
            }
            if (sum > maxSum) {
                maxSum = sum;
                mostCostlyRoute = delivery;
            }
        }

        DeliveryGenerated savedGeneratedDelivery = new DeliveryGenerated();
        savedGeneratedDelivery.setCheapestRouteLocation(cheapestRoute.getLocation());
        savedGeneratedDelivery.setCheapestRouteDistance(cheapestRoute.getDistance());
        savedGeneratedDelivery.setCheapestRouteClearingCost(cheapestRoute.getClearingCost());

        savedGeneratedDelivery.setMostCostlyRouteLocation(mostCostlyRoute.getLocation());
        savedGeneratedDelivery.setMostCostlyRouteDistance(mostCostlyRoute.getDistance());
        savedGeneratedDelivery.setMostCostlyRouteClearingCost(mostCostlyRoute.getClearingCost());

        savedGeneratedDelivery.setPathLocation(path.getLocation());
        savedGeneratedDelivery.setPathDistance(path.getDistance());
        savedGeneratedDelivery.setPathClearingCost(path.getClearingCost());

        savedGeneratedDelivery.setDestinationLocation(destination.getLocation());
        savedGeneratedDelivery.setDestinationDistance(destination.getDistance());
        savedGeneratedDelivery.setDestinationClearingCost(destination.getClearingCost());

        savedGeneratedDelivery.setOriginLocation(origin.getLocation());

        deliveryGeneratedRepository.save(savedGeneratedDelivery);
        return new ApiResponse(true, "Delivery route generated successfully");
    }

    public List<DeliveryGeneratedResponse> getAllGeneratedDeliveries() {
        List<DeliveryGenerated> allGeneratedDelivery = deliveryGeneratedRepository.findAll();

        return allGeneratedDelivery.stream().map(delivery -> {
            DeliveryGeneratedResponse deliveryGeneratedResponse = new DeliveryGeneratedResponse();
            deliveryGeneratedResponse.setId(delivery.getId());

            deliveryGeneratedResponse.setCheapestRouteLocation(delivery.getCheapestRouteLocation());
            deliveryGeneratedResponse.setCheapestRouteDistance(delivery.getCheapestRouteDistance());
            deliveryGeneratedResponse.setCheapestRouteClearingCost(delivery.getCheapestRouteClearingCost());

            deliveryGeneratedResponse.setMostCostlyRouteLocation(delivery.getMostCostlyRouteLocation());
            deliveryGeneratedResponse.setMostCostlyRouteDistance(delivery.getMostCostlyRouteDistance());
            deliveryGeneratedResponse.setMostCostlyRouteClearingCost(delivery.getMostCostlyRouteClearingCost());

            deliveryGeneratedResponse.setPathLocation(delivery.getPathLocation());
            deliveryGeneratedResponse.setPathDistance(delivery.getPathDistance());
            deliveryGeneratedResponse.setPathClearingCost(delivery.getPathClearingCost());

            deliveryGeneratedResponse.setDestinationLocation(delivery.getDestinationLocation());
            deliveryGeneratedResponse.setDestinationDistance(delivery.getDestinationDistance());
            deliveryGeneratedResponse.setDestinationClearingCost(delivery.getDestinationClearingCost());

            deliveryGeneratedResponse.setOriginLocation(delivery.getOriginLocation());

            return deliveryGeneratedResponse;
        }).collect(Collectors.toList());
    }
}
