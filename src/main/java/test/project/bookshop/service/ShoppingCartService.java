package test.project.bookshop.service;

import test.project.bookshop.dto.shopping.cart.AddBookToCartDto;
import test.project.bookshop.dto.shopping.cart.CartItemDto;
import test.project.bookshop.dto.shopping.cart.CartResponseDto;
import test.project.bookshop.dto.shopping.cart.UpdateCartItemRequest;
import test.project.bookshop.model.User;

public interface ShoppingCartService {
    void createShoppingCartForUser(User user);

    CartResponseDto add(AddBookToCartDto bookToCartDto);

    CartResponseDto getCart();

    CartItemDto update(Long cartItemId, UpdateCartItemRequest updateCartItemRequest);

    void delete(Long cartItemId);
}
