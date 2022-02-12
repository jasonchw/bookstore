package com.example.bookstore.model;

import com.example.bookstore.payload.ImmutableResult;
import com.example.bookstore.payload.Result;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableBook.class)
@JsonDeserialize(as = ImmutableBook.class)
public interface Book {
    String getTitle();

    List<String> getAuthors();

    static Result<List<Book>> toResultSuccess(List<Book> books) {
        return ImmutableResult.<List<Book>>builder()
                .status(Result.Status.SUCCESS)
                .payload(books)
                .build();
    }
}
