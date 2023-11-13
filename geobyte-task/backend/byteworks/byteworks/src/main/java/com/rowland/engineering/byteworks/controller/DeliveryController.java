package com.rowland.engineering.byteworks.controller;


import com.rowland.engineering.byteworks.model.Delivery;
import com.rowland.engineering.byteworks.service.DeliveryService;
import com.rowland.engineering.byteworks.dto.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;




//    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/create")
    public ResponseEntity<String> createDelivery(@Valid
                                                 @RequestBody CreateDeliveryRequest deliveryRequest) {
        return deliveryService.createDelivery(deliveryRequest);
    }

//    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse> generateDelivery(
            @RequestBody DeliveryRequest deliveryRequest) {
        return new ResponseEntity<ApiResponse>(deliveryService.generateDelivery(deliveryRequest),HttpStatus.ACCEPTED);
    }


//    @ResponseStatus(value = HttpStatus.FOUND)
    @GetMapping("/getAllDeliveries")
    public List<DeliveryResponse> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }

//    @ResponseStatus(value = HttpStatus.FOUND)
    @GetMapping("/getAllGeneratedDeliveries")
    public List<DeliveryGeneratedResponse> getAllGeneratedDeliveries() {
        return deliveryService.getAllGeneratedDeliveries();
    }

//    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @DeleteMapping("/deleteDelivery/{id}")
    public ResponseEntity<ApiResponse> deleteDelivery(
            @PathVariable(value = "id") Long id) {
        return new ResponseEntity<ApiResponse>(deliveryService.deleteDelivery(id),HttpStatus.ACCEPTED);
    }

//    @ResponseStatus(value = HttpStatus.FOUND)
    @GetMapping("/view/{deliveryId}")
    public ResponseEntity<Optional<Delivery>> getDelivery(@PathVariable Long deliveryId) {
        Optional<Delivery> delivery = deliveryService.getDelivery(deliveryId);
        System.out.println("DELIVERY DELIVERY"+delivery);
        return ResponseEntity.status(HttpStatus.FOUND).body(delivery);
    }


//    @ResponseStatus(value = HttpStatus.CREATED)
    @PutMapping("/updateDeliveryInformation/{id}")
    public ResponseEntity<String> updateDeliveryInformation(@RequestParam("location") String location,
                                                        @RequestParam("clearingCost") Double clearingCost,
                                                        @RequestParam("distance") Double distance,
                                                            @PathVariable Long id) {
        return deliveryService.updateDeliveryDetails(id,  location,  clearingCost, distance);

    }
}
