package test.project.bookshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.project.bookshop.config.MapperConfig;
import test.project.bookshop.dto.shopping.cart.CartResponseDto;
import test.project.bookshop.model.ShoppingCart;

@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class})
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    CartResponseDto toDto(ShoppingCart shoppingCart);
}
