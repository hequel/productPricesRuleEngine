package com.lending.productPricesRuleEngine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RulesDefinition {

    private String action;
    private String parameter;
    private List<ActionTrigger> actionTtriggers;

}
