package com.visiolending.productPricesRuleEngine.service;

import com.visiolending.productPricesRuleEngine.model.PriceRequest;
import com.visiolending.productPricesRuleEngine.model.Result;

import static com.visiolending.productPricesRuleEngine.RulesConstant.*;

public class ProductNameIncreaseRateRule implements RuleEvaluator {
    Result result = new Result();

    @Override
    public boolean matches(PriceRequest priceRequest, String action, double rateDiscount, double creditScoreRule, String creditScoreMathSymbolRule, String personStateRule, String productNameRule) {
        return (priceRequest.getProductName().equalsIgnoreCase(INCREASED_RATE_PRODUCT_NAME)
                || priceRequest.getProductName().equalsIgnoreCase(productNameRule)
                && action.equalsIgnoreCase(INCREASE_RATE));
    }

    @Override
    public Result evaluate(PriceRequest priceRequest, String action, double rateDiscount, double creditScoreRule, String creditScoreMathSymbolRule, String personStateRule, String productNameRule) {
        if (priceRequest.getProductName().equalsIgnoreCase(INCREASED_RATE_PRODUCT_NAME)) {
            result.setInterest_rate(START_INTEREST_RATE + PRODUCT_NAME_INCREASED_RATE);
            result.setDisqualified(false);

        }

        if (productNameRule != null && priceRequest.getProductName().equalsIgnoreCase(productNameRule) && action.equalsIgnoreCase(INCREASE_RATE)) {
            result.setInterest_rate(START_INTEREST_RATE + rateDiscount);
            result.setDisqualified(false);
            result.setMatchLodedRule(true);

        }

        if (priceRequest.getState().equalsIgnoreCase(DISQUALIFIED_STATE))
            result.setDisqualified(true);


        if (personStateRule != null && priceRequest.getState().equalsIgnoreCase(personStateRule) && action.equalsIgnoreCase(DISQUALIFY))
            result.setDisqualified(true);


        return result;
    }
}
