package test.project.bookshop.repository.user;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import test.project.bookshop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(@NotBlank @Email String email);
}
