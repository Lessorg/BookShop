package test.project.bookshop.repository.book;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import test.project.bookshop.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book> {
    Page<Book> findAllByCategoriesId(Long categoryId, Pageable pageable);

    @EntityGraph(attributePaths = {"categories"})
    @NonNull
    Page<Book> findAll(@NonNull Pageable pageable);

    @EntityGraph(attributePaths = {"categories"})
    @NonNull
    Optional<Book> findById(@NonNull Long id);
}
