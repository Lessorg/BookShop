package test.project.bookshop.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequestDto {
    private String title;
    private String author;
    private String isbn;
    private String description;
    private String coverImage;
    private BigDecimal price;
}
