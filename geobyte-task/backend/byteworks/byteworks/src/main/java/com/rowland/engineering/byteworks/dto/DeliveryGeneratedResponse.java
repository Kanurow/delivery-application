package com.rowland.engineering.byteworks.dto;

import com.rowland.engineering.byteworks.model.Delivery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryGeneratedResponse {

    private Long id;
    private String cheapestRouteLocation;
    private Double cheapestRouteDistance;
    private Double cheapestRouteClearingCost;

    private String mostCostlyRouteLocation;
    private Double mostCostlyRouteDistance;
    private Double mostCostlyRouteClearingCost;

    private String pathLocation;
    private Double pathDistance;
    private Double pathClearingCost;

    private String destinationLocation;
    private Double destinationDistance;
    private Double destinationClearingCost;
    private String originLocation;
}
