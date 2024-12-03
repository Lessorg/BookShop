package test.project.bookshop.dto.shopping.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddBookToCartDto(
        @NotNull
        Long bookId,
        @Min(value = 1)
        int quantity
) {
}
