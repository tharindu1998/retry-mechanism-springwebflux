package com.example.retrymechanism.client.impl;

import com.example.retrymechanism.client.ItemClient;
import com.example.retrymechanism.dto.Item;
import com.example.retrymechanism.exceptions.ApiError;
import com.example.retrymechanism.exceptions.GeneralStatusCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class ItemClientImpl implements ItemClient {

    private final WebClient webClient;

    public ItemClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }


    public Mono<Item> getData(String itemId) {
        return webClient.get()
                .uri("/getItemData", itemId)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(Item.class)
                .retryWhen(Retry
                        .backoff(3, Duration.ofSeconds(1)) // Retry up to 3 times with a backoff of 1 second
                        .maxBackoff(Duration.ofSeconds(5)) // Maximum backoff of 5 seconds
                        .doAfterRetry(retrySignal -> log.info("RETRY_ATTEMPTED | {} | Get Item Data Retry Attempted"))
                .filter(throwable -> throwable instanceof WebClientResponseException
                        && ((WebClientResponseException) throwable).getStatusCode().is5xxServerError()
                        || throwable.getCause() instanceof TimeoutException)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    log.info("SERVICE_UNAVAILABLE | External Service failed to process after max retries ");
                    try {
                        throw new GeneralStatusCodeException(ApiError.SERVICE_UNAVAILABLE);
                    } catch (GeneralStatusCodeException e) {
                        throw new RuntimeException(e);
                    }
                }));

    }
}
