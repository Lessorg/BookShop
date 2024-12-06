package test.project.bookshop.repository.cart.item;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import test.project.bookshop.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findCartItemById(Long id);
}
