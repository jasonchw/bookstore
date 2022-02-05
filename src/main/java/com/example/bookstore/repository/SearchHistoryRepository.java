package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class SearchHistoryRepository {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final Comparator<Book> BOOK_COMPARATOR = Comparator.comparing(Book::getTitle);

    public static final int MAX_CACHE_SIZE_PER_CLIENT = 50;

    // TODO use external cache/data store when deploying multiple instances
    // clientId -> book(title, authors)'s
    private final Map<String, SortedSet<Book>> histories;

    @Autowired
    public SearchHistoryRepository(Map<String, SortedSet<Book>> searchHistoryCache) {
        this.histories = searchHistoryCache;
    }

    public int save(String clientId, List<Book> books) {
        SortedSet<Book> searchedBooks = histories.getOrDefault(clientId, newSearchHistories());
        logger.debug("before save - clientId {} has {} searched books", clientId, searchedBooks.size());

        searchedBooks.addAll(books);
        if (searchedBooks.size() > MAX_CACHE_SIZE_PER_CLIENT) {
            logger.debug("before adjusting cache - clientId {} has {} searched books", clientId, searchedBooks.size());

            List<Book> cachedBooks = new ArrayList<>(searchedBooks);
            cachedBooks.sort(BOOK_COMPARATOR);
            List<Book> cachedBooksCopy = cachedBooks.subList(0, MAX_CACHE_SIZE_PER_CLIENT);
            searchedBooks.retainAll(cachedBooksCopy);

            logger.debug("after adjusting cache - clientId {} has {} searched books", clientId, searchedBooks.size());
        }

        histories.put(clientId, searchedBooks);
        logger.debug("after save - clientId {} has {} searched books", clientId, searchedBooks.size());

        return searchedBooks.size();
    }

    public List<Book> findByClientId(String clientId) {
        SortedSet<Book> searchedBooks = histories.getOrDefault(clientId, newSearchHistories());
        logger.debug("clientId {} has {} searched books", clientId, searchedBooks.size());

        List<Book> books = new ArrayList<>(searchedBooks);
        books.sort(BOOK_COMPARATOR);

        return books;
    }

    private SortedSet<Book> newSearchHistories() {
        return new TreeSet<>(BOOK_COMPARATOR);
    }
}
