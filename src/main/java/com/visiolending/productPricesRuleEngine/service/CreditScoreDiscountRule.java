package com.visiolending.productPricesRuleEngine.service;

import com.visiolending.productPricesRuleEngine.model.PriceRequest;
import com.visiolending.productPricesRuleEngine.model.Result;

import static com.visiolending.productPricesRuleEngine.service.RulesEngine.*;

public class CreditScoreDiscountRule implements RuleEvaluator {

    Result result = new Result();

    @Override
    public boolean evaluate(PriceRequest priceRequest, String action, double rateDiscount, double creditScoreRule, String creditScoreMathSymbolRule, String personStateRule, String productNameRule) {

        if (priceRequest.getCredit_score() >= CURRENT_CREDIT_SCORE_BAR) {
            result.setInterest_rate(START_INTEREST_RATE - GREATER_THAN_720_INCREASE_RATE);
            result.setDisqualified(false);
            result.setMatch(true);
        }

        if (creditScoreRule != 0.0) {

            if (creditScoreMathSymbolRule != null && creditScoreMathSymbolRule.equalsIgnoreCase(GREATER_THAN_OR_EQUAL_TO)) {

                if (priceRequest.getCredit_score() >= creditScoreRule && action.equalsIgnoreCase(DECREASE_RATE)) {
                    result.setInterest_rate(START_INTEREST_RATE - rateDiscount);
                    result.setDisqualified(false);
                    result.setMatch(true);
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
            }

        }
        return result.isMatch();
    }

    @Override
    public Result getResult() {
        return result;
    }
}
