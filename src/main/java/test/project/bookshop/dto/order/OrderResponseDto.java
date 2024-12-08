package test.project.bookshop.dto.order;

import java.time.LocalDateTime;
import java.util.List;
import test.project.bookshop.model.Order;

public record OrderResponseDto(
        Long id,
        Long userId,
        List<OrderItemResponseDto> orderItems,
        LocalDateTime orderDate,
        double total,
        Order.Status status
) {
}
