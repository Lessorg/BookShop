package test.project.bookshop.dto.shopping.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddBookToCartDto(
        @NotNull
        @Positive
        Long bookId,
        @Positive
        int quantity
) {
}
