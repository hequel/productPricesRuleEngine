package com.visiolending.productPricesRuleEngine.service;

import com.visiolending.productPricesRuleEngine.model.PriceRequest;
import com.visiolending.productPricesRuleEngine.model.Result;

import static com.visiolending.productPricesRuleEngine.service.RulesEngine.*;

public class ProductNameIncreaseRateRule implements RuleEvaluator {
    Result result;
    @Override
    public boolean evaluate(PriceRequest priceRequest, String action, double rateDiscount, double creditScoreRule, String creditScoreMathSymbolRule, String personStateRule, String productNameRule) {
            result=null;
        if (priceRequest.getProductName().equalsIgnoreCase(INCREASED_RATE_PRODUCT_NAME)) {
            result = new Result();
            result.setInterest_rate(START_INTEREST_RATE + PRODUCT_NAME_INCREASED_RATE);
            result.setDisqualified(false);
            result.setMatch(true);

        }

        if (productNameRule != null) {
            if (priceRequest.getProductName().equalsIgnoreCase(productNameRule) && action.equalsIgnoreCase(INCREASE_RATE)) {
                result.setInterest_rate(START_INTEREST_RATE + rateDiscount);
                result.setDisqualified(false);
                result.setMatch(true);
                result.setMatchLodedRule(true);

            }
        }

        if (result != null) {
            if (priceRequest.getState().equalsIgnoreCase(DISQUALIFIED_STATE)) {
                result.setDisqualified(true);
                result.setMatch(true);
            }

            if (personStateRule != null) {
                if (priceRequest.getState().equalsIgnoreCase(personStateRule) && action.equalsIgnoreCase(DISQUALIFY)) {
                    result.setDisqualified(true);
                    result.setMatch(true);
                    result.setMatchLodedRule(true);
                }

            }
        }

        if (result == null) {
            result = new Result();
            result.setMatch(false);
        }
        return result.isMatch();
    }

    @Override
    public Result getResult() {
        return result;
    }
}
