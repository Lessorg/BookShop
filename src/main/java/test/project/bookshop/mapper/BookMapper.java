package test.project.bookshop.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import test.project.bookshop.config.MapperConfig;
import test.project.bookshop.context.provider.ContextProvider;
import test.project.bookshop.dto.book.BookDto;
import test.project.bookshop.dto.book.BookRequestDto;
import test.project.bookshop.dto.book.BookWithoutCategoryIdsDto;
import test.project.bookshop.exception.EntityNotFoundException;
import test.project.bookshop.model.Book;
import test.project.bookshop.model.Category;
import test.project.bookshop.repository.book.BookRepository;
import test.project.bookshop.repository.category.CategoryRepository;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", source = "categories")
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Book toBook(BookRequestDto bookRequestDto);

    @AfterMapping
    default void mapCategoryIdsToCategories(
            @MappingTarget Book book,
            BookRequestDto bookRequestDto) {
        if (bookRequestDto.getCategoryIds() != null) {
            CategoryRepository categoryRepository =
                    ContextProvider.getBean(CategoryRepository.class);

            Set<Category> categories = bookRequestDto.getCategoryIds().stream()
                    .map(categoryId -> categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Can't find category with ID: "
                            + categoryId)))
                    .collect(Collectors.toSet());
            book.setCategories(categories);
        }
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateBookFromDto(BookRequestDto bookDto, @MappingTarget Book book);

    default Set<Long> mapCategoriesInCategoryIds(Set<Category> categories) {
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }

    @Named("bookFromId")
    default Book bookFromId(Long bookId) {
        if (bookId == null) {
            return null;
        }
        BookRepository bookRepository = ContextProvider.getBean(BookRepository.class);
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found for ID: " + bookId));
    }

    BookWithoutCategoryIdsDto toDtoWithoutCategories(Book book);
}
