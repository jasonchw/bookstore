package com.example.bookstore.payload;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;


@Value.Immutable
@JsonSerialize(as = ImmutableSearchRequest.class)
@JsonDeserialize(as = ImmutableSearchRequest.class)
public interface SearchRequest {
    @Nullable
    String getAuthorName();
}
