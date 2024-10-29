package test.project.bookshop.repository;

import java.util.List;
import java.util.Optional;
import test.project.bookshop.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findByid(Long id);
}
