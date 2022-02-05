package com.example.bookstore.util;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.ImmutableBook;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class BookUtils {
    public static List<Book> getBooks(int count) {
        List<Book> books = new ArrayList<>();

        IntStream.range(0, count).forEach(i -> {
                    String title = "Title " + RandomStringUtils.randomAlphabetic(10) + " " + i;
                    String author = "Author " + RandomStringUtils.randomAlphabetic(10) + " " + i;

                    books.add(
                            ImmutableBook.builder()
                                    .title(title)
                                    .addAuthors(author)
                                    .build()
                    );
                }
        );

        return books;
    }
}
