package com.cfgtest.services.riskevaluation.exceptionhandlers;

import com.cfgtest.client.customerservice.model.ErrorDetails;
import lombok.Getter;

@Getter
public class RiskEvaluationServiceException extends RuntimeException {

    private ErrorDetails.ResultEnum resultEnum;

    private String sourceSystem;

    private String traceId;

    public RiskEvaluationServiceException(ErrorDetails.ResultEnum resultEnum, String sourceSystem, String traceId, Integer errorCode) {
        this.resultEnum = resultEnum;
        this.sourceSystem = sourceSystem;
        this.traceId = traceId;
        this.errorCode = errorCode;
    }

    public RiskEvaluationServiceException(String message, ErrorDetails.ResultEnum resultEnum, String sourceSystem, String traceId, Integer errorCode) {
        super(message);
        this.resultEnum = resultEnum;
        this.sourceSystem = sourceSystem;
        this.traceId = traceId;
        this.errorCode = errorCode;
    }

    public RiskEvaluationServiceException(String message, Throwable cause, ErrorDetails.ResultEnum resultEnum, String sourceSystem, String traceId, Integer errorCode) {
        super(message, cause);
        this.resultEnum = resultEnum;
        this.sourceSystem = sourceSystem;
        this.traceId = traceId;
        this.errorCode = errorCode;
    }

    public RiskEvaluationServiceException(Throwable cause, ErrorDetails.ResultEnum resultEnum, String sourceSystem, String traceId, Integer errorCode) {
        super(cause);
        this.resultEnum = resultEnum;
        this.sourceSystem = sourceSystem;
        this.traceId = traceId;
        this.errorCode = errorCode;
    }

    private Integer errorCode;


}
