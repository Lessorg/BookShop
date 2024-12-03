package test.project.bookshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.project.bookshop.config.MapperConfig;
import test.project.bookshop.dto.shopping.cart.ResponseCartDto;
import test.project.bookshop.model.ShoppingCart;

@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class})
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "shoppingCart.user.id")
    @Mapping(target = "cartItems", source = "cartItems")
    ResponseCartDto toDto(ShoppingCart shoppingCart);
}
