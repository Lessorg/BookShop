package test.project.bookshop.service;

import java.util.List;
<<<<<<< HEAD
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
=======
import test.project.bookshop.model.Book;

public interface BookService {
    Book save(Book book);

    List findAll();
>>>>>>> master
}
