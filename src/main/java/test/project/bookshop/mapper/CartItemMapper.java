package test.project.bookshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import test.project.bookshop.config.MapperConfig;
import test.project.bookshop.dto.shopping.cart.AddBookToCartDto;
import test.project.bookshop.dto.shopping.cart.CartItemDto;
import test.project.bookshop.dto.shopping.cart.UpdateCartItemRequest;
import test.project.bookshop.model.CartItem;
import test.project.bookshop.model.ShoppingCart;

@Mapper(config = MapperConfig.class, uses = {BookMapper.class})
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookToCartDto.bookId", qualifiedByName = "bookFromId")
    @Mapping(target = "shoppingCart", source = "shoppingCart")
    @Mapping(target = "id", ignore = true)
    CartItem toEntity(AddBookToCartDto bookToCartDto, ShoppingCart shoppingCart);

    void updateCartItemFromDto(UpdateCartItemRequest updateCartItemRequest,
                               @MappingTarget CartItem cartItem);
}
