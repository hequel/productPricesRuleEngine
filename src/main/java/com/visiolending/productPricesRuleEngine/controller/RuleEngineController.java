package com.visiolending.productPricesRuleEngine.controller;

import com.visiolending.productPricesRuleEngine.model.PriceRequest;
import com.visiolending.productPricesRuleEngine.model.Result;
import com.visiolending.productPricesRuleEngine.service.RulesEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Result> getPrice(@RequestBody PriceRequest priceRequest)
    {
        Result result = rulesEngine.processRequest(priceRequest);
        return ResponseEntity.ok().body(result);
    }

}
