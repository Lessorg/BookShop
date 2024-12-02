package test.project.bookshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import test.project.bookshop.dto.book.BookDto;
import test.project.bookshop.dto.book.BookRequestDto;
import test.project.bookshop.dto.book.BookSearchParametersDto;

public interface BookService {
    BookDto save(BookRequestDto bookRequestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void delete(Long id);

    BookDto update(Long id, BookRequestDto requestDto);

    Page<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable);

    Page<BookDto> findBooksByCategotyId(Long id, Pageable pageable);
}
