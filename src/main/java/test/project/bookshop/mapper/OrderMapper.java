package test.project.bookshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import test.project.bookshop.config.MapperConfig;
import test.project.bookshop.dto.order.OrderResponseDto;
import test.project.bookshop.dto.order.UpdateOrderStatusRequest;
import test.project.bookshop.model.Order;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderResponseDto toDto(Order order);

    void update(@MappingTarget Order order, UpdateOrderStatusRequest updateRequest);
}
