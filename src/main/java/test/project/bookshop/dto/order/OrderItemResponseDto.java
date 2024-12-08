package test.project.bookshop.dto.order;

public record OrderItemResponseDto(
        Long id,
        Long bookId,
        int quantity
) {
}
