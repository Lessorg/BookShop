package test.project.bookshop.dto.shopping.cart;

import jakarta.validation.constraints.Positive;

public record UpdateCartItemRequest(
        @Positive
        int quantity
) {
}
