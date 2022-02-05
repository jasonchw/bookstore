package com.example.bookstore.repository;

import com.example.bookstore.config.properties.GoogleApiConfigurationProperties;
import com.example.bookstore.lang.ServiceException;
import com.example.bookstore.model.Book;
import com.example.bookstore.payload.GoogleBookGetResponse;
import com.example.bookstore.payload.ImmutableGoogleBookGetResponse;
import com.example.bookstore.payload.ImmutableSearchRequest;
import com.example.bookstore.payload.SearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class GoogleBookRepositoryMockTest {
    private GoogleBookRepository googleBookRepository;

    @Mock
    private RestTemplate mockRestTemplate;

    @BeforeEach
    public void beforeEach() {
        openMocks(this);

        GoogleApiConfigurationProperties mockGoogleApiConfigurationProperties = new GoogleApiConfigurationProperties(
                "baseUrl", "pathBookGet", "paramMaxResultsBookGet"
        );

        googleBookRepository = new GoogleBookRepository(mockGoogleApiConfigurationProperties, mockRestTemplate);
    }

    @Test
    public void findByAuthorNameSuccess() {
        // given
        final String title = "Title 1";
        final String author = "Author";
        final List<String> authors = Arrays.asList(author + " 1");
        final ResponseEntity<GoogleBookGetResponse> response = ResponseEntity.ok(
                ImmutableGoogleBookGetResponse.builder()
                        .addItems(
                                ImmutableGoogleBookGetResponse.Item.builder()
                                        .volumeInfo(
                                                ImmutableGoogleBookGetResponse.VolumeInfo.builder()
                                                        .title(title)
                                                        .addAllAuthors(authors)
                                                        .build()
                                        )
                                        .build()
                        )
                        .build()
        );

        when(mockRestTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenReturn(response);

        // when
        final SearchRequest searchRequest = ImmutableSearchRequest.builder().authorName(author).build();
        List<Book> books = googleBookRepository.findByAuthorName(searchRequest);

        // then
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        assertEquals(title, books.get(0).getTitle());
        assertEquals(authors, books.get(0).getAuthors());
        verify(mockRestTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class));
    }

    @Test
    public void findByAuthorNameWithServiceException() {
        // given
        final String author = "Author";
        final HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST);

        when(mockRestTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenThrow(exception);

        // when
        try {
            final SearchRequest searchRequest = ImmutableSearchRequest.builder().authorName(author).build();
            List<Book> books = googleBookRepository.findByAuthorName(searchRequest);
            fail("should throw ServiceException");
        } catch (ServiceException e) {
            // then
            assertEquals("error." + HttpStatus.BAD_REQUEST, e.getErrorCode());
        }

        // then
        verify(mockRestTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class));
    }

    @Test
    public void findByAuthorNameWithNullResponseFromGoogleBookGet() {
        // given
        final String author = "Author";
        final ResponseEntity<GoogleBookGetResponse> response = ResponseEntity.ok(null);

        when(mockRestTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenReturn(response);

        // when
        final SearchRequest searchRequest = ImmutableSearchRequest.builder().authorName(author).build();
        List<Book> books = googleBookRepository.findByAuthorName(searchRequest);

        // then
        assertTrue(books.isEmpty());
        verify(mockRestTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class));
    }
}
