package test.project.bookshop.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import test.project.bookshop.dto.category.CategoryDto;
import test.project.bookshop.dto.category.CategoryRequestDto;
import test.project.bookshop.exception.EntityNotFoundException;
import test.project.bookshop.mapper.CategoryMapper;
import test.project.bookshop.model.Category;
import test.project.bookshop.repository.CategoryRepository;
import test.project.bookshop.service.CategoryService;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(findCategoryById(id));
    }

    @Override
    public CategoryDto save(CategoryRequestDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CategoryRequestDto categoryDto) {
        Category category = findCategoryById(id);

        categoryMapper.updateCategoryFromDto(categoryDto, category);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category with id " + id + " does not exist.");
        }

        categoryRepository.deleteById(id);
    }

    @Override
    public Set<Category> findCategoriesByIds(List<Long> categoryIds) {
        return categoryIds.stream()
                .map(this::findCategoryById)
                .collect(Collectors.toSet());
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id: " + id));
    }
}
