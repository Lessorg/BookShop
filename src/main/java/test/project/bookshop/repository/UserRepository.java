package test.project.bookshop.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import test.project.bookshop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    @EntityGraph(value = "User.roles", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByEmail(String email);
}
