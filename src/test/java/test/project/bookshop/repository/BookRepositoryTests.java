package test.project.bookshop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import test.project.bookshop.model.Book;
import test.project.bookshop.repository.book.BookRepository;

@DataJpaTest
@Sql(scripts = "/db/add-test-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTests {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by category ID")
    void findAllByCategoriesId_CategoryExists_ReturnsBooks() {
        Long categoryId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);

        Page<Book> actual = bookRepository.findAllByCategoriesId(categoryId, pageable);

        assertEquals(3, actual.getContent().size());
        assertEquals("Pride and Prejudice", actual.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("Find all books with EntityGraph when pageable request is provided")
    void findAllWithEntityGraph_PageableRequest_ReturnsBooksWithCategories() {
        PageRequest pageable = PageRequest.of(0, 10);

        Page<Book> actual = bookRepository.findAll(pageable);

        assertEquals(5, actual.getContent().size());
        assertEquals("Pride and Prejudice", actual.getContent().get(0).getTitle());
        assertFalse(actual.getContent().get(0).getCategories().isEmpty());
    }

    @Test
    @DisplayName("Find book by ID with EntityGraph when book exists")
    void findByIdWithEntityGraph_BookExists_ReturnsBookWithCategories() {
        Long bookId = 1L;

        Optional<Book> actual = bookRepository.findById(bookId);

        assertTrue(actual.isPresent());
        assertEquals("Pride and Prejudice", actual.get().getTitle());
        assertEquals(1, actual.get().getCategories().size());
    }

    @Test
    @DisplayName("Find all books by category ID when category does not exist")
    void findAllByCategoriesId_CategoryNotExists_ReturnsEmpty() {
        Long categoryId = 999L;
        PageRequest pageable = PageRequest.of(0, 10);

        Page<Book> actual = bookRepository.findAllByCategoriesId(categoryId, pageable);

        assertEquals(0, actual.getContent().size());
    }

    @Test
    @DisplayName("Find book by ID when book does not exist")
    void findById_BookNotExists_ReturnsEmpty() {
        Long bookId = 999L;

        Optional<Book> actual = bookRepository.findById(bookId);

        assertTrue(actual.isEmpty());
    }
}
