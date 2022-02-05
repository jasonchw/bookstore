package com.example.bookstore.payload;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableErrorResponse.class)
@JsonDeserialize(as = ImmutableErrorResponse.class)
public interface ErrorResponse {
    String errorCode();

    String errorMessage();
}
