package test.project.bookshop.service;

import test.project.bookshop.dto.shopping.cart.AddBookToCartDto;
import test.project.bookshop.dto.shopping.cart.CartItemDto;
import test.project.bookshop.dto.shopping.cart.ResponseCartDto;
import test.project.bookshop.dto.shopping.cart.UpdateCartItemRequest;

public interface ShoppingCartService {
    ResponseCartDto add(AddBookToCartDto bookToCartDto);

    ResponseCartDto getContent();

    CartItemDto update(Long cartItemId, UpdateCartItemRequest updateCartItemRequest);

    void delete(Long cartItemId);
}
