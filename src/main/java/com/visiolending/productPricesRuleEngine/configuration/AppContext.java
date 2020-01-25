package com.visiolending.productPricesRuleEngine.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppContext {

    @Bean(name = "RuleServiceRestTemplate")
    public RestTemplate RuleServiceRestTemplate()
    {
        return new RestTemplate();
    }
}
