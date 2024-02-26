package com.example.retrymechanism.client;

import com.example.retrymechanism.dto.Item;
import reactor.core.publisher.Mono;

public interface ItemClient {

    Mono<Item> getData(String itemId);
}
