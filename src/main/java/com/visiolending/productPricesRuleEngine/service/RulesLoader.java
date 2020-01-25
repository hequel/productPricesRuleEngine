package com.visiolending.productPricesRuleEngine.service;

import com.visiolending.productPricesRuleEngine.exception.BadRequestException;
import com.visiolending.productPricesRuleEngine.exception.RulesNotAvailableException;
import com.visiolending.productPricesRuleEngine.model.Rules;
import com.visiolending.productPricesRuleEngine.model.RulesDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.digester.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RulesLoader {

    public static final String RULE_SERVICE_URL = "http://visiolending/api/v1/rules";

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
