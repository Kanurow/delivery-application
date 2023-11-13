package com.rowland.engineering.byteworks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deliveries_generated")
public class DeliveryGenerated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
