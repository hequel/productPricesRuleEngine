package com.visiolending.productPricesRuleEngine.service;

import com.visiolending.productPricesRuleEngine.model.PriceRequest;
import com.visiolending.productPricesRuleEngine.model.Result;

import static com.visiolending.productPricesRuleEngine.service.RulesEngine.DISQUALIFIED_STATE;
import static com.visiolending.productPricesRuleEngine.service.RulesEngine.DISQUALIFY;

public class DisqualifiedStateRule implements RuleEvaluator {
    Result result = new Result();

    @Override
    public boolean evaluate(PriceRequest priceRequest, String action, double rateDiscount, double creditScoreRule, String creditScoreMathSymbolRule, String personStateRule, String productNameRule) {

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