package test.project.bookshop.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.project.bookshop.dto.shopping.cart.AddBookToCartDto;
import test.project.bookshop.dto.shopping.cart.CartItemDto;
import test.project.bookshop.dto.shopping.cart.UpdateCartItemRequest;
import test.project.bookshop.exception.EntityNotFoundException;
import test.project.bookshop.mapper.CartItemMapper;
import test.project.bookshop.model.CartItem;
import test.project.bookshop.model.ShoppingCart;
import test.project.bookshop.repository.cart.item.CartItemRepository;
import test.project.bookshop.service.BookService;
import test.project.bookshop.service.CartItemService;

@Transactional
@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookService bookService;

    @Override
    public CartItemDto add(AddBookToCartDto bookToCartDto, ShoppingCart cart) {
        CartItem cartItem = cartItemMapper.toEntity(
                bookToCartDto,
                cart,
                bookService.findBookById(bookToCartDto.bookId()));

        cart.getCartItems().add(cartItem);
        cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public CartItemDto update(Long cartItemId, UpdateCartItemRequest updateCartItemRequest) {
        CartItem cartItem = cartItemRepository.findCartItemById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item with id "
                        + cartItemId + " not found."));
        cartItemMapper.updateCartItemFromDto(updateCartItemRequest, cartItem);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void delete(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new EntityNotFoundException("Cart item with id "
                    + cartItemId + " does not exist.");
        }
        cartItemRepository.deleteById(cartItemId);
    }
}
