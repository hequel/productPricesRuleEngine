package com.visiolending.productPricesRuleEngine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceRequest {

    private double credit_score;
    private String state;
    private String productName;

}
