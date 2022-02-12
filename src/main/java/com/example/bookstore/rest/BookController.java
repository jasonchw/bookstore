package com.example.bookstore.rest;

import com.example.bookstore.model.Book;
import com.example.bookstore.payload.Result;
import com.example.bookstore.payload.SearchRequest;
import com.example.bookstore.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/services")
public class BookController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String HEADER_CLIENT_ID = "X-Client-Id";

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(value = "/books/v1/search", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:8081") // TODO remove once front/back ends are using "same" origin
    public ResponseEntity<Result<List<Book>>> search(@RequestHeader(HEADER_CLIENT_ID) String clientId,
                                                     @RequestBody SearchRequest searchRequest) {
        logger.debug("clientId: {}, searchRequest: {}", clientId, searchRequest);

        List<Book> books = bookService.findByAuthorName(clientId, searchRequest);
        Result<List<Book>> result = Book.toResultSuccess(books);
        logger.trace("result: {}", result);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/books/v1/search/histories", produces = APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:8081") // TODO remove once front/back ends are using "same" origin
    public ResponseEntity<Result<List<Book>>> getSearchHistories(@RequestHeader(HEADER_CLIENT_ID) String clientId) {
        logger.debug("clientId: {}", clientId);

        List<Book> books = bookService.findSearchHistoriesByClientId(clientId);
        Result<List<Book>> result = Book.toResultSuccess(books);
        logger.trace("result: {}", result);

        return ResponseEntity.ok(result);
    }
}
