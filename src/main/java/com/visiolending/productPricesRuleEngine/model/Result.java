package com.visiolending.productPricesRuleEngine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private Double interest_rate;
    private boolean disqualified;
    private boolean match;

}
