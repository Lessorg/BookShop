package test.project.bookshop.mapper;

import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.project.bookshop.config.MapperConfig;
import test.project.bookshop.dto.order.OrderItemResponseDto;
import test.project.bookshop.model.CartItem;
import test.project.bookshop.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toDto(OrderItem orderItem);

    Set<OrderItem> toEntity(Set<CartItem> cartItems);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "price", source = "book.price")
    OrderItem toEntity(CartItem cartItem);
}
