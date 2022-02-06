package com.example.bookstore.repository;

import com.example.bookstore.TestContextConfiguration;
import com.example.bookstore.config.properties.GoogleApiConfigurationProperties;
import com.example.bookstore.model.Book;
import com.example.bookstore.payload.ImmutableSearchRequest;
import com.example.bookstore.payload.SearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestContextConfiguration.class})
public class GoogleBookRepositoryTest {
    private GoogleBookRepository googleBookRepository;

    @Autowired
    private GoogleApiConfigurationProperties googleApiConfigurationProperties;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    public void beforeEach() {
        googleBookRepository = new GoogleBookRepository(googleApiConfigurationProperties, restTemplate);
    }

    @Test
    public void findByAuthorName() {
        // given
        final String author = "Mark Twain";

        // when
        final SearchRequest searchRequest = ImmutableSearchRequest.builder().authorName(author).build();
        List<Book> books = googleBookRepository.findByAuthorName(searchRequest);

        // then
        assertFalse(books.isEmpty());
    }
}
