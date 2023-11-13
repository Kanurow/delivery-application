package com.rowland.engineering.byteworks.model;

import jakarta.persistence.*;

import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String location;

    @Positive
    @Min(value = 25, message = "Clearing Cost should not be less than $25")
    @Max(value = 100, message = "Clearing Cost should not be more than $100")
    private Double clearingCost;

    @Positive
    private Double distance;
}




