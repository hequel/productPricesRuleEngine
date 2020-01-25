package com.visiolending.productPricesRuleEngine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rules {

    private List<RulesDefinition> rules;
}
