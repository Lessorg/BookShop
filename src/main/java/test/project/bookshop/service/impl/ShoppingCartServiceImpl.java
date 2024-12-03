package test.project.bookshop.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.project.bookshop.dto.shopping.cart.AddBookToCartDto;
import test.project.bookshop.dto.shopping.cart.CartItemDto;
import test.project.bookshop.dto.shopping.cart.ResponseCartDto;
import test.project.bookshop.dto.shopping.cart.UpdateCartItemRequest;
import test.project.bookshop.exception.EntityNotFoundException;
import test.project.bookshop.mapper.ShoppingCartMapper;
import test.project.bookshop.model.ShoppingCart;
import test.project.bookshop.model.User;
import test.project.bookshop.repository.shopping.cart.ShoppingCartRepository;
import test.project.bookshop.service.CartItemService;
import test.project.bookshop.service.ShoppingCartService;
import test.project.bookshop.service.UserService;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserService userService;
    private final CartItemService cartItemService;

    @Transactional
    @Override
    public ResponseCartDto add(AddBookToCartDto bookToCartDto) {
        ShoppingCart cart = getUserCart();
        cartItemService.add(bookToCartDto, cart);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ResponseCartDto getContent() {
        return shoppingCartMapper.toDto(getUserCart());
    }

    @Transactional
    @Override
    public CartItemDto update(Long cartItemId, UpdateCartItemRequest updateCartItemRequest) {
        return cartItemService.update(cartItemId, updateCartItemRequest);
    }

    @Override
    public void delete(Long cartItemId) {
        cartItemService.delete(cartItemId);
    }

    private ShoppingCart getUserCart() {
        User currentUser = userService.getCurrentUser();
        return shoppingCartRepository.findShoppingCartByUserId(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user: " + currentUser.getId()));
    }
}
