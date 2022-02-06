package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.ImmutableBook;
import com.example.bookstore.payload.ImmutableSearchRequest;
import com.example.bookstore.payload.SearchRequest;
import com.example.bookstore.repository.GoogleBookRepository;
import com.example.bookstore.repository.SearchHistoryRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class BookServiceTest {
    private BookService bookService;

    @Mock
    private GoogleBookRepository mockGoogleBookRepository;
    @Mock
    private SearchHistoryRepository mockSearchHistoryRepository;

    @BeforeEach
    public void beforeEach() {
        openMocks(this);
        bookService = new BookService(mockGoogleBookRepository, mockSearchHistoryRepository);
    }

    @Test
    public void findByAuthorNameWithAuthorName() {
        // given
        final String title = "Title 1";
        final String author = "Author";
        final List<String> authors = Arrays.asList(author + " 1");
        final List<Book> booksFromGoogle = Arrays.asList(ImmutableBook.builder().title(title).addAllAuthors(authors).build());

        when(mockGoogleBookRepository.findByAuthorName(any(SearchRequest.class))).thenReturn(booksFromGoogle);

        // when
        final String clientId = RandomStringUtils.randomAlphabetic(10);
        final SearchRequest searchRequest = ImmutableSearchRequest.builder().authorName(author).build();
        List<Book> books = bookService.findByAuthorName(clientId, searchRequest);

        // then
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        assertEquals(title, books.get(0).getTitle());
        assertEquals(authors, books.get(0).getAuthors());
        verify(mockGoogleBookRepository).findByAuthorName(any(SearchRequest.class));
        verify(mockSearchHistoryRepository).save(anyString(), anyList());
    }

    @Test
    public void findByAuthorNameWithNullAuthorName() {
        // given
        // none

        // when
        final String clientId = RandomStringUtils.randomAlphabetic(10);
        final String author = null;
        final SearchRequest searchRequest = ImmutableSearchRequest.builder().authorName(author).build();
        List<Book> books = bookService.findByAuthorName(clientId, searchRequest);

        // then
        assertTrue(books.isEmpty());
        verify(mockGoogleBookRepository, never()).findByAuthorName(any(SearchRequest.class));
        verify(mockSearchHistoryRepository, never()).save(anyString(), anyList());
    }

    @Test
    public void findByAuthorNameWithEmptyAuthorName() {
        // given
        // none

        // when
        final String clientId = RandomStringUtils.randomAlphabetic(10);
        final String author = "";
        final SearchRequest searchRequest = ImmutableSearchRequest.builder().authorName(author).build();
        List<Book> books = bookService.findByAuthorName(clientId, searchRequest);

        // then
        assertTrue(books.isEmpty());
        verify(mockGoogleBookRepository, never()).findByAuthorName(any(SearchRequest.class));
        verify(mockSearchHistoryRepository, never()).save(anyString(), anyList());
    }

    @Test
    public void findSearchHistoriesByClientId() {
        // given
        final String clientId = RandomStringUtils.randomAlphabetic(10);

        // when
        List<Book> books = bookService.findSearchHistoriesByClientId(clientId);

        // then
        // don't care about books, as we are testing SearchHistoryRepository separately
        verify(mockSearchHistoryRepository).findByClientId(anyString());
    }
}
