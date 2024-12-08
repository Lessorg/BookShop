package test.project.bookshop.dto.order;

import jakarta.validation.constraints.NotNull;
import test.project.bookshop.model.Order;

public record UpdateOrderStatusRequest(
        @NotNull
        Order.Status status
) {
}
