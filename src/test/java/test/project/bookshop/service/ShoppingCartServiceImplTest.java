package test.project.bookshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.project.bookshop.dto.shopping.cart.AddBookToCartDto;
import test.project.bookshop.dto.shopping.cart.CartItemDto;
import test.project.bookshop.dto.shopping.cart.CartResponseDto;
import test.project.bookshop.dto.shopping.cart.UpdateCartItemRequest;
import test.project.bookshop.exception.EntityNotFoundException;
import test.project.bookshop.mapper.CartItemMapper;
import test.project.bookshop.mapper.ShoppingCartMapper;
import test.project.bookshop.model.Book;
import test.project.bookshop.model.CartItem;
import test.project.bookshop.model.ShoppingCart;
import test.project.bookshop.model.User;
import test.project.bookshop.repository.CartItemRepository;
import test.project.bookshop.repository.ShoppingCartRepository;
import test.project.bookshop.service.impl.ShoppingCartServiceImpl;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
    private static final Long USER_ID = 1L;
    private static final Long BOOK_ID = 2L;
    private static final Long CART_ITEM_ID = 1L;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private BookService bookService;

    @Test
    @DisplayName("Create a shopping cart for a user")
    void createShoppingCartForUser_Success() {
        User user = new User();

        shoppingCartService.createShoppingCartForUser(user);

        ArgumentCaptor<ShoppingCart> captor = ArgumentCaptor.forClass(ShoppingCart.class);
        verify(shoppingCartRepository).save(captor.capture());

        ShoppingCart capturedCart = captor.getValue();
        assertEquals(user, capturedCart.getUser(),
                "The shopping cart's user "
                        + capturedCart.getUser() + " should match the given user: " + user);
    }

    @Test
    @DisplayName("Add book to cart successfully")
    void addBookToCart_Success() {
        AddBookToCartDto addBookToCartDto = new AddBookToCartDto(BOOK_ID, 1);
        ShoppingCart cart = getShoppingCart();
        CartItem cartItem = getCartItem(addBookToCartDto);
        Book book = getBook();
        CartItemDto cartItemDto = new CartItemDto(CART_ITEM_ID, BOOK_ID, "Test Book", 1);
        CartResponseDto expectedDto = new CartResponseDto(1L, USER_ID, List.of(cartItemDto));

        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));
        when(cartItemMapper.toEntity(addBookToCartDto)).thenReturn(cartItem);
        when(bookService.findBookById(BOOK_ID)).thenReturn(book);
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);
        CartResponseDto result = shoppingCartService.add(USER_ID, addBookToCartDto);

        assertEquals(expectedDto, result);
        verify(shoppingCartRepository).findByUserId(USER_ID);
        verify(cartItemMapper).toEntity(addBookToCartDto);
        verify(bookService).findBookById(BOOK_ID);
        verify(cartItemRepository).save(cartItem);
        verify(shoppingCartMapper).toDto(cart);
    }

    @Test
    @DisplayName("Add book to cart when shopping cart not found")
    void addBookToCart_ShoppingCartNotFound() {
        AddBookToCartDto addBookToCartDto = new AddBookToCartDto(BOOK_ID, 1);

        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.add(USER_ID, addBookToCartDto));

        assertEquals("Shopping cart not found for user: " + USER_ID, exception.getMessage());
        verify(shoppingCartRepository).findByUserId(USER_ID);
    }

    @Test
    @DisplayName("Get cart by user ID successfully")
    void getCartByUserId_Success() {
        ShoppingCart cart = new ShoppingCart();
        CartResponseDto expectedDto = new CartResponseDto(
                1L, USER_ID, List.of(new CartItemDto(
                        1L, 2L, "Sample Book", 2)
        )
        );

        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);
        CartResponseDto result = shoppingCartService.getCartByUserId(USER_ID);

        assertEquals(expectedDto, result);
        verify(shoppingCartRepository).findByUserId(USER_ID);
        verify(shoppingCartMapper).toDto(cart);
    }

    @Test
    @DisplayName("Get cart by user ID when shopping cart not found")
    void getCartByUserId_ShoppingCartNotFound() {
        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.getCartByUserId(USER_ID));
        assertEquals("Shopping cart not found for user: " + USER_ID, exception.getMessage());
        verify(shoppingCartRepository).findByUserId(USER_ID);
    }

    @Test
    @DisplayName("Update cart item successfully")
    void updateCartItem_Success() {
        CartItem cartItem = new CartItem();
        cartItem.setId(CART_ITEM_ID);
        cartItem.setQuantity(2);
        cartItem.setBook(new Book());
        CartItemDto expectedDto = new CartItemDto(null, null, null, 1);

        when(cartItemRepository.findCartItemById(CART_ITEM_ID)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(expectedDto);

        UpdateCartItemRequest updateRequest = new UpdateCartItemRequest(3);
        CartItemDto result = shoppingCartService.update(USER_ID, CART_ITEM_ID, updateRequest);
        assertEquals(expectedDto, result);
        verify(cartItemRepository).findCartItemById(CART_ITEM_ID);
        verify(cartItemMapper).updateCartItemFromDto(updateRequest, cartItem);
        verify(cartItemRepository).save(cartItem);
        verify(cartItemMapper).toDto(cartItem);
    }

    @Test
    @DisplayName("Update cart item when cart item not found")
    void updateCartItem_CartItemNotFound() {
        UpdateCartItemRequest updateRequest = new UpdateCartItemRequest(3);

        when(cartItemRepository.findCartItemById(CART_ITEM_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.update(USER_ID, CART_ITEM_ID, updateRequest));
        assertEquals("Cart item with id " + CART_ITEM_ID + " not found.", exception.getMessage());
        verify(cartItemRepository).findCartItemById(CART_ITEM_ID);
    }

    @Test
    @DisplayName("Delete cart item successfully")
    void deleteCartItem_Success() {
        when(cartItemRepository.existsById(CART_ITEM_ID)).thenReturn(true);

        shoppingCartService.delete(USER_ID, CART_ITEM_ID);

        verify(cartItemRepository).existsById(CART_ITEM_ID);
        verify(cartItemRepository).deleteById(CART_ITEM_ID);
    }

    @Test
    @DisplayName("Delete cart item when cart item not found")
    void deleteCartItem_NotFound() {
        when(cartItemRepository.existsById(CART_ITEM_ID)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.delete(USER_ID, CART_ITEM_ID));
        assertEquals("Cart item with id " + CART_ITEM_ID + " does not exist.",
                exception.getMessage());
        verify(cartItemRepository).existsById(CART_ITEM_ID);
    }

    private Book getBook() {
        Book book = new Book();
        book.setId(BOOK_ID);
        book.setTitle("Test Book");
        return book;
    }

    private CartItem getCartItem(AddBookToCartDto addBookToCartDto) {
        CartItem cartItem = new CartItem();
        cartItem.setId(CART_ITEM_ID);
        cartItem.setQuantity(addBookToCartDto.quantity());
        return cartItem;
    }

    private ShoppingCart getShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setUser(new User());
        cart.setCartItems(new HashSet<>());
        return cart;
    }
}
