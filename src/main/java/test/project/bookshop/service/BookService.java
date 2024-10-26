package test.project.bookshop.service;

import java.util.List;
import test.project.bookshop.dto.BookDto;
import test.project.bookshop.dto.BookRequestDto;

public interface BookService {
    BookDto save(BookRequestDto bookRequestDto);

    List<BookDto> findAll();

    BookDto findByid(Long id);
}
