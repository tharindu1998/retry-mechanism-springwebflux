package com.example.retrymechanism.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiError {
    SERVICE_UNAVAILABLE("External Service failed to process after max retries", "Z101Z", "External Service failed to process after max retries", HttpStatus.SERVICE_UNAVAILABLE);

    private final String title;
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ApiError(String title, String code, String message, HttpStatus httpStatus) {
        this.title = title;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "title='" + title + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", httpStatus=" + httpStatus +
                '}';
    }
}
