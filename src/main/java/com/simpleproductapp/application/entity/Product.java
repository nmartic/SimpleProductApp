package com.simpleproductapp.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    @Min(value=0)
    private BigDecimal priceHrk;

    @Min(value=0)
    private BigDecimal priceEur;

    private String description;

    private boolean isAvailable;

    private Timestamp created;

    private Timestamp updated;

    private Timestamp deleted;
}
