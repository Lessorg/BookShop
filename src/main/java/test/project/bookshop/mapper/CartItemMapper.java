package test.project.bookshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import test.project.bookshop.config.MapperConfig;
import test.project.bookshop.dto.shopping.cart.AddBookToCartDto;
import test.project.bookshop.dto.shopping.cart.CartItemDto;
import test.project.bookshop.dto.shopping.cart.UpdateCartItemRequest;
import test.project.bookshop.model.CartItem;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "id", ignore = true)
    CartItem toEntity(AddBookToCartDto bookToCartDto);

    @Mapping(target = "book", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateCartItemFromDto(UpdateCartItemRequest updateCartItemRequest,
                               @MappingTarget CartItem cartItem);
}
