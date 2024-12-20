package test.project.bookshop.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import test.project.bookshop.dto.book.BookDto;
import test.project.bookshop.dto.book.BookRequestDto;
import test.project.bookshop.dto.book.BookWithoutCategoryIdsDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/db/add-test-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/clean-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookControllerTests {

    protected static MockMvc mockMvc;

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
        List<BookDto> books = createBookDtoList();
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BookDto> page = new PageImpl<>(books, pageRequest, books.size());

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Given a valid book ID, retrieve the corresponding book")
    void getBookById_BookExists_ReturnsExpectedBook() throws Exception {
        Long bookId = 1L;
        BookWithoutCategoryIdsDto expectedBook = createBookWithoutCategoryIdsDto(bookId);

        mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBook)));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Search books successfully")
    void searchBooks_Success() throws Exception {
        List<BookDto> books = List.of(createBookDto(2L));
        Page<BookDto> page = new PageImpl<>(books, PageRequest.of(0, 10), books.size());

        mockMvc.perform(get("/books/search")
                        .param("authors", "George Orwell")
                        .param("titles", "1984")
                        .param("isbns", "978-0-452-28423-4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Given valid book details, create a new book successfully")
    void createBook_ValidRequest_ReturnsCreatedBook() throws Exception {
        Long newBookId = 6L;
        BookRequestDto requestDto = getBookRequestDto();
        BookDto expectedBook = createBookDtoFromRequest(newBookId, requestDto);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBook)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete book successfully")
    void deleteBook_Success() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update book successfully")
    void updateBook_Success() throws Exception {
        Long bookId = 1L;
        BookRequestDto requestDto = getBookRequestDto();
        BookDto updatedBook = createBookDtoFromRequest(bookId, requestDto);

        mockMvc.perform(put("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedBook)));
    }

    private List<BookDto> createBookDtoList() {
        BookDto book1 = new BookDto();
        book1.setId(1L);
        book1.setTitle("Pride and Prejudice");
        book1.setAuthor("Jane Austen");
        book1.setIsbn("978-3-16-148410-0");
        book1.setDescription("A classic novel about love and social standing.");
        book1.setCoverImage("http://example.com/pride_and_prejudice.jpg");
        book1.setPrice(BigDecimal.valueOf(9.99));
        book1.setCategoryIds(Set.of(1L));

        BookDto book2 = new BookDto();
        book2.setId(2L);
        book2.setTitle("1984");
        book2.setAuthor("George Orwell");
        book2.setIsbn("978-0-452-28423-4");
        book2.setDescription("A dystopian novel set in a totalitarian society.");
        book2.setCoverImage("http://example.com/1984.jpg");
        book2.setPrice(BigDecimal.valueOf(14.99));
        book2.setCategoryIds(Set.of(2L));

        BookDto book3 = new BookDto();
        book3.setId(3L);
        book3.setTitle("To Kill a Mockingbird");
        book3.setAuthor("Harper Lee");
        book3.setIsbn("978-0-06-112008-4");
        book3.setDescription("A novel about racial injustice in the Deep South.");
        book3.setCoverImage("http://example.com/to_kill_a_mockingbird.jpg");
        book3.setPrice(BigDecimal.valueOf(12.99));
        book3.setCategoryIds(Set.of(3L));

        BookDto book4 = new BookDto();
        book4.setId(4L);
        book4.setTitle("The Great Gatsby");
        book4.setAuthor("F. Scott Fitzgerald");
        book4.setIsbn("978-0-7432-7356-5");
        book4.setDescription("A novel about the American dream and the Roaring Twenties.");
        book4.setCoverImage("http://example.com/the_great_gatsby.jpg");
        book4.setPrice(BigDecimal.valueOf(10.99));
        book4.setCategoryIds(Set.of(1L));

        BookDto book5 = new BookDto();
        book5.setId(5L);
        book5.setTitle("Moby Dick");
        book5.setAuthor("Herman Melville");
        book5.setIsbn("978-0-14-243724-7");
        book5.setDescription("A novel about the obsessive quest of Ahab for revenge on Moby Dick.");
        book5.setCoverImage("http://example.com/moby_dick.jpg");
        book5.setPrice(BigDecimal.valueOf(11.99));
        book5.setCategoryIds(Set.of(1L));

        return List.of(book1, book2, book3, book4, book5);
    }

    private BookDto createBookDto(Long id) {
        return createBookDtoList().stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Book with ID " + id + " not found"));
    }

    private BookDto createBookDtoFromRequest(Long id, BookRequestDto requestDto) {
        BookDto bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setTitle(requestDto.getTitle());
        bookDto.setAuthor(requestDto.getAuthor());
        bookDto.setIsbn(requestDto.getIsbn());
        bookDto.setDescription(requestDto.getDescription());
        bookDto.setCoverImage(requestDto.getCoverImage());
        bookDto.setPrice(requestDto.getPrice());
        bookDto.setCategoryIds(new HashSet<>(requestDto.getCategoryIds()));
        return bookDto;
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

    private BookWithoutCategoryIdsDto createBookWithoutCategoryIdsDto(Long bookId) {
        BookWithoutCategoryIdsDto bookDto = new BookWithoutCategoryIdsDto();
        bookDto.setId(bookId);
        bookDto.setTitle("Pride and Prejudice");
        bookDto.setAuthor("Jane Austen");
        bookDto.setIsbn("978-3-16-148410-0");
        bookDto.setDescription("A classic novel about love and social standing.");
        bookDto.setCoverImage("http://example.com/pride_and_prejudice.jpg");
        bookDto.setPrice(BigDecimal.valueOf(9.99));

        return bookDto;
    }
}
