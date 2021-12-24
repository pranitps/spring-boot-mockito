package com.library.junit.springboottest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @MockBean
    private BookRepository bookRepository;

    Book book_rec_1 = new Book(1L, "book1", "book summary 1", 1);
    Book book_rec_2 = new Book(2L, "book2", "book summary 2", 2);
    Book book_rec_3 = new Book(3L, "book3", "book summary 3", 3);

    @Test
    public void getAllListOfBooks_success() throws Exception {
        List<Book> bookList = new ArrayList<>(Arrays.asList(book_rec_1, book_rec_2, book_rec_3));

        when(bookRepository.findAll()).thenReturn(bookList);

        mockMvc.perform(get("/book")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("book3")));
    }

    @Test
    public void getBookById_success() throws Exception {
        when(bookRepository.findById(book_rec_1.getBookId())).thenReturn(java.util.Optional.of(book_rec_1));

        mockMvc.perform(get("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("book1")));
    }

    @Test
    public void createBookRec_success() throws Exception {
        Book book = Book.builder()
                .bookId(4L)
                .name("Book 4")
                .summary("Book Summary 4")
                .rating(4).build();

        when(bookRepository.save(book)).thenReturn(book);

        String content = objectWriter.writeValueAsString(book);

        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Book 4")));;


    }
}
