package test.project.bookshop.repository.shopping.cart;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import test.project.bookshop.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = {"cartItems", "cartItems.book"})
    @Query("SELECT sc FROM ShoppingCart sc WHERE sc.user.id = :userId")
    Optional<ShoppingCart> findByUserIdFetchCartItemsAndBooks(Long userId);
}
