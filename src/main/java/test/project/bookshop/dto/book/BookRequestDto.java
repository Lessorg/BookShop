package test.project.bookshop.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String isbn;
    @NotBlank
    private String description;
    private String coverImage;
    @Positive
    private BigDecimal price;
    @NotEmpty
    private List<Long> categoryIds;
}
