package com.example.retrymechanism.exceptions;

public class GeneralStatusCodeException extends Exception {

    private final ApiError apiError;

    public GeneralStatusCodeException(ApiError apiError) {
        super(apiError.getTitle());
        this.apiError = apiError;
    }

    public ErrorMessage getApiError() {
        return new ErrorMessage(apiError);
    }
}
