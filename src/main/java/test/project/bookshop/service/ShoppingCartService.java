package test.project.bookshop.service;

import test.project.bookshop.dto.shopping.cart.AddBookToCartDto;
import test.project.bookshop.dto.shopping.cart.CartItemDto;
import test.project.bookshop.dto.shopping.cart.CartResponseDto;
import test.project.bookshop.dto.shopping.cart.UpdateCartItemRequest;
import test.project.bookshop.model.User;

public interface ShoppingCartService {
    void createShoppingCartForUser(User user);

    CartResponseDto add(Long userId, AddBookToCartDto bookToCartDto);

    CartResponseDto getCartByUserId(Long userId);

    CartItemDto update(Long userId, Long cartItemId, UpdateCartItemRequest updateCartItemRequest);

    void delete(Long userId, Long cartItemId);
}
