package test.project.bookshop.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import test.project.bookshop.dto.book.BookDto;
import test.project.bookshop.dto.book.BookRequestDto;
import test.project.bookshop.dto.book.BookSearchParametersDto;
import test.project.bookshop.dto.book.BookWithoutCategoryIdsDto;
import test.project.bookshop.exception.DuplicateIsbnException;
import test.project.bookshop.exception.EntityNotFoundException;
import test.project.bookshop.mapper.BookMapper;
import test.project.bookshop.model.Book;
import test.project.bookshop.model.Category;
import test.project.bookshop.repository.book.BookRepository;
import test.project.bookshop.repository.book.BookSpecificationBuilder;
import test.project.bookshop.service.BookService;
import test.project.bookshop.service.CategoryService;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryService categoryService;

    @Override
    public BookDto save(BookRequestDto bookRequestDto) {
        if (bookRepository.existsByIsbn(bookRequestDto.getIsbn())) {
            throw new DuplicateIsbnException("Isbn: + "
                    + bookRequestDto.getIsbn() + " already exists");
        }
        Set<Category> categories =
                categoryService.findCategoriesByIds(bookRequestDto.getCategoryIds());
        Book book = bookMapper.toBook(bookRequestDto);
        book.getCategories().addAll(categories);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::toDto);
    }

    @Override
    public BookWithoutCategoryIdsDto findById(Long id) {
        return bookMapper.toDtoWithoutCategories(findBookById(id));
    }

    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id " + id + " does not exist.");
        }
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto update(Long id, BookRequestDto bookRequestDto) {
        Book book = findBookById(id);
        Set<Category> categories =
                categoryService.findCategoriesByIds(bookRequestDto.getCategoryIds());
        bookMapper.updateBookFromDto(bookRequestDto, book);
        book.setCategories(categories);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findBooksByCategoryId(Long id, Pageable pageable) {
        Page<Book> books = bookRepository.findAllByCategoriesId(id, pageable);
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Can't find books by category id: " + id);
        }
        return books.map(bookMapper::toDto);
    }

    @Override
    public Book findBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id));
    }

    @Override
    public Page<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        Page<Book> books = bookRepository.findAll(bookSpecification, pageable);
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Can't find book with parameters: "
                    + searchParameters);
        }
        return books.map(bookMapper::toDto);
    }
}
