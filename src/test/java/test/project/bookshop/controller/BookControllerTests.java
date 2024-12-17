package test.project.bookshop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import test.project.bookshop.dto.book.BookDto;
import test.project.bookshop.dto.book.BookRequestDto;
import test.project.bookshop.dto.book.BookWithoutCategoryIdsDto;
import test.project.bookshop.service.BookService;
import test.project.bookshop.utils.JwtUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTests {
    protected static MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all books successfully")
    void getAllBooks_Success() throws Exception {
        List<BookDto> books = List.of(getBookDto(1L), getBookDto(2L));
        Page<BookDto> page = new PageImpl<>(books);

        String expectedResponseJson = objectMapper.writeValueAsString(page);

        when(bookService.findAll(any())).thenReturn(page);

        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponseJson, actualResponse);

        verify(bookService).findAll(any());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Given a valid book ID, retrieve the corresponding book")
    void getBookById_BookExists_ReturnsExpectedBook() throws Exception {
        Long bookId = 1L;
        BookWithoutCategoryIdsDto expectedBook = new BookWithoutCategoryIdsDto();
        expectedBook.setId(bookId);
        expectedBook.setTitle("Effective Java");
        expectedBook.setAuthor("Joshua Bloch");
        expectedBook.setIsbn("978-0134685991");
        expectedBook.setPrice(BigDecimal.valueOf(45.00));
        expectedBook.setDescription("A comprehensive guide to programming in Java");
        expectedBook.setCoverImage("cover-image-url");

        String expectedResponse = objectMapper.writeValueAsString(expectedBook);

        when(bookService.findById(bookId)).thenReturn(expectedBook);

        MvcResult result = mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Search books successfully")
    void searchBooks_Success() throws Exception {
        Page<BookDto> page = new PageImpl<>(List.of(getBookDto(1L), getBookDto(2L)));
        String expectedResponseJson = objectMapper.writeValueAsString(page);

        when(bookService.search(any(), any())).thenReturn(page);

        MvcResult result = mockMvc.perform(get("/books/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponseJson, actualResponse);

        verify(bookService).search(any(), any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Given valid book details, create a new book successfully")
    void createBook_ValidRequest_ReturnsCreatedBook() throws Exception {
        BookRequestDto requestDto = getBookRequestDto();
        BookDto expectedResponse = getBookDto(1L);
        String expectedResponseJson = objectMapper.writeValueAsString(expectedResponse);

        when(bookService.save(any())).thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponseJson, actualResponse);

        verify(bookService).save(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete book successfully")
    void deleteBook_Success() throws Exception {
        Long bookId = 1L;

        doNothing().when(bookService).delete(bookId);

        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        verify(bookService).delete(bookId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update book successfully")
    void updateBook_Success() throws Exception {
        Long bookId = 1L;
        BookRequestDto requestDto = getBookRequestDto();
        BookDto responseDto = getBookDto(bookId);
        String expectedResponseJson = objectMapper.writeValueAsString(responseDto);

        when(bookService.update(eq(bookId), any())).thenReturn(responseDto);

        MvcResult result = mockMvc.perform(put("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponseJson, actualResponse);

        verify(bookService).update(eq(bookId), any());
    }

    private BookRequestDto getBookRequestDto() {
        BookRequestDto requestDto = new BookRequestDto();
        requestDto.setTitle("Effective Java");
        requestDto.setAuthor("Joshua Bloch");
        requestDto.setIsbn("978-0134685991");
        requestDto.setDescription("A comprehensive guide to programming in Java");
        requestDto.setCoverImage("cover-image-url");
        requestDto.setPrice(BigDecimal.valueOf(45.00));
        requestDto.setCategoryIds(List.of(1L, 2L));
        return requestDto;
    }

    private BookDto getBookDto(Long id) {
        BookDto bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setTitle("Effective Java");
        bookDto.setAuthor("Joshua Bloch");
        bookDto.setIsbn("978-0134685991");
        bookDto.setDescription("A comprehensive guide to programming in Java");
        bookDto.setCoverImage("cover-image-url");
        bookDto.setPrice(BigDecimal.valueOf(45.00));
        bookDto.setCategoryIds(Set.of(1L, 2L));
        return bookDto;
    }
}
