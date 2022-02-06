package com.example.bookstore.rest;

import com.example.bookstore.model.Book;
import com.example.bookstore.payload.ImmutableSearchRequest;
import com.example.bookstore.payload.SearchRequest;
import com.example.bookstore.service.BookService;
import com.example.bookstore.util.BookUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.example.bookstore.model.Result.Status.SUCCESS;
import static com.example.bookstore.rest.BookController.HEADER_CLIENT_ID;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest {
    @Mock
    private BookService mockBookService;

    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
    }

    // extract to parent class when more Controller tests are added
    public Object getController() {
        return new BookController(mockBookService);
    }

    @Test
    void search() throws Exception {
        // given
        final String clientId = RandomStringUtils.randomAlphabetic(10);
        final String author = "Author";
        final SearchRequest searchRequest = ImmutableSearchRequest.builder().authorName(author).build();
        final int expectedCount = 2;
        final List<Book> books = BookUtils.getBooks(expectedCount);

        when(mockBookService.findByAuthorName(anyString(), any(SearchRequest.class))).thenReturn(books);

        // when + then
        mockMvc.perform(post("/services/books/v1/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_CLIENT_ID, clientId)
                        .content("{}")) // don't care about request body
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(SUCCESS.name()))
                .andExpect(jsonPath("$.payload", hasSize(expectedCount)))
                .andReturn();
    }

    @Test
    void getSearchHistories() throws Exception {
        // given
        final String clientId = RandomStringUtils.randomAlphabetic(10);
        final int expectedCount = 2;
        final List<Book> books = BookUtils.getBooks(expectedCount);

        when(mockBookService.findSearchHistoriesByClientId(anyString())).thenReturn(books);

        // when + then
        mockMvc.perform(get("/services/books/v1/search/histories")
                        .header(HEADER_CLIENT_ID, clientId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(SUCCESS.name()))
                .andReturn();
    }
}
