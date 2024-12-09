package test.project.bookshop.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import test.project.bookshop.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> findByOrderId(Long orderId, Pageable pageable);

    Optional<OrderItem> findByOrderIdAndId(Long orderId, Long id);
}

