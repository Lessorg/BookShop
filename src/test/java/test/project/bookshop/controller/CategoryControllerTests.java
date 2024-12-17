package test.project.bookshop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
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
import test.project.bookshop.dto.category.CategoryDto;
import test.project.bookshop.dto.category.CategoryRequestDto;
import test.project.bookshop.service.BookService;
import test.project.bookshop.service.CategoryService;
import test.project.bookshop.utils.JwtUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTests {
    protected static MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

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
    @DisplayName("Get all categories successfully")
    void getAllCategories_Success() throws Exception {
        List<CategoryDto> categories = List.of(
                new CategoryDto(1L, "Fiction", "Books in the fiction category"),
                new CategoryDto(2L, "Non-Fiction", "Books in the non-fiction category")
        );
        Page<CategoryDto> page = new PageImpl<>(categories);
        String expectedResponseJson = objectMapper.writeValueAsString(page);

        when(categoryService.findAll(any())).thenReturn(page);

        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponseJson, actualResponse);

        verify(categoryService).findAll(any());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get books by category ID successfully")
    void getBooksByCategoryId_Success() throws Exception {
        Long categoryId = 1L;
        List<BookDto> books = List.of(getBookDto(1L), getBookDto(2L));
        Page<BookDto> page = new PageImpl<>(books);
        String expectedResponseJson = objectMapper.writeValueAsString(page);

        when(bookService.findBooksByCategoryId(eq(categoryId), any())).thenReturn(page);

        MvcResult result = mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponseJson, actualResponse);

        verify(bookService).findBooksByCategoryId(eq(categoryId), any());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get category by ID successfully")
    void getCategoryById_Success() throws Exception {
        Long categoryId = 1L;
        CategoryDto expectedCategory = new CategoryDto(categoryId, "Fiction",
                "Books in the fiction category");
        String expectedResponseJson = objectMapper.writeValueAsString(expectedCategory);

        when(categoryService.getById(categoryId)).thenReturn(expectedCategory);

        MvcResult result = mockMvc.perform(get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponseJson, actualResponse);

        verify(categoryService).getById(categoryId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create a category successfully")
    void createCategory_Success() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto("Fiction",
                "Books in the fiction category");
        CategoryDto expectedCategory = new CategoryDto(1L, "Fiction",
                "Books in the fiction category");
        String expectedResponseJson = objectMapper.writeValueAsString(expectedCategory);

        when(categoryService.save(any())).thenReturn(expectedCategory);

        MvcResult result = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponseJson, actualResponse);

        verify(categoryService).save(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete a category successfully")
    void deleteCategory_Success() throws Exception {
        Long categoryId = 1L;

        doNothing().when(categoryService).deleteById(categoryId);

        mockMvc.perform(delete("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteById(categoryId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update a category successfully")
    void updateCategory_Success() throws Exception {
        Long categoryId = 1L;
        CategoryRequestDto requestDto = new CategoryRequestDto("Fiction Updated",
                "Updated description");
        CategoryDto updatedCategory = new CategoryDto(categoryId, "Fiction Updated",
                "Updated description");
        String expectedResponseJson = objectMapper.writeValueAsString(updatedCategory);

        when(categoryService.update(eq(categoryId), any())).thenReturn(updatedCategory);

        MvcResult result = mockMvc.perform(put("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponseJson, actualResponse);

        verify(categoryService).update(eq(categoryId), any());
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
