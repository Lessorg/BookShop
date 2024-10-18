package test.project.bookshop.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.project.bookshop.model.Book;
import test.project.bookshop.repository.BookRepository;
import test.project.bookshop.service.BookService;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List findAll() {
        return bookRepository.findAll();
    }
}
