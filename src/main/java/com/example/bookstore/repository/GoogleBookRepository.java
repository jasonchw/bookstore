package com.example.bookstore.repository;

import com.example.bookstore.config.properties.GoogleApiConfigurationProperties;
import com.example.bookstore.lang.ServiceException;
import com.example.bookstore.model.Book;
import com.example.bookstore.payload.GoogleBookGetResponse;
import com.example.bookstore.payload.SearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Repository
public class GoogleBookRepository {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final GoogleApiConfigurationProperties googleApiConfigurationProperties;
    private final RestTemplate restTemplate;

    @Autowired
    public GoogleBookRepository(GoogleApiConfigurationProperties googleApiConfigurationProperties,
                                RestTemplate restTemplate) {
        this.googleApiConfigurationProperties = googleApiConfigurationProperties;
        this.restTemplate = restTemplate;
    }

    public List<Book> findByAuthorName(SearchRequest searchRequest) {
        logger.debug("searchRequest: {}", searchRequest);

        String searchTerm = String.format("inauthor:%s", searchRequest.getAuthorName());

        String baseUrl = googleApiConfigurationProperties.getBaseUrl();
        String path = googleApiConfigurationProperties.getPathBookGet();

        String uri = UriComponentsBuilder.fromUriString(baseUrl)
                .path(path)
                .queryParam("q", searchTerm)
                .queryParam("maxResults", googleApiConfigurationProperties.getParamMaxResultsBookGet())
                .build()
                .toUriString();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<GoogleBookGetResponse> response = null;

        try {
            response = restTemplate.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<GoogleBookGetResponse>() {
            });
            logger.debug("GET {}, searchTerm={}, status: {}", path, searchTerm, response.getStatusCode());
            logger.trace("response: {}", response);
        } catch (HttpStatusCodeException e) {
            HttpStatus httpStatus = e.getStatusCode();
            String responseBody = e.getResponseBodyAsString();

            logger.error("Failed to process request GET {}, searchTerm={}, response code {}, response body {}",
                    path, searchTerm, httpStatus, responseBody);

            throw new ServiceException("error." + httpStatus, responseBody, e);
        }

        GoogleBookGetResponse googleBookGetResponse = response.getBody();
        if (googleBookGetResponse == null) {
            logger.error("received null response for GET {}, searchTerm={}", path, searchTerm);
            return Collections.emptyList();
        }

        List<Book> books = GoogleBookGetResponse.to(googleBookGetResponse.getItems());
        logger.debug("found {} books matching searchRequest: {}", books.size(), searchRequest);
        logger.trace("books: {}", books);

        return books;
    }
}
