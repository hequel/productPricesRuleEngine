package com.visiolending.productPricesRuleEngine.service;

import com.visiolending.productPricesRuleEngine.model.PriceRequest;
import com.visiolending.productPricesRuleEngine.model.Result;
import com.visiolending.productPricesRuleEngine.model.Rules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RulesEngine {

    private static List<RuleEvaluator> rules = new ArrayList<>();

    public  static final double CURRENT_CREDIT_SCORE_BAR = 720.0;
    static final Double START_INTEREST_RATE = 5.0;
    static final String DISQUALIFIED_STATE = "florida";
    public static final double GREATER_THAN_720_INCREASE_RATE = .3;
    public static final double PRODUCT_NAME_INCREASED_RATE = .5;
    static final String INCREASED_RATE_PRODUCT_NAME = "7-1 ARM";
    public static final String INCREASE_RATE = "increase_rate";
    public static final String DECREASE_RATE = "decrease_rate";
    public static final String DISQUALIFY = "disqualify";
    public static final String LESS_THAN = "<";
    public static final String GREATER_THAN_OR_EQUAL_TO = ">=";

    private final RulesLoader rulesLoader;

    @Autowired
    public RulesEngine(RulesLoader rulesLoader) {
        this.rulesLoader = rulesLoader;
    }

    static {
        rules.add(new CreditScoreIncreaseRateRule());
        rules.add(new DisqualifiedStateRule());
        rules.add(new CreditScoreDiscountRule());
        rules.add(new ProductNameIncreaseRateRule());
    }

    public Result processRequest(PriceRequest priceRequest) {

        Rules loaderRules = rulesLoader.getRules();

        double[] creditScoreRule = {0};
        String[] personStateRule = {null};
        String[] productNameRule = {null};
        String[] creditScoreMathSymbolRule = {null};
        RuleEvaluator[] rule = {null};

        if(loaderRules != null && !loaderRules.getRules().isEmpty()) {
            loaderRules.getRules()
                    .forEach(rulesDefinition -> {

                        String action = rulesDefinition.getAction();

                        double rateDiscount = 0; // = Double.parseDouble(rulesDefinition.getParameter());
                        if (rulesDefinition.getParameter() != null)
                            rateDiscount = Double.parseDouble(rulesDefinition.getParameter());

                        rulesDefinition.getActionTtriggers()
                                .forEach(
                                        actionTrigger -> {
                                            if (actionTrigger.getPersonCreditSCore() != null)

                                                creditScoreRule[0] = Double.parseDouble(actionTrigger.getPersonCreditSCore());
                                            if (actionTrigger.getPersonCreditSCoreComparisonOperator() != null)
                                                creditScoreMathSymbolRule[0] = actionTrigger.getPersonCreditSCoreComparisonOperator();
                                            if (actionTrigger.getPersonState() != null)
                                                personStateRule[0] = actionTrigger.getPersonState();
                                            if (actionTrigger.getProductName() != null)
                                                productNameRule[0] = actionTrigger.getProductName();
                                        }
                                );


                        double finalRateDiscount = rateDiscount;
                        rule[0] = rules
                                .stream()
                                .filter(r -> r.evaluate(priceRequest, action, finalRateDiscount, creditScoreRule[0], creditScoreMathSymbolRule[0], personStateRule[0], productNameRule[0]))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Expression does not matches any Rule"));


                    });
        }else
        {
            rule[0] = rules
                    .stream()
                    .filter(r -> r.evaluate(priceRequest, null, 0.0, 0.0, null, null, null))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Expression does not matches any Rule"));

        }

        return rule[0].getResult();
    }

}
