package test.project.bookshop.repository.book;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import test.project.bookshop.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book> {
    Page<Book> findAllByCategoriesId(Long categoryId, Pageable pageable);

    @EntityGraph(attributePaths = {"categories"})
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"categories"})
    Optional<Book> findById(Long id);
}
