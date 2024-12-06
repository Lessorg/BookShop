package test.project.bookshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import test.project.bookshop.dto.book.BookDto;
import test.project.bookshop.dto.book.BookRequestDto;
import test.project.bookshop.dto.book.BookSearchParametersDto;
import test.project.bookshop.dto.book.BookWithoutCategoryIdsDto;
import test.project.bookshop.model.Book;

public interface BookService {
    BookDto save(BookRequestDto bookRequestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookWithoutCategoryIdsDto findById(Long id);

    void delete(Long id);

    BookDto update(Long id, BookRequestDto requestDto);

    Page<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable);

    Page<BookDto> findBooksByCategoryId(Long id, Pageable pageable);

    Book findBookById(Long id);
}
