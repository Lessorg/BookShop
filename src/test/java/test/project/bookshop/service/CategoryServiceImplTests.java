package test.project.bookshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import test.project.bookshop.dto.category.CategoryDto;
import test.project.bookshop.dto.category.CategoryRequestDto;
import test.project.bookshop.exception.EntityNotFoundException;
import test.project.bookshop.mapper.CategoryMapper;
import test.project.bookshop.model.Category;
import test.project.bookshop.repository.CategoryRepository;
import test.project.bookshop.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTests {
    private static final Long EXISTING_CATEGORY_ID = 1L;
    private static final Long NON_EXISTENT_CATEGORY_ID = 999L;
    private static final PageRequest PAGEABLE = PageRequest.of(0, 10);

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Find all categories with pagination")
    void findAll_ReturnsPagedCategories() {
        Category category = getCategory(EXISTING_CATEGORY_ID);
        CategoryDto categoryDto = getCategoryDto(category);
        Page<Category> pagedCategories = new PageImpl<>(List.of(category));
        Page<CategoryDto> pagedCategoryDtos = new PageImpl<>(List.of(categoryDto));

        when(categoryRepository.findAll(PAGEABLE)).thenReturn(pagedCategories);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        Page<CategoryDto> actual = categoryService.findAll(PAGEABLE);

        assertEquals(pagedCategoryDtos, actual);
        verify(categoryRepository).findAll(PAGEABLE);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Get category by ID when category exists")
    void getById_CategoryExists_ReturnsCategoryDto() {
        Category category = getCategory(EXISTING_CATEGORY_ID);
        CategoryDto categoryDto = getCategoryDto(category);

        when(categoryRepository.findById(EXISTING_CATEGORY_ID)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        CategoryDto actual = categoryService.getById(EXISTING_CATEGORY_ID);

        assertEquals(categoryDto, actual);
        verify(categoryRepository).findById(EXISTING_CATEGORY_ID);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Get category by ID when category does not exist")
    void getById_CategoryNotExists_ThrowsException() {
        when(categoryRepository.findById(NON_EXISTENT_CATEGORY_ID)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(NON_EXISTENT_CATEGORY_ID));

        assertEquals("Can't find category by id: "
                + NON_EXISTENT_CATEGORY_ID, exception.getMessage());
        verify(categoryRepository).findById(NON_EXISTENT_CATEGORY_ID);
    }

    @Test
    @DisplayName("Save a new category")
    void save_NewCategory_ReturnsSavedCategoryDto() {
        CategoryRequestDto categoryRequestDto = getCategoryRequestDto();
        Category category = getCategory(null);
        Category savedCategory = getCategory(EXISTING_CATEGORY_ID);
        CategoryDto savedCategoryDto = getCategoryDto(savedCategory);

        when(categoryMapper.toCategory(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(savedCategoryDto);
        CategoryDto actual = categoryService.save(categoryRequestDto);

        assertEquals(savedCategoryDto, actual);
        verify(categoryMapper).toCategory(categoryRequestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(savedCategory);
    }

    @Test
    @DisplayName("Update an existing category")
    void update_CategoryExists_ReturnsUpdatedCategoryDto() {
        CategoryRequestDto categoryRequestDto = getCategoryRequestDto();
        Category existingCategory = getCategory(EXISTING_CATEGORY_ID);
        Category updatedCategory = getCategory(EXISTING_CATEGORY_ID);
        CategoryDto updatedCategoryDto = getCategoryDto(updatedCategory);

        when(categoryRepository.findById(EXISTING_CATEGORY_ID))
                .thenReturn(Optional.of(existingCategory));
        doNothing().when(categoryMapper).updateCategoryFromDto(categoryRequestDto,
                existingCategory);
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(updatedCategoryDto);
        CategoryDto actual = categoryService.update(EXISTING_CATEGORY_ID, categoryRequestDto);

        assertEquals(updatedCategoryDto, actual);
        verify(categoryRepository).findById(EXISTING_CATEGORY_ID);
        verify(categoryMapper).updateCategoryFromDto(categoryRequestDto, existingCategory);
        verify(categoryRepository).save(existingCategory);
        verify(categoryMapper).toDto(updatedCategory);
    }

    @Test
    @DisplayName("Update a non-existent category")
    void update_CategoryNotExists_ThrowsException() {
        CategoryRequestDto categoryRequestDto = getCategoryRequestDto();

        when(categoryRepository.findById(NON_EXISTENT_CATEGORY_ID)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(NON_EXISTENT_CATEGORY_ID, categoryRequestDto));

        assertEquals("Can't find category by id: " + NON_EXISTENT_CATEGORY_ID,
                exception.getMessage());
        verify(categoryRepository).findById(NON_EXISTENT_CATEGORY_ID);
    }

    @Test
    @DisplayName("Delete category by ID when category exists")
    void deleteById_CategoryExists_DeletesSuccessfully() {
        when(categoryRepository.existsById(EXISTING_CATEGORY_ID)).thenReturn(Boolean.TRUE);
        categoryService.deleteById(EXISTING_CATEGORY_ID);

        verify(categoryRepository).existsById(EXISTING_CATEGORY_ID);
        verify(categoryRepository).deleteById(EXISTING_CATEGORY_ID);
    }

    @Test
    @DisplayName("Delete category by ID when category does not exist")
    void deleteById_CategoryNotExists_ThrowsException() {
        when(categoryRepository.existsById(NON_EXISTENT_CATEGORY_ID)).thenReturn(
                Boolean.FALSE);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.deleteById(NON_EXISTENT_CATEGORY_ID));

        assertEquals("Category with id " + NON_EXISTENT_CATEGORY_ID
                + " does not exist.", exception.getMessage());
        verify(categoryRepository).existsById(NON_EXISTENT_CATEGORY_ID);
        verify(categoryRepository, never()).deleteById(NON_EXISTENT_CATEGORY_ID);
    }

    @Test
    @DisplayName("Find categories by IDs")
    void findCategoriesByIds_ValidIds_ReturnsCategories() {
        Category category1 = getCategory(1L);
        Category category2 = getCategory(2L);
        List<Long> categoryIds = List.of(1L, 2L);
        Set<Category> expectedCategories = Set.of(category1, category2);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category2));
        Set<Category> actual = categoryService.findCategoriesByIds(categoryIds);

        assertEquals(expectedCategories, actual);
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).findById(2L);
    }

    private Category getCategory(Long id) {
        Category category = new Category();
        category.setId(id);
        category.setName("Sample Category");
        return category;
    }

    private CategoryDto getCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription());
    }

    private CategoryRequestDto getCategoryRequestDto() {
        return new CategoryRequestDto(
                "Sample Category",
                "Sample description"
        );
    }
}
