package com.example.bookstore.model;

import com.example.bookstore.util.BookUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    @Test
    void toResultSuccessWithEmptyList() {
        // given
        final List<Book> books = Collections.emptyList();

        // when
        Result<List<Book>> result = Book.toResultSuccess(books);

        // then
        assertEquals(Result.Status.SUCCESS, result.getStatus());
        assertTrue(result.getPayload().isEmpty());
    }

    @Test
    void toResultSuccessWithNonEmptyList() {
        // given
        final int count = 2;
        final List<Book> books = BookUtils.getBooks(count);

        // when
        Result<List<Book>> result = Book.toResultSuccess(books);

        // then
        assertEquals(Result.Status.SUCCESS, result.getStatus());
        assertFalse(result.getPayload().isEmpty());
        assertEquals(count, result.getPayload().size());
    }
}
