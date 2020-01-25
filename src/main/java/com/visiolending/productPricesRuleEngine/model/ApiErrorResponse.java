package com.visiolending.productPricesRuleEngine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.annotation.WebFilter;

@Data
@WebFilter
@AllArgsConstructor
public class ApiErrorResponse {

    private String status;
    private String message;
}
