package test.project.bookshop.repository;

import java.util.List;
import test.project.bookshop.model.Book;

public interface BookRepository {
    Book save(Book book);

    List findAll();
}
