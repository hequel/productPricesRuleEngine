package com.lending.productPricesRuleEngine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceRequest {

    @NotNull
    private double credit_score;

    @NotNull
    private String state;

    @NotNull
    private String productName;

}
