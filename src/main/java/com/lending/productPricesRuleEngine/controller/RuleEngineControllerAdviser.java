package com.lending.productPricesRuleEngine.controller;

import com.lending.productPricesRuleEngine.exception.BadRequestException;
import com.lending.productPricesRuleEngine.exception.RulesNotAvailableException;
import com.lending.productPricesRuleEngine.model.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RuleEngineControllerAdviser {

    @ExceptionHandler(RulesNotAvailableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleRulesNotAvailableException(RulesNotAvailableException ex)
    {
        log.debug("not found", ex);
        return new ApiErrorResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBadRequestException(BadRequestException ex)
    {
        log.debug("Bad request", ex);
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST.toString(), ex.getMessage());
    }
}
