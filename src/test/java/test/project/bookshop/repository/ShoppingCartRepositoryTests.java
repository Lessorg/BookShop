package test.project.bookshop.repository;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import test.project.bookshop.model.ShoppingCart;

@DataJpaTest
@Sql(scripts = "/db/add-test-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/add-shopping-cart.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/clean-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTests {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    void findByUserId_Success() {
        Long userId = 1L;

        Optional<ShoppingCart> result = shoppingCartRepository.findByUserId(userId);

        assertTrue(result.isPresent());
        ShoppingCart cart = result.get();
        assertEquals(userId, cart.getId());
        assertNotNull(cart.getCartItems());
        assertFalse(cart.getCartItems().isEmpty());
        assertNotNull(cart.getCartItems().toArray()[0]);
    }

    @Test
    void findByUserId_NoResult() {
        Long nonExistentUserId = 999L;

        Optional<ShoppingCart> result = shoppingCartRepository.findByUserId(nonExistentUserId);

        assertTrue(result.isEmpty());
    }
}
