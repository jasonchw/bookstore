package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.payload.SearchRequest;
import com.example.bookstore.repository.GoogleBookRepository;
import com.example.bookstore.repository.SearchHistoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final GoogleBookRepository googleBookRepository;
    private final SearchHistoryRepository searchHistoryRepository;

    @Autowired
    public BookService(GoogleBookRepository googleBookRepository, SearchHistoryRepository searchHistoryRepository) {
        this.googleBookRepository = googleBookRepository;
        this.searchHistoryRepository = searchHistoryRepository;
    }

    public List<Book> findByAuthorName(String clientId, SearchRequest searchRequest) {
        logger.debug("searchRequest: {}", searchRequest);

        if (!hasAuthorName(searchRequest)) {
            return Collections.emptyList();
        }

        List<Book> books = googleBookRepository.findByAuthorName(searchRequest);
        searchHistoryRepository.save(clientId, books);

        return books;
    }

    private boolean hasAuthorName(SearchRequest searchRequest) {
        return searchRequest != null && StringUtils.isNotBlank(searchRequest.getAuthorName());
    }

    public List<Book> findSearchHistoriesByClientId(String clientId) {
        List<Book> books = searchHistoryRepository.findByClientId(clientId);
        logger.debug("found {} for clientId: {}", books.size(), clientId);

        return books;
    }
}
