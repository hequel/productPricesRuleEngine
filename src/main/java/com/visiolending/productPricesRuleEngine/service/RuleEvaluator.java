package com.visiolending.productPricesRuleEngine.service;

import com.visiolending.productPricesRuleEngine.model.Person;
import com.visiolending.productPricesRuleEngine.model.PriceRequest;
import com.visiolending.productPricesRuleEngine.model.Product;
import com.visiolending.productPricesRuleEngine.model.Result;

public interface RuleEvaluator {

    boolean matches(PriceRequest priceRequest, String action, double rateDiscount, double creditScoreRule, String  creditScoreMathSymbolRule, String personStateRule, String productNameRule);
    Result evaluate(PriceRequest priceRequest, String action, double rateDiscount, double creditScoreRule, String  creditScoreMathSymbolRule, String personStateRule, String productNameRule);
}
