package test.project.bookshop.dto.shopping.cart;

import jakarta.validation.constraints.Min;

public record UpdateCartItemRequest(
        @Min(value = 1)
        int quantity
) {
}
