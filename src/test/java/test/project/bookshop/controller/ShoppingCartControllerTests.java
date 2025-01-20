package test.project.bookshop.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import test.project.bookshop.config.WithMockCustomUser;
import test.project.bookshop.dto.shopping.cart.AddBookToCartDto;
import test.project.bookshop.dto.shopping.cart.CartItemDto;
import test.project.bookshop.dto.shopping.cart.CartResponseDto;
import test.project.bookshop.dto.shopping.cart.UpdateCartItemRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/db/add-test-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/add-shopping-cart.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/clean-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ShoppingCartControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockCustomUser
    @DisplayName("Add book to shopping cart successfully")
    void addBookToCart_Success() throws Exception {
        AddBookToCartDto requestDto = new AddBookToCartDto(2L, 2);
        CartResponseDto expectedResponse = createAddCartResponseDto();

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Retrieve shopping cart contents successfully")
    void getCartContents_Success() throws Exception {
        CartResponseDto expectedResponse = createCartResponseDto();

        mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Update item quantity in shopping cart successfully")
    void updateCartItem_Success() throws Exception {
        Long cartItemId = 1L;
        UpdateCartItemRequest requestDto = new UpdateCartItemRequest(3);
        CartItemDto expectedItem = new CartItemDto(cartItemId, 1L, "Pride and Prejudice", 3);

        mockMvc.perform(put("/cart/items/{cartItemId}", cartItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedItem)));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Delete item from shopping cart successfully")
    void deleteCartItem_Success() throws Exception {
        Long cartItemId = 1L;

        mockMvc.perform(delete("/cart/items/{cartItemId}", cartItemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Fail to add book to cart with invalid input")
    void addBookToCart_InvalidInput_BadRequest() throws Exception {
        AddBookToCartDto invalidRequest = new AddBookToCartDto(null, -1);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Add book to shopping cart with invalid book ID")
    void addBookToCart_InvalidBookId_NotFound() throws Exception {
        Long nonExistingBookId = 99L;
        AddBookToCartDto requestDto = new AddBookToCartDto(nonExistingBookId, 2);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Can't find book by id: "
                        + nonExistingBookId));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Add book to shopping cart with invalid quantity")
    void addBookToCart_InvalidQuantity_BadRequest() throws Exception {
        AddBookToCartDto requestDto = new AddBookToCartDto(1L, 0);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Update item quantity with invalid cart item ID")
    void updateCartItem_InvalidCartItemId_NotFound() throws Exception {
        Long invalidCartItemId = 99L;
        UpdateCartItemRequest requestDto = new UpdateCartItemRequest(5);

        mockMvc.perform(put("/cart/items/{cartItemId}", invalidCartItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Cart item with id " + invalidCartItemId + " not found."));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Delete item from cart with invalid cart item ID")
    void deleteCartItem_InvalidCartItemId_NotFound() throws Exception {
        Long invalidCartItemId = 99L;

        mockMvc.perform(delete("/cart/items/{cartItemId}", invalidCartItemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cart item with id "
                        + invalidCartItemId + " does not exist."));
    }

    @Test
    @DisplayName("Add book to cart without authentication")
    void addBookToCart_Unauthenticated_Unauthorized() throws Exception {
        AddBookToCartDto requestDto = new AddBookToCartDto(1L, 1);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());
    }

    private CartResponseDto createCartResponseDto() {
        CartItemDto cartItem = new CartItemDto(1L, 1L, "Pride and Prejudice", 2);
        return new CartResponseDto(1L, 1L, List.of(cartItem));
    }

    private CartResponseDto createAddCartResponseDto() {
        CartItemDto cartItem1 = new CartItemDto(1L, 1L, "Pride and Prejudice", 2);
        CartItemDto cartItem2 = new CartItemDto(2L, 2L, "1984", 2);
        return new CartResponseDto(1L, 1L, List.of(cartItem1, cartItem2));
    }
}

