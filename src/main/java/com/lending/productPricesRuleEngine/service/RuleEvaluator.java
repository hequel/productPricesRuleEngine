package com.lending.productPricesRuleEngine.service;

import com.lending.productPricesRuleEngine.model.PriceRequest;
import com.lending.productPricesRuleEngine.model.Result;

public interface RuleEvaluator {

    boolean matches(PriceRequest priceRequest, String action, double rateDiscount, double creditScoreRule, String  creditScoreMathSymbolRule, String personStateRule, String productNameRule);
    Result evaluate(PriceRequest priceRequest, String action, double rateDiscount, double creditScoreRule, String  creditScoreMathSymbolRule, String personStateRule, String productNameRule);
}
