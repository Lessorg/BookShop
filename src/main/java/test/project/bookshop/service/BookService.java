package test.project.bookshop.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import test.project.bookshop.dto.BookDto;
import test.project.bookshop.dto.BookRequestDto;
import test.project.bookshop.dto.BookSearchParametersDto;

public interface BookService {
    BookDto save(BookRequestDto bookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void delete(Long id);

    BookDto update(Long id, BookRequestDto requestDto);

    List<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable);
}
