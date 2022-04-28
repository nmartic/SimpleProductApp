package com.simpleproductapp.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @Size(min = 10, max = 10)
    private String code;

    private String name;
    private BigDecimal priceHrk;
    private String description;
    private boolean available;
}
