package test.project.bookshop.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import test.project.bookshop.dto.order.OrderItemResponseDto;
import test.project.bookshop.dto.order.OrderResponseDto;
import test.project.bookshop.dto.order.UpdateOrderStatusRequest;
import test.project.bookshop.exception.EntityNotFoundException;
import test.project.bookshop.mapper.OrderItemMapper;
import test.project.bookshop.mapper.OrderMapper;
import test.project.bookshop.model.CartItem;
import test.project.bookshop.model.Order;
import test.project.bookshop.model.OrderItem;
import test.project.bookshop.model.User;
import test.project.bookshop.repository.OrderItemRepository;
import test.project.bookshop.repository.OrderRepository;
import test.project.bookshop.service.OrderService;
import test.project.bookshop.service.ShoppingCartService;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService shoppingCartService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto placeOrder(User currentUser) {

        Order order = new Order();
        order.setUser(currentUser);
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(currentUser.getShippingAddress());

        Set<CartItem> cartContent = shoppingCartService.getCartContentByUserId(currentUser.getId());
        Set<OrderItem> orderContent = orderItemMapper.toEntity(cartContent);

        orderContent.forEach(item -> item.setOrder(order));

        order.setOrderItems(orderContent);
        order.setTotal(calculateTotal(orderContent));

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderResponseDto> getOrderHistory(Long userId, Pageable pageable) {
        return orderRepository
                .findByUserId(userId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public Page<OrderItemResponseDto> getOrderItems(Long orderId, Pageable pageable) {
        return orderItemRepository
                .findByOrderId(orderId, pageable)
                .map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long orderId, Long itemId) {
        return orderItemMapper.toDto(orderItemRepository.findByOrderIdAndId(orderId, itemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "OrderItem not found for order ID: " + orderId + " item ID: " + itemId)));
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long id, UpdateOrderStatusRequest updateRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found for ID: " + id));
        orderMapper.update(order, updateRequest);
        return orderMapper.toDto(orderRepository.save(order));
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderContent) {
        return orderContent.stream()
                .map(OrderItem::getOrderItemPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
