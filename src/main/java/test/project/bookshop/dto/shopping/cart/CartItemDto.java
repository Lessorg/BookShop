package test.project.bookshop.dto.shopping.cart;

public record CartItemDto(
        Long id,
        Long bookId,
        String bookTitle,
        int quantity
) {
}
