package test.project.bookshop.service;

import java.util.List;
import test.project.bookshop.dto.BookDto;
import test.project.bookshop.dto.BookRequestDto;
import test.project.bookshop.dto.BookSearchParametersDto;

public interface BookService {
    BookDto save(BookRequestDto bookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void delete(Long id);

    BookDto update(Long id, BookRequestDto requestDto);

    List<BookDto> search(BookSearchParametersDto searchParameters);
}
