package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import com.example.bookstore.util.BookUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.example.bookstore.repository.SearchHistoryRepository.BOOK_COMPARATOR;
import static com.example.bookstore.repository.SearchHistoryRepository.MAX_CACHE_SIZE_PER_CLIENT;
import static org.junit.jupiter.api.Assertions.*;

class SearchHistoryRepositoryTest {
    private SearchHistoryRepository searchHistoryRepository;

    private Map<String, SortedSet<Book>> histories;

    @BeforeEach
    public void beforeEach() {
        histories = new HashMap<>();

        searchHistoryRepository = new SearchHistoryRepository(histories);
    }

    @Test
    void saveWithNullClientId() {
        // given
        final String clientId = null;
        final int expectedCount = 2;
        final List<Book> books = BookUtils.getBooks(expectedCount);

        // when
        int count = searchHistoryRepository.save(clientId, books);

        // then
        assertEquals(expectedCount, count);
    }

    @Test
    void saveWithEmptyClientId() {
        // given
        final String clientId = "";
        final int expectedCount = 2;
        final List<Book> books = BookUtils.getBooks(expectedCount);

        // when
        int count = searchHistoryRepository.save(clientId, books);

        // then
        assertEquals(expectedCount, count);
    }

    @Test
    void saveWithNewClientId() {
        // given
        final String clientId = RandomStringUtils.randomAlphabetic(10);
        final int expectedCount = 2;
        final List<Book> books = BookUtils.getBooks(expectedCount);

        // when
        int count = searchHistoryRepository.save(clientId, books);

        // then
        assertEquals(books.size(), count);
    }

    @Test
    void saveWithExistingClientId() {
        // given
        final String clientId = RandomStringUtils.randomAlphabetic(10);
        final int expectedCount1 = 2;
        final List<Book> books1 = BookUtils.getBooks(expectedCount1);
        final int expectedCount2 = 12;
        final List<Book> books2 = BookUtils.getBooks(expectedCount2);

        // when
        int count1 = searchHistoryRepository.save(clientId, books1);
        int count2 = searchHistoryRepository.save(clientId, books2);

        // then
        assertEquals(expectedCount1, count1);
        assertEquals(expectedCount1 + expectedCount2, count2);
    }

    @Test
    void saveMoreThanMaxCacheSize() {
        // given
        final String clientId = RandomStringUtils.randomAlphabetic(10);
        final int maxCountPlus = MAX_CACHE_SIZE_PER_CLIENT + 10;
        final List<Book> books = BookUtils.getBooks(maxCountPlus);

        // when
        int count = searchHistoryRepository.save(clientId, books);

        // then
        assertEquals(books.size(), maxCountPlus);
        assertEquals(MAX_CACHE_SIZE_PER_CLIENT, count);
    }

    @Test
    void findByClientIdWithSearchHistories() {
        // given
        final String clientId = RandomStringUtils.randomAlphabetic(10);
        final int expectedCount = 2;
        final List<Book> cachedBooks = BookUtils.getBooks(expectedCount);
        cachedBooks.sort(Comparator.comparing(Book::getTitle)); // notes - need to apply same sorting order for later comparison

        SortedSet<Book> bookHistories = new TreeSet<>(BOOK_COMPARATOR);
        bookHistories.addAll(cachedBooks);
        histories.put(clientId, bookHistories);

        // when
        List<Book> books = searchHistoryRepository.findByClientId(clientId);

        // then
        assertFalse(books.isEmpty());
        assertEquals(cachedBooks, books);
    }

    @Test
    void findByClientIdWithoutSearchHistories() {
        // given
        final String clientId = RandomStringUtils.randomAlphabetic(10);

        // when
        List<Book> books = searchHistoryRepository.findByClientId(clientId);

        // then
        assertTrue(books.isEmpty());
    }
}
