package com.rowland.engineering.byteworks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDeliveryRequest {
    private String location;
    private Double distance;
    private Double clearingCost;
}
