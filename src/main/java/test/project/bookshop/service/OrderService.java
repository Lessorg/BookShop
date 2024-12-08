package test.project.bookshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import test.project.bookshop.dto.order.OrderItemResponseDto;
import test.project.bookshop.dto.order.OrderResponseDto;
import test.project.bookshop.dto.order.UpdateOrderStatusRequest;
import test.project.bookshop.model.User;

public interface OrderService {

    OrderResponseDto placeOrder(User user);

    Page<OrderResponseDto> getOrderHistory(Long id, Pageable pageable);

    Page<OrderItemResponseDto> getOrderItems(Long orderId, Pageable pageable);

    OrderItemResponseDto getOrderItem(Long orderId, Long itemId);

    OrderResponseDto updateOrderStatus(Long id, UpdateOrderStatusRequest updateRequest);
}
