package com.lending.productPricesRuleEngine.service;

import com.lending.productPricesRuleEngine.RulesConstant;
import com.lending.productPricesRuleEngine.model.PriceRequest;
import com.lending.productPricesRuleEngine.model.Result;

public class ProductNameIncreaseRateRule implements RuleEvaluator {
    Result result = new Result();

    @Override
    public boolean matches(PriceRequest priceRequest, String action, double rateDiscount, double creditScoreRule, String creditScoreMathSymbolRule, String personStateRule, String productNameRule) {
        return (priceRequest.getProductName().equalsIgnoreCase(RulesConstant.INCREASED_RATE_PRODUCT_NAME)
                || priceRequest.getProductName().equalsIgnoreCase(productNameRule)
                && action.equalsIgnoreCase(RulesConstant.INCREASE_RATE));
    }

    @Override
    public Result evaluate(PriceRequest priceRequest, String action, double rateDiscount, double creditScoreRule, String creditScoreMathSymbolRule, String personStateRule, String productNameRule) {
        if (priceRequest.getProductName().equalsIgnoreCase(RulesConstant.INCREASED_RATE_PRODUCT_NAME)) {
            result.setInterest_rate(RulesConstant.START_INTEREST_RATE + RulesConstant.PRODUCT_NAME_INCREASED_RATE);
            result.setDisqualified(false);

        }

        if (productNameRule != null && priceRequest.getProductName().equalsIgnoreCase(productNameRule) && action.equalsIgnoreCase(RulesConstant.INCREASE_RATE)) {
            result.setInterest_rate(RulesConstant.START_INTEREST_RATE + rateDiscount);
            result.setDisqualified(false);
            result.setMatchLodedRule(true);

        }

        if (priceRequest.getState().equalsIgnoreCase(RulesConstant.DISQUALIFIED_STATE))
            result.setDisqualified(true);


        if (personStateRule != null && priceRequest.getState().equalsIgnoreCase(personStateRule) && action.equalsIgnoreCase(RulesConstant.DISQUALIFY))
            result.setDisqualified(true);


        return result;
    }
}
