package test.project.bookshop.mapper;

import org.mapstruct.Mapper;
import test.project.bookshop.config.MapperConfig;
import test.project.bookshop.dto.BookDto;
import test.project.bookshop.dto.BookRequestDto;
import test.project.bookshop.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toBook(BookRequestDto bookRequestDto);
}
