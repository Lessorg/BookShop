package test.project.bookshop.dto.shopping.cart;

import java.util.List;

public record CartResponseDto(
        Long id,
        Long userId,
        List<CartItemDto> cartItems
) {
}
