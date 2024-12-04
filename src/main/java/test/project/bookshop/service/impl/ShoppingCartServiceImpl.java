package test.project.bookshop.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import test.project.bookshop.dto.shopping.cart.AddBookToCartDto;
import test.project.bookshop.dto.shopping.cart.CartItemDto;
import test.project.bookshop.dto.shopping.cart.CartResponseDto;
import test.project.bookshop.dto.shopping.cart.UpdateCartItemRequest;
import test.project.bookshop.exception.EntityNotFoundException;
import test.project.bookshop.mapper.CartItemMapper;
import test.project.bookshop.mapper.ShoppingCartMapper;
import test.project.bookshop.model.CartItem;
import test.project.bookshop.model.ShoppingCart;
import test.project.bookshop.model.User;
import test.project.bookshop.repository.cart.item.CartItemRepository;
import test.project.bookshop.repository.shopping.cart.ShoppingCartRepository;
import test.project.bookshop.service.BookService;
import test.project.bookshop.service.ShoppingCartService;

@Transactional
@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookService bookService;

    @Override
    public void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public CartResponseDto add(AddBookToCartDto bookToCartDto) {
        ShoppingCart cart = getUserCart();
        CartItem cartItem = cartItemMapper.toEntity(
                bookToCartDto,
                cart,
                bookService.findBookById(bookToCartDto.bookId()));

        cart.getCartItems().add(cartItem);
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public CartResponseDto getCart() {
        return shoppingCartMapper.toDto(getUserCart());
    }

    @Override
    public CartItemDto update(Long cartItemId, UpdateCartItemRequest updateCartItemRequest) {
        CartItem cartItem = cartItemRepository.findCartItemById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item with id " + cartItemId + " not found."));
        cartItemMapper.updateCartItemFromDto(updateCartItemRequest, cartItem);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void delete(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new EntityNotFoundException(
                    "Cart item with id " + cartItemId + " does not exist.");
        }
        cartItemRepository.deleteById(cartItemId);
    }

    private ShoppingCart getUserCart() {
        User currentUser =
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return shoppingCartRepository.findShoppingCartByUserId(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user: " + currentUser.getId()));
    }
}
