package test.project.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.project.bookshop.dto.shopping.cart.AddBookToCartDto;
import test.project.bookshop.dto.shopping.cart.CartItemDto;
import test.project.bookshop.dto.shopping.cart.CartResponseDto;
import test.project.bookshop.dto.shopping.cart.UpdateCartItemRequest;
import test.project.bookshop.service.ShoppingCartService;

@Tag(name = "Shopping Cart", description = "Endpoints for managing shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @Operation(
            summary = "Add book to shopping cart",
            description = "Add a book to the user's shopping cart")
    public CartResponseDto addBook(@RequestBody AddBookToCartDto bookToCartDto) {
        return shoppingCartService.add(bookToCartDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(
            summary = "Get shopping cart contents",
            description = "Retrieve the current user's shopping cart contents")
    public CartResponseDto getCartContents() {
        return shoppingCartService.getCart();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/items/{cartItemId}")
    @Operation(
            summary = "Update quantity of an item in the cart",
            description = "Update the quantity of a specific item in the user's shopping cart")
    public CartItemDto updateQuantity(
            @PathVariable Long cartItemId,
            @RequestBody UpdateCartItemRequest updateCartItemRequest) {
        return shoppingCartService.update(cartItemId, updateCartItemRequest);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/items/{cartItemId}")
    @Operation(
            summary = "Delete an item from the cart",
            description = "Remove a specific item from the user's shopping cart")
    public void deleteItem(@PathVariable Long cartItemId) {
        shoppingCartService.delete(cartItemId);
    }
}