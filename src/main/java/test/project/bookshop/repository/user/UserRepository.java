package test.project.bookshop.repository.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import test.project.bookshop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
