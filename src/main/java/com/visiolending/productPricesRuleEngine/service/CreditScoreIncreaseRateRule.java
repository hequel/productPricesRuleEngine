package com.visiolending.productPricesRuleEngine.service;

import com.visiolending.productPricesRuleEngine.model.PriceRequest;
import com.visiolending.productPricesRuleEngine.model.Result;

import java.math.BigDecimal;

import static com.visiolending.productPricesRuleEngine.service.RulesEngine.*;

public class CreditScoreIncreaseRateRule implements RuleEvaluator {
    Result result = new Result();

    @Override
    public boolean evaluate(PriceRequest priceRequest, String action, double rateDiscount, double creditScoreRule, String creditScoreMathSymbolRule, String personStateRule, String productNameRule) {

        if (priceRequest.getCredit_score() < CURRENT_CREDIT_SCORE_BAR) {
            result.setInterest_rate(START_INTEREST_RATE + PRODUCT_NAME_INCREASED_RATE);
            result.setDisqualified(false);
            result.setMatch(true);

            if (priceRequest.getProductName().equalsIgnoreCase(INCREASED_RATE_PRODUCT_NAME)) {
                result.setInterest_rate(result.getInterest_rate() + PRODUCT_NAME_INCREASED_RATE);

            }

            if (productNameRule != null) {
                if (priceRequest.getProductName().equalsIgnoreCase(productNameRule) && action.equalsIgnoreCase(INCREASE_RATE)) {
                    result.setInterest_rate(result.getInterest_rate() + rateDiscount);
                    result.setMatchLodedRule(true);

                }
            }
        }

        if (creditScoreMathSymbolRule != null && creditScoreMathSymbolRule.equalsIgnoreCase(LESS_THAN)) {

            if (priceRequest.getCredit_score() < creditScoreRule && action.equalsIgnoreCase(INCREASE_RATE)) {
                result.setInterest_rate(START_INTEREST_RATE + rateDiscount);
                result.setDisqualified(false);
                result.setMatch(true);
                result.setMatchLodedRule(true);

                if (priceRequest.getProductName().equalsIgnoreCase(INCREASED_RATE_PRODUCT_NAME)) {
                    result.setInterest_rate(result.getInterest_rate() + PRODUCT_NAME_INCREASED_RATE);

                }

                if (productNameRule != null) {
                    if (priceRequest.getProductName().equalsIgnoreCase(productNameRule) && action.equalsIgnoreCase(INCREASE_RATE)) {
                        result.setInterest_rate(result.getInterest_rate() + rateDiscount);
                        result.setMatchLodedRule(true);

                    }
                }
            }
        }

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

        return result.isMatch();
    }

    @Override
    public Result getResult() {
        return result;
    }
}