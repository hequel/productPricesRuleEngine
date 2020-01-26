package com.visiolending.productPricesRuleEngine.service;

import com.visiolending.productPricesRuleEngine.model.PriceRequest;
import com.visiolending.productPricesRuleEngine.model.Result;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import static com.visiolending.productPricesRuleEngine.RulesConstant.*;

@Slf4j
public class CreditScoreIncreaseRateRule implements RuleEvaluator {
    Result result = new Result();

    @Override
    public boolean matches(PriceRequest priceRequest, String action, double rateDiscount, double creditScoreRule, String creditScoreMathSymbolRule, String personStateRule, String productNameRule) {
        return ((priceRequest.getCredit_score() < CURRENT_CREDIT_SCORE_BAR
                || creditScoreMathSymbolRule != null
                && creditScoreMathSymbolRule.equalsIgnoreCase(LESS_THAN)));
    }

    @Override
    public Result evaluate(PriceRequest priceRequest, String action, double rateDiscount, double creditScoreRule, String creditScoreMathSymbolRule, String personStateRule, String productNameRule) {

        if (priceRequest.getCredit_score() < CURRENT_CREDIT_SCORE_BAR) {
            result.setInterest_rate(START_INTEREST_RATE + POINT_FIVE_PERCENT_INCREASED_RATE);
            result.setDisqualified(false);

            if (priceRequest.getProductName().equalsIgnoreCase(INCREASED_RATE_PRODUCT_NAME))
                result.setInterest_rate(result.getInterest_rate() + PRODUCT_NAME_INCREASED_RATE);

            if (productNameRule != null && priceRequest.getProductName().equalsIgnoreCase(productNameRule) && action.equalsIgnoreCase(INCREASE_RATE))
                result.setInterest_rate(result.getInterest_rate() + rateDiscount);

        }

        if (creditScoreMathSymbolRule != null && creditScoreMathSymbolRule.equalsIgnoreCase(LESS_THAN)) {

            if (priceRequest.getCredit_score() < creditScoreRule && action.equalsIgnoreCase(INCREASE_RATE)) {
                result.setInterest_rate(START_INTEREST_RATE + rateDiscount);
                result.setDisqualified(false);
                result.setMatchLodedRule(true);

                if (priceRequest.getProductName().equalsIgnoreCase(INCREASED_RATE_PRODUCT_NAME))
                    result.setInterest_rate(result.getInterest_rate() + PRODUCT_NAME_INCREASED_RATE);

                if (productNameRule != null && priceRequest.getProductName().equalsIgnoreCase(productNameRule) && action.equalsIgnoreCase(INCREASE_RATE))
                    result.setInterest_rate(result.getInterest_rate() + rateDiscount);
            }
        }

        if (priceRequest.getState().equalsIgnoreCase(DISQUALIFIED_STATE))
            result.setDisqualified(true);

        if (personStateRule != null && priceRequest.getState().equalsIgnoreCase(personStateRule) && action.equalsIgnoreCase(DISQUALIFY))
            result.setDisqualified(true);

        return result;
    }
}