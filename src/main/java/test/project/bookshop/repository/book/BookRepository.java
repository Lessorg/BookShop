package test.project.bookshop.repository.book;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import test.project.bookshop.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book> {
    Page<Book> findAllByCategoriesId(Long categoryId, Pageable pageable);

    @EntityGraph(attributePaths = {"categories"})
    @Query("SELECT b FROM Book b")
    Page<Book> findAllFetchCategories(Pageable pageable);

    @EntityGraph(attributePaths = {"categories"})
    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Optional<Book> findByIdFetchCategories(Long id);
}
