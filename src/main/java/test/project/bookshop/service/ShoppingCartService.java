package test.project.bookshop.service;

import test.project.bookshop.dto.shopping.cart.AddBookToCartDto;
import test.project.bookshop.dto.shopping.cart.CartItemDto;
import test.project.bookshop.dto.shopping.cart.CartResponseDto;
import test.project.bookshop.dto.shopping.cart.UpdateCartItemRequest;

public interface ShoppingCartService {
    CartResponseDto add(AddBookToCartDto bookToCartDto);

    CartResponseDto getCart();

    CartItemDto update(Long cartItemId, UpdateCartItemRequest updateCartItemRequest);

    void delete(Long cartItemId);
}
