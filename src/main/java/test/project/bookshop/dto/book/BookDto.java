package test.project.bookshop.dto.book;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<Long> categoryIds;
}
