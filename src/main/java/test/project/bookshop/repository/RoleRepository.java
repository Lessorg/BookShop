package test.project.bookshop.repository;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import test.project.bookshop.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findByName(Role.RoleName name);
}
