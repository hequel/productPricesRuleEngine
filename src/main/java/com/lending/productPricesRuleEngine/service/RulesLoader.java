package com.lending.productPricesRuleEngine.service;

import com.lending.productPricesRuleEngine.exception.RulesNotAvailableException;
import com.lending.productPricesRuleEngine.model.Rules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RulesLoader {

    public static final String RULE_SERVICE_URL = "http://lending/api/v1/rules";

    private final RestTemplate restTemplate;

    @Autowired
    public RulesLoader(@Qualifier("RuleServiceRestTemplate") RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    //@Value("${rule.service.url}")
    //private String ruleServiceUrl;

    public Rules getRules() {

        log.info("Retrieving rules from rules service");

        try {
            ResponseEntity<Rules> ruleResponseEntity =  restTemplate.getForEntity(RULE_SERVICE_URL, Rules.class);

            return ruleResponseEntity.getBody();

        } catch (HttpServerErrorException e) {

            log.info("{} response, Error {} while retreiving rules from {}", e.getStatusCode(), e.getMessage(), RULE_SERVICE_URL);
            throw new RulesNotAvailableException("Please try some other time");
        }
    }


}
