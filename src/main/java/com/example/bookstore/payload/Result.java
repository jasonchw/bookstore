package com.example.bookstore.payload;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableResult.class)
public interface Result<T> {
    enum Status {
        SUCCESS, FAILURE;
    }

    Status getStatus();

    T getPayload();
}
