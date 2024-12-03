package test.project.bookshop.service;

import test.project.bookshop.dto.shopping.cart.AddBookToCartDto;
import test.project.bookshop.dto.shopping.cart.CartItemDto;
import test.project.bookshop.dto.shopping.cart.UpdateCartItemRequest;
import test.project.bookshop.model.ShoppingCart;

public interface CartItemService {
    CartItemDto add(AddBookToCartDto bookToCartDto, ShoppingCart cart);

    CartItemDto update(Long cartItemId, UpdateCartItemRequest updateCartItemRequest);

    void delete(Long cartItemId);
}
