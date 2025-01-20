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
import test.project.bookshop.dto.category.CategoryDto;
import test.project.bookshop.dto.category.CategoryRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/db/add-test-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/clean-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CategoryControllerTests {
    public static final long NONEXISTENT_CATEGORY_ID = 999L;
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
    @DisplayName("Get all categories successfully")
    void getAllCategories_Success() throws Exception {
        List<CategoryDto> categories = createCategoryDtoList();
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<CategoryDto> page = new PageImpl<>(categories, pageRequest, categories.size());

        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get books by category ID successfully")
    void getBooksByCategoryId_Success() throws Exception {
        Long categoryId = 1L;
        List<BookDto> books = createBookDtos();
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BookDto> page = new PageImpl<>(books, pageRequest, books.size());

        mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get category by ID successfully")
    void getCategoryById_Success() throws Exception {
        Long categoryId = 1L;
        CategoryDto category = createCategoryDto(categoryId);

        mockMvc.perform(get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(category)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create a category successfully")
    void createCategory_Success() throws Exception {
        Long categoryId = 4L;
        CategoryRequestDto requestDto = createCategoryRequestDto();
        CategoryDto expectedCategory = createExpectedCategoryDto(categoryId);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedCategory)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete a category successfully")
    void deleteCategory_Success() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(delete("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update a category successfully")
    void updateCategory_Success() throws Exception {
        Long categoryId = 1L;
        CategoryRequestDto requestDto = createCategoryRequestDto();
        CategoryDto updatedCategory = createExpectedCategoryDto(categoryId);

        mockMvc.perform(put("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedCategory)));
    }

    @Test
    @DisplayName("Unauthorized user cannot create a category")
    void createCategory_UnauthorizedUser_Forbidden() throws Exception {
        CategoryRequestDto requestDto = createCategoryRequestDto();

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Fail to create category with invalid input")
    void createCategory_InvalidRequest_BadRequest() throws Exception {
        CategoryRequestDto invalidRequestDto = new CategoryRequestDto("", "");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Fail to retrieve a category with a nonexistent ID")
    void getCategoryById_NonexistentId_NotFound() throws Exception {
        Long nonexistentCategoryId = NONEXISTENT_CATEGORY_ID;

        mockMvc.perform(get("/categories/{id}", nonexistentCategoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Fail to retrieve books for a nonexistent category")
    void getBooksByCategoryId_NonexistentId_NotFound() throws Exception {
        Long nonexistentCategoryId = NONEXISTENT_CATEGORY_ID;

        mockMvc.perform(get("/categories/{id}/books", nonexistentCategoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Fail to update a nonexistent category")
    void updateCategory_NonexistentId_NotFound() throws Exception {
        Long nonexistentCategoryId = NONEXISTENT_CATEGORY_ID;
        CategoryRequestDto requestDto = createCategoryRequestDto();

        mockMvc.perform(put("/categories/{id}", nonexistentCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Fail to delete a nonexistent category")
    void deleteCategory_NonexistentId_NotFound() throws Exception {
        Long nonexistentCategoryId = NONEXISTENT_CATEGORY_ID;

        mockMvc.perform(delete("/categories/{id}", nonexistentCategoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Fail to update category with invalid input")
    void updateCategory_InvalidRequest_BadRequest() throws Exception {
        Long categoryId = 1L;
        CategoryRequestDto invalidRequestDto = new CategoryRequestDto("", "");

        mockMvc.perform(put("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());
    }

    private List<CategoryDto> createCategoryDtoList() {
        return List.of(
                new CategoryDto(1L,
                        "Fiction", "Books that contain fictional stories and characters."),
                new CategoryDto(2L,
                        "Science", "Books that explore scientific concepts and discoveries."),
                new CategoryDto(3L,
                        "History", "Books that delve into historical events and figures.")
        );
    }

    private List<BookDto> createBookDtos() {
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
        book2.setId(4L);
        book2.setTitle("The Great Gatsby");
        book2.setAuthor("F. Scott Fitzgerald");
        book2.setIsbn("978-0-7432-7356-5");
        book2.setDescription("A novel about the American dream and the Roaring Twenties.");
        book2.setCoverImage("http://example.com/the_great_gatsby.jpg");
        book2.setPrice(BigDecimal.valueOf(10.99));
        book2.setCategoryIds(Set.of(1L));

        BookDto book3 = new BookDto();
        book3.setId(5L);
        book3.setTitle("Moby Dick");
        book3.setAuthor("Herman Melville");
        book3.setIsbn("978-0-14-243724-7");
        book3.setDescription("A novel about the obsessive quest of Ahab for revenge on Moby Dick.");
        book3.setCoverImage("http://example.com/moby_dick.jpg");
        book3.setPrice(BigDecimal.valueOf(11.99));
        book3.setCategoryIds(Set.of(1L));

        return List.of(book1, book2, book3);
    }

    private CategoryRequestDto createCategoryRequestDto() {
        return new CategoryRequestDto("Test", "Test description");
    }

    private CategoryDto createExpectedCategoryDto(Long id) {
        return new CategoryDto(id, "Test", "Test description");
    }

    private CategoryDto createCategoryDto(Long id) {
        return createCategoryDtoList()
                .stream()
                .filter(category -> category.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category with ID " + id + " not found"));
    }
}
