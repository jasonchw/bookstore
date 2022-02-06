package com.example.bookstore.payload;

import com.example.bookstore.model.Book;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleBookGetResponseTest {
    @Test
    void itemsToBooksWithEmptyList() {
        // given
        final List<GoogleBookGetResponse.Item> items = Collections.emptyList();

        // when
        List<Book> books = GoogleBookGetResponse.to(items);

        // then
        assertTrue(books.isEmpty());
    }

    @Test
    public void itemsToBooksWithNonEmptyList() {
        // given
        final List<GoogleBookGetResponse.Item> items = Arrays.asList(
                ImmutableGoogleBookGetResponse.Item.builder()
                        .volumeInfo(
                                ImmutableGoogleBookGetResponse.VolumeInfo.builder()
                                        .title("Title 1")
                                        .addAuthors("Author 1")
                                        .build()
                        )
                        .build(),
                ImmutableGoogleBookGetResponse.Item.builder()
                        .volumeInfo(
                                ImmutableGoogleBookGetResponse.VolumeInfo.builder()
                                        .title("Title 2")
                                        .addAuthors("Author 2")
                                        .build()
                        )
                        .build()
        );

        // when
        List<Book> books = GoogleBookGetResponse.to(items);

        // then
        assertFalse(books.isEmpty());
        assertEquals(items.size(), books.size());
    }

    @Test
    public void volumeInfoToBook() {
        // given
        final GoogleBookGetResponse.Item.VolumeInfo volumeInfo = ImmutableGoogleBookGetResponse.VolumeInfo.builder()
                .title("Title 1")
                .addAuthors("Author 1")
                .build();

        // when
        Book book = GoogleBookGetResponse.Item.VolumeInfo.to(volumeInfo);

        // then
        assertEquals(volumeInfo.getTitle(), book.getTitle());
        assertEquals(volumeInfo.getAuthors(), book.getAuthors());
    }
}
