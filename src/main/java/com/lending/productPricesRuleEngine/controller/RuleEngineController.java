package com.lending.productPricesRuleEngine.controller;

import com.lending.productPricesRuleEngine.model.PriceRequest;
import com.lending.productPricesRuleEngine.model.Result;
import com.lending.productPricesRuleEngine.service.RulesEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@Slf4j
public class RuleEngineController {

    private final RulesEngine rulesEngine;

    @Autowired
    public RuleEngineController(RulesEngine rulesEngine)
    {
        this.rulesEngine = rulesEngine;
    }

    @RequestMapping(value = "/api/v1/prices",  method = RequestMethod.POST )
    public ResponseEntity<Result> getPrice(@Valid @RequestBody PriceRequest priceRequest)
    {
        return ResponseEntity.ok().body(rulesEngine.processRequest(priceRequest));
    }

}
