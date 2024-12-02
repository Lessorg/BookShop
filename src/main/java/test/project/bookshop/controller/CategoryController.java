package test.project.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import test.project.bookshop.dto.book.BookDto;
import test.project.bookshop.dto.category.CategoryDto;
import test.project.bookshop.dto.category.CategoryRequestDto;
import test.project.bookshop.service.BookService;
import test.project.bookshop.service.CategoryService;

@Tag(name = "Categories", description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(
            summary = "Get all categories",
            description = "Retrieve a list of all categories with pagination")
    public Page<CategoryDto> findAllCategories(
            @ParameterObject @PageableDefault Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Get books by category ID",
            description = "Retrieve a list of books for a specific category ID with pagination")
    @GetMapping("/{id}/books")
    public Page<BookDto> getBooksByCategoryId(
            @PathVariable Long id,
            @ParameterObject @PageableDefault Pageable pageable) {
        return bookService.findBooksByCategotyId(id, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Get category by ID",
            description = "Retrieve a category by its unique identifier")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Create a new category",
            description = "Create a new category")
    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid CategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a category",
            description = "Delete a category by its unique identifier")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(
            summary = "Update a category",
            description = "Update the details of a category by its unique identifier")
    public CategoryDto updateCategory(@PathVariable Long id,
                              @RequestBody @Valid CategoryRequestDto requestDto) {
        return categoryService.update(id, requestDto);
    }
}
