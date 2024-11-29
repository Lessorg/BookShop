package test.project.bookshop.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import test.project.bookshop.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
