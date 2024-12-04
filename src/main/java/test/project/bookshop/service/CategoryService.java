package test.project.bookshop.service;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import test.project.bookshop.dto.category.CategoryDto;
import test.project.bookshop.dto.category.CategoryRequestDto;
import test.project.bookshop.model.Category;

public interface CategoryService {
    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryRequestDto categoryDto);

    CategoryDto update(Long id, CategoryRequestDto categoryDto);

    void deleteById(Long id);

    Set<Category> findCategoriesByIds(@NotEmpty List<Long> categoryIds);
}
