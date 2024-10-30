package test.project.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.project.bookshop.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {


}
