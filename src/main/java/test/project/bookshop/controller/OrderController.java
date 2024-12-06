package test.project.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.project.bookshop.dto.order.OrderItemResponseDto;
import test.project.bookshop.dto.order.OrderResponseDto;
import test.project.bookshop.dto.order.UpdateOrderStatusRequest;
import test.project.bookshop.model.User;
import test.project.bookshop.service.OrderService;

@Tag(name = "Orders", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Place an order",
            description = "Place an order for the items in the user's shopping cart")
    public OrderResponseDto placeOrder(Authentication authentication) {
        return orderService.placeOrder((User) authentication.getPrincipal());
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "View order history",
            description = "Retrieve the user's past orders")
    public Page<OrderResponseDto> getOrderHistory(
            Authentication authentication,
            @ParameterObject @PageableDefault Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderHistory(user.getId(), pageable);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "View order items",
            description = "Retrieve all items for a specific order")
    public Page<OrderItemResponseDto> getOrderItems(
            @PathVariable Long orderId,
            @ParameterObject @PageableDefault Pageable pageable) {
        return orderService.getOrderItems(orderId, pageable);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "View specific order item",
            description = "Retrieve details of a specific item in an order")
    public OrderItemResponseDto getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getOrderItem(orderId, itemId);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update order status",
            description = "Update the status of an order")
    public OrderResponseDto updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusRequest updateRequest) {
        return orderService.updateOrderStatus(id, updateRequest);
    }
}
