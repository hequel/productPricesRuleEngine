package com.lending.productPricesRuleEngine.service;

import com.lending.productPricesRuleEngine.model.PriceRequest;
import com.lending.productPricesRuleEngine.model.Result;
import com.lending.productPricesRuleEngine.model.Rules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RulesEngine {

    private List<RuleEvaluator> rules = new ArrayList<>();

    private final RulesLoader rulesLoader;

    @Autowired
    public RulesEngine(RulesLoader rulesLoader) {
        this.rulesLoader = rulesLoader;

        rules.add(new CreditScoreDiscountRule());
        rules.add(new CreditScoreIncreaseRateRule());
        rules.add(new ProductNameIncreaseRateRule());
        rules.add(new DisqualifiedStateRule());

    }

    public Result processRequest(PriceRequest priceRequest) {

        Rules loaderRules = rulesLoader.getRules();

        double[] creditScoreRule = {0};
        String[] personStateRule = {null};
        String[] productNameRule = {null};
        String[] creditScoreMathSymbolRule = {null};
        Result[] rule = {null};

        if (loaderRules != null && !loaderRules.getRules().isEmpty()) {
            loaderRules.getRules()
                    .forEach(rulesDefinition -> {

                        String action = rulesDefinition.getAction();

                       // if (rule[0] != null && rule[0].isMatchLodedRule())
                        //    return;

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
                                .filter(r -> r.matches(priceRequest, action, finalRateDiscount, creditScoreRule[0], creditScoreMathSymbolRule[0], personStateRule[0], productNameRule[0]))
                                .map(r -> r.evaluate(priceRequest, action, finalRateDiscount, creditScoreRule[0], creditScoreMathSymbolRule[0], personStateRule[0], productNameRule[0]))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Expression does not matches any Rule"));


                    });
        } else {

            rule[0] = rules
                    .stream()
                    .filter(r -> r.matches(priceRequest, null, 0.0, 0.0, null, null, null))
                    .map(r -> r.evaluate(priceRequest, null, 0.0, 0.0, null, null, null))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Expression does not matches any Rule"));

        }
        log.info("rule engine result for priceRequest {} : {}", priceRequest.toString(), rule[0].toString());
        return rule[0];
    }

}
