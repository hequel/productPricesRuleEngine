package com.visiolending.productPricesRuleEngine.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ActionTrigger {
    private String productName;
    private String personCreditSCore;
    private String personState;
    private String personCreditSCoreComparisonOperator;

}
