package test.project.bookshop.service;

import java.util.List;
import test.project.bookshop.model.Book;

public interface BookService {
    Book save(Book book);

    List findAll();
}
