package test.project.bookshop.repository.shopping.cart;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import test.project.bookshop.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = {"cartItems", "cartItems.book"})
    Optional<ShoppingCart> findShoppingCartByUserId(Long id);

    @EntityGraph(attributePaths = {"cartItems", "cartItems.book"})
    ShoppingCart save(ShoppingCart shoppingCart);
}
