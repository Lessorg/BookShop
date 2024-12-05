package test.project.bookshop.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import test.project.bookshop.config.MapperConfig;
import test.project.bookshop.dto.book.BookDto;
import test.project.bookshop.dto.book.BookRequestDto;
import test.project.bookshop.dto.book.BookWithoutCategoryIdsDto;
import test.project.bookshop.model.Book;
import test.project.bookshop.model.Category;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", source = "categories")
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Book toBook(BookRequestDto bookRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateBookFromDto(BookRequestDto bookDto, @MappingTarget Book book);

    BookWithoutCategoryIdsDto toDtoWithoutCategories(Book book);

    default Set<Long> mapCategoriesInCategoryIds(@NotNull Set<Category> categories) {
        return categories.stream().map(Category::getId).collect(Collectors.toSet());
    }
}
