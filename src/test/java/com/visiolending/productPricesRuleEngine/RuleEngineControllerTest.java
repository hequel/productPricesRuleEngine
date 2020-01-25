package com.visiolending.productPricesRuleEngine;

import com.visiolending.productPricesRuleEngine.model.*;
import com.visiolending.productPricesRuleEngine.service.RulesEngine;
import com.visiolending.productPricesRuleEngine.service.RulesLoader;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import static com.visiolending.productPricesRuleEngine.service.RulesEngine.*;
import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.*;

import static com.visiolending.productPricesRuleEngine.service.RulesLoader.RULE_SERVICE_URL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.mockito.Matchers.eq;

@Slf4j
@SpringBootTest(classes = ProductPricesRuleEngineApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RuleEngineControllerTest {

    @Value("${local.server.port}")
    private int port;

    @MockBean(name = "RuleServiceRestTemplate")
    private RestTemplate RuleServiceRestTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RulesEngine rulesEngine;

    @MockBean
    private RulesLoader rulesLoader;


    @Test
    public void send_request_should_return_decrease_rate_when_credit_score_greater_or_equal_720()
    {
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setCredit_score(725);
        priceRequest.setProductName("homeMorgage");
        priceRequest.setState("california");

        Result result = new Result();
        result.setInterest_rate(4.7);
        result.setDisqualified(false);

        List<RulesDefinition> rulesDefinitions = new ArrayList<>();

        RulesDefinition rulesDefinition = new RulesDefinition();
        rulesDefinition.setAction(DECREASE_RATE);
        rulesDefinition.setParameter(".3");

        List<ActionTrigger> actionTriggers = new ArrayList<>();

        ActionTrigger actionTriggerCreditScore = new ActionTrigger();
        actionTriggerCreditScore.setPersonCreditSCore("720");
        actionTriggerCreditScore.setPersonCreditSCoreComparisonOperator(GREATER_THAN_OR_EQUAL_TO);

        actionTriggers.add(actionTriggerCreditScore);

        rulesDefinition.setActionTtriggers(actionTriggers);
        rulesDefinitions.add(rulesDefinition);
        Rules rules = new Rules();
        rules.setRules(rulesDefinitions);

        ResponseEntity<Rules> listResponseEntity = mock(ResponseEntity.class);
        when(listResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(listResponseEntity.getBody()).thenReturn(rules);

        when(RuleServiceRestTemplate
                .getForEntity(
                        eq(RULE_SERVICE_URL),
                        eq(Rules.class)))
                .thenReturn(listResponseEntity);

        when(rulesLoader.getRules()).thenReturn(rules);


        ResponseEntity<Result> ruleEngileResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/ruleEngine/api/v1/prices", priceRequest, Result.class);

        assertThat(ruleEngileResponseEntity, notNullValue());
        assertThat(ruleEngileResponseEntity.getStatusCode(), is(HttpStatus.OK));

        Result resultResponse = ruleEngileResponseEntity.getBody();
        assertThat(resultResponse.getInterest_rate(), is(4.7));
        assertFalse(resultResponse.isDisqualified());
    }

    @Test
    public void send_request_should_return_decrease_rate_when_credit_score_greater_or_equal_720_using_initial_rules() {
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setCredit_score(725);
        priceRequest.setProductName("homeMorgage");
        priceRequest.setState("california");

        Rules rules = new Rules();
        rules.setRules(Collections.EMPTY_LIST);

        ResponseEntity<Rules> listResponseEntity = mock(ResponseEntity.class);
        when(listResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(listResponseEntity.getBody()).thenReturn(rules);

        when(RuleServiceRestTemplate
                .getForEntity(
                        eq(RULE_SERVICE_URL),
                        eq(Rules.class)))
                .thenReturn(listResponseEntity);

        when(rulesLoader.getRules()).thenReturn(rules);


        ResponseEntity<Result> ruleEngileResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/ruleEngine/api/v1/prices", priceRequest, Result.class);

        assertThat(ruleEngileResponseEntity, notNullValue());
        assertThat(ruleEngileResponseEntity.getStatusCode(), is(HttpStatus.OK));

        Result resultResponse = ruleEngileResponseEntity.getBody();
        assertThat(resultResponse.getInterest_rate(), is(4.7));
        assertFalse(resultResponse.isDisqualified());
    }


    @Test
    public void send_request_should_return_increase_rate_when_credit_score_less_Than_720()
    {
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setCredit_score(695);
        priceRequest.setProductName("carloan");
        priceRequest.setState("utah");

        Result result = new Result();
        result.setInterest_rate(5.5);
        result.setDisqualified(false);

        List<RulesDefinition> rulesDefinitions = new ArrayList<>();

        RulesDefinition rulesDefinition = new RulesDefinition();
        rulesDefinition.setAction(INCREASE_RATE);
        rulesDefinition.setParameter(".5");

        List<ActionTrigger> actionTriggers = new ArrayList<>();

        ActionTrigger actionTriggerCreditScore = new ActionTrigger();
        actionTriggerCreditScore.setPersonCreditSCore("720");
        actionTriggerCreditScore.setPersonCreditSCoreComparisonOperator(LESS_THAN);

        actionTriggers.add(actionTriggerCreditScore);

        rulesDefinition.setActionTtriggers(actionTriggers);
        rulesDefinitions.add(rulesDefinition);
        Rules rules = new Rules();
        rules.setRules(rulesDefinitions);

        ResponseEntity<Rules> listResponseEntity = mock(ResponseEntity.class);
        when(listResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(listResponseEntity.getBody()).thenReturn(rules);

        when(RuleServiceRestTemplate
                .getForEntity(
                        eq(RULE_SERVICE_URL),
                        eq(Rules.class)))
                .thenReturn(listResponseEntity);

        when(rulesLoader.getRules()).thenReturn(rules);


        ResponseEntity<Result> ruleEngileResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/ruleEngine/api/v1/prices", priceRequest, Result.class);

        assertThat(ruleEngileResponseEntity, notNullValue());
        assertThat(ruleEngileResponseEntity.getStatusCode(), is(HttpStatus.OK));

        Result resultResponse = ruleEngileResponseEntity.getBody();
        assertThat(resultResponse.getInterest_rate(), is(5.5));
        assertFalse(resultResponse.isDisqualified());
    }

    @Test
    public void send_request_should_return_increase_rate_when_credit_score_less_Than_720_using_initial_set_of_rules()
    {
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setCredit_score(695);
        priceRequest.setProductName("carloan");
        priceRequest.setState("utah");

        Result result = new Result();
        result.setInterest_rate(5.5);
        result.setDisqualified(false);

        Rules rules = new Rules();
        rules.setRules(Collections.EMPTY_LIST);

        ResponseEntity<Rules> listResponseEntity = mock(ResponseEntity.class);
        when(listResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(listResponseEntity.getBody()).thenReturn(rules);

        when(RuleServiceRestTemplate
                .getForEntity(
                        eq(RULE_SERVICE_URL),
                        eq(Rules.class)))
                .thenReturn(listResponseEntity);

        when(rulesLoader.getRules()).thenReturn(rules);


        ResponseEntity<Result> ruleEngileResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/ruleEngine/api/v1/prices", priceRequest, Result.class);

        assertThat(ruleEngileResponseEntity, notNullValue());
        assertThat(ruleEngileResponseEntity.getStatusCode(), is(HttpStatus.OK));

        Result resultResponse = ruleEngileResponseEntity.getBody();
        assertThat(resultResponse.getInterest_rate(), is(5.5));
        assertFalse(resultResponse.isDisqualified());
    }

    @Test
    public void send_request_should_return_increase_rate_when_program_name_is_equal_to_71_ARM()
    {
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setCredit_score(695);
        priceRequest.setProductName("7-1 ARM");
        priceRequest.setState("utah");

        Result result = new Result();
        result.setInterest_rate(5.5);
        result.setDisqualified(false);

        List<RulesDefinition> rulesDefinitions = new ArrayList<>();

        RulesDefinition rulesDefinition = new RulesDefinition();
        rulesDefinition.setAction(INCREASE_RATE);
        rulesDefinition.setParameter(".5");

        List<ActionTrigger> actionTriggers = new ArrayList<>();

        ActionTrigger actionTriggerCreditScore = new ActionTrigger();
        actionTriggerCreditScore.setPersonCreditSCore("720");
        actionTriggerCreditScore.setPersonCreditSCoreComparisonOperator(LESS_THAN);

        actionTriggers.add(actionTriggerCreditScore);

        rulesDefinition.setActionTtriggers(actionTriggers);
        rulesDefinitions.add(rulesDefinition);
        Rules rules = new Rules();
        rules.setRules(rulesDefinitions);

        ResponseEntity<Rules> listResponseEntity = mock(ResponseEntity.class);
        when(listResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(listResponseEntity.getBody()).thenReturn(rules);

        when(RuleServiceRestTemplate
                .getForEntity(
                        eq(RULE_SERVICE_URL),
                        eq(Rules.class)))
                .thenReturn(listResponseEntity);

        when(rulesLoader.getRules()).thenReturn(rules);


        ResponseEntity<Result> ruleEngileResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/ruleEngine/api/v1/prices", priceRequest, Result.class);

        assertThat(ruleEngileResponseEntity, notNullValue());
        assertThat(ruleEngileResponseEntity.getStatusCode(), is(HttpStatus.OK));

        Result resultResponse = ruleEngileResponseEntity.getBody();
        assertThat(resultResponse.getInterest_rate(), is(5.5));
        assertFalse(resultResponse.isDisqualified());
    }

    @Test
    public void send_request_should_return_increase_rate_when_program_name_is_equal_to_71_ARM_using_initial_set_of_rules()
    {
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setCredit_score(695);
        priceRequest.setProductName("7-1 ARM");
        priceRequest.setState("utah");

        Result result = new Result();
        result.setInterest_rate(5.5);
        result.setDisqualified(false);

        Rules rules = new Rules();
        rules.setRules(Collections.EMPTY_LIST);

        ResponseEntity<Rules> listResponseEntity = mock(ResponseEntity.class);
        when(listResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(listResponseEntity.getBody()).thenReturn(rules);

        when(RuleServiceRestTemplate
                .getForEntity(
                        eq(RULE_SERVICE_URL),
                        eq(Rules.class)))
                .thenReturn(listResponseEntity);

        when(rulesLoader.getRules()).thenReturn(rules);


        ResponseEntity<Result> ruleEngileResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/ruleEngine/api/v1/prices", priceRequest, Result.class);

        assertThat(ruleEngileResponseEntity, notNullValue());
        assertThat(ruleEngileResponseEntity.getStatusCode(), is(HttpStatus.OK));

        Result resultResponse = ruleEngileResponseEntity.getBody();
        assertThat(resultResponse.getInterest_rate(), is(5.5));
        assertFalse(resultResponse.isDisqualified());
    }


    @Test
    public void send_request_should_return_disqualify_when_person_State_is_florida()
    {
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setCredit_score(700);
        priceRequest.setProductName("carloan");
        priceRequest.setState("florida");

        Result result = new Result();
        result.setDisqualified(true);

        List<RulesDefinition> rulesDefinitions = new ArrayList<>();

        RulesDefinition rulesDefinition = new RulesDefinition();
        rulesDefinition.setAction(DISQUALIFY);

        List<ActionTrigger> actionTriggers = new ArrayList<>();

        ActionTrigger actionTriggerCreditScore = new ActionTrigger();
        actionTriggerCreditScore.setPersonState("florida");

        actionTriggers.add(actionTriggerCreditScore);

        rulesDefinition.setActionTtriggers(actionTriggers);
        rulesDefinitions.add(rulesDefinition);
        Rules rules = new Rules();
        rules.setRules(rulesDefinitions);

        ResponseEntity<Rules> listResponseEntity = mock(ResponseEntity.class);
        when(listResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(listResponseEntity.getBody()).thenReturn(rules);

        when(RuleServiceRestTemplate
                .getForEntity(
                        eq(RULE_SERVICE_URL),
                        eq(Rules.class)))
                .thenReturn(listResponseEntity);

        when(rulesLoader.getRules()).thenReturn(rules);


        ResponseEntity<Result> ruleEngileResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/ruleEngine/api/v1/prices", priceRequest, Result.class);

        assertThat(ruleEngileResponseEntity, notNullValue());
        assertThat(ruleEngileResponseEntity.getStatusCode(), is(HttpStatus.OK));
        Result result1 =  ruleEngileResponseEntity.getBody();
        assertThat(result1.isDisqualified(), is(true));
    }

    @Test
    public void send_request_should_return_disqualify_when_person_State_is_florida_using_initial_set_of_rules()
    {
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setCredit_score(700);
        priceRequest.setProductName("carloan");
        priceRequest.setState("florida");

        Result result = new Result();
        result.setDisqualified(true);

        Rules rules = new Rules();
        rules.setRules(Collections.EMPTY_LIST);

        ResponseEntity<Rules> listResponseEntity = mock(ResponseEntity.class);
        when(listResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(listResponseEntity.getBody()).thenReturn(rules);

        when(RuleServiceRestTemplate
                .getForEntity(
                        eq(RULE_SERVICE_URL),
                        eq(Rules.class)))
                .thenReturn(listResponseEntity);

        when(rulesLoader.getRules()).thenReturn(rules);


        ResponseEntity<Result> ruleEngileResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/ruleEngine/api/v1/prices", priceRequest, Result.class);

        assertThat(ruleEngileResponseEntity, notNullValue());
        assertThat(ruleEngileResponseEntity.getStatusCode(), is(HttpStatus.OK));
        Result result1 =  ruleEngileResponseEntity.getBody();
        assertThat(result1.isDisqualified(), is(true));
    }

    @Test
    public void send_request_should_return_decrease_rate_of_5_percent_when_credit_score_greater_or_equal_750()
    {
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setCredit_score(759);
        priceRequest.setProductName("homeMorgage");
        priceRequest.setState("california");

        Result result = new Result();
        result.setInterest_rate(4.5);
        result.setDisqualified(false);

        List<RulesDefinition> rulesDefinitions = new ArrayList<>();

        RulesDefinition rulesDefinition = new RulesDefinition();
        rulesDefinition.setAction(DECREASE_RATE);
        rulesDefinition.setParameter(".5");

        List<ActionTrigger> actionTriggers = new ArrayList<>();

        ActionTrigger actionTriggerCreditScore = new ActionTrigger();
        actionTriggerCreditScore.setPersonCreditSCore("750");
        actionTriggerCreditScore.setPersonCreditSCoreComparisonOperator(GREATER_THAN_OR_EQUAL_TO);

        actionTriggers.add(actionTriggerCreditScore);

        rulesDefinition.setActionTtriggers(actionTriggers);
        rulesDefinitions.add(rulesDefinition);
        Rules rules = new Rules();
        rules.setRules(rulesDefinitions);

        ResponseEntity<Rules> listResponseEntity = mock(ResponseEntity.class);
        when(listResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(listResponseEntity.getBody()).thenReturn(rules);

        when(RuleServiceRestTemplate
                .getForEntity(
                        eq(RULE_SERVICE_URL),
                        eq(Rules.class)))
                .thenReturn(listResponseEntity);

        when(rulesLoader.getRules()).thenReturn(rules);


        ResponseEntity<Result> ruleEngileResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/ruleEngine/api/v1/prices", priceRequest, Result.class);

        assertThat(ruleEngileResponseEntity, notNullValue());
        assertThat(ruleEngileResponseEntity.getStatusCode(), is(HttpStatus.OK));

        Result resultResponse = ruleEngileResponseEntity.getBody();
        assertThat(resultResponse.getInterest_rate(), is(4.5));
        assertFalse(resultResponse.isDisqualified());
    }


    private String serviceUrl(String path) {
        return "http://localhost:" + port + "/api/v1" + path;
    }

}
