package test.project.bookshop.repository.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import test.project.bookshop.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book> {

}