package com.cfgtest.services.riskevaluation.exceptionhandlers;

import com.cfgtest.client.customerservice.model.ErrorDetails;
import com.cfgtest.client.customerservice.model.ErrorDetailsErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import javax.validation.ConstraintViolationException;
import java.util.StringJoiner;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(RiskEvaluationServiceException.class)
//    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDetails> handleCustomerServiceException(RiskEvaluationServiceException cse) {
        log.error("***** Customer Service Exception Handler *****");
        ErrorDetails errorDetails = new ErrorDetails();
        ErrorDetailsErrorDetails errorDetailsItem = new ErrorDetailsErrorDetails();
        errorDetails.setResult(cse.getResultEnum());
        errorDetails.setSource(cse.getSourceSystem());
        errorDetails.setTraceId(cse.getTraceId());
        errorDetails.addErrorDetailsItem(errorDetailsItem);
        errorDetailsItem.code(cse.getErrorCode());
        errorDetailsItem.message(cse.getMessage());
        log.error("***** Error Details Object *****\n {}" , errorDetails);
        return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.valueOf(cse.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDetails handleCustomerServiceException(Exception e) {
        log.error("***** Runtime Exception Handler *****");
        e.printStackTrace();
        ErrorDetails errorDetails = new ErrorDetails();
        ErrorDetailsErrorDetails errorDetailsItem = new ErrorDetailsErrorDetails();
        errorDetails.setResult(ErrorDetails.ResultEnum.FAILED);
        errorDetails.setSource("INTERNAL SYSTEM ERROR");
        errorDetails.addErrorDetailsItem(errorDetailsItem);
        errorDetailsItem.code(500);
        errorDetailsItem.message(e.getMessage());
        log.error("***** Error Details Object *****\n {}" , errorDetails);
        return errorDetails;
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorDetails handleWebExchangeBindException(WebExchangeBindException e) {
        log.error("***** WebExchangeBindException handler *****");
        ErrorDetails errorDetails = new ErrorDetails();

        errorDetails.setResult(ErrorDetails.ResultEnum.FAILED);
        errorDetails.setSource(e.getReason());
        e.getFieldErrors().stream().forEach(fieldError -> {
            ErrorDetailsErrorDetails errorDetailsItem = new ErrorDetailsErrorDetails();
            errorDetails.addErrorDetailsItem(errorDetailsItem);
//            errorDetailsItem.code(Integer.getInteger(fieldError.getCode()));
            StringJoiner message = new StringJoiner(" ");
            message.add(fieldError.getField()).add(":" ).add(fieldError.getDefaultMessage());
            errorDetailsItem.message(message.toString());
        });

        log.error("***** Error Details Object *****\n {}" , errorDetails);
        return errorDetails;
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorDetails handleConstraintViolationException(ConstraintViolationException e) {
        log.error("***** ConstraintViolationException handler *****");
        ErrorDetails errorDetails = new ErrorDetails();

        errorDetails.setResult(ErrorDetails.ResultEnum.FAILED);
        errorDetails.setSource(e.getLocalizedMessage());
        e.getConstraintViolations().stream().forEach(constraintViolation -> {
            StringJoiner stringJoiner = new StringJoiner(" ");
            ErrorDetailsErrorDetails errorDetailsItem = new ErrorDetailsErrorDetails();
            errorDetails.addErrorDetailsItem(errorDetailsItem);
            stringJoiner.add(constraintViolation.getPropertyPath().toString()).add(":").add(constraintViolation.getMessage());
            errorDetailsItem.message(stringJoiner.toString());
        });

        log.error("***** Error Details Object *****\n {}" , errorDetails);
        return errorDetails;
    }

}
