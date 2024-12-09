package test.project.bookshop.dto.order;

import jakarta.validation.constraints.NotNull;

public record CreateOrderRequestDto(
        @NotNull
        String shippingAddress
) {
}
