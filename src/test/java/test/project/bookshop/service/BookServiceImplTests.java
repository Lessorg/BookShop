package test.project.bookshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashSet;
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
import org.springframework.data.jpa.domain.Specification;
import test.project.bookshop.dto.book.BookDto;
import test.project.bookshop.dto.book.BookRequestDto;
import test.project.bookshop.dto.book.BookSearchParametersDto;
import test.project.bookshop.dto.book.BookWithoutCategoryIdsDto;
import test.project.bookshop.exception.EntityNotFoundException;
import test.project.bookshop.mapper.BookMapper;
import test.project.bookshop.model.Book;
import test.project.bookshop.model.Category;
import test.project.bookshop.repository.book.BookRepository;
import test.project.bookshop.repository.book.BookSpecificationBuilder;
import test.project.bookshop.service.impl.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTests {
    private static final Long FIRST_BOOK_ID = 1L;
    private static final Long SECOND_BOOK_ID = 2L;
    private static final Long NON_EXISTENT_BOOK_ID = 999L;
    private static final PageRequest PAGEABLE = PageRequest.of(0, 10);

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save a new book successfully")
    void save_NewBook_ReturnsSavedBookDto() {
        BookRequestDto bookRequestDto = getBookRequestDto();
        Book firstBook = getBook(FIRST_BOOK_ID);
        Set<Category> categories = firstBook.getCategories();
        BookDto firstBookDto = getBookDto(firstBook);

        when(categoryService.findCategoriesByIds(
                bookRequestDto.getCategoryIds())).thenReturn(categories);
        when(bookMapper.toBook(bookRequestDto)).thenReturn(firstBook);
        when(bookRepository.save(firstBook)).thenReturn(firstBook);
        when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
        BookDto actual = bookService.save(bookRequestDto);

        assertEquals(firstBookDto, actual);
        verify(categoryService).findCategoriesByIds(bookRequestDto.getCategoryIds());
        verify(bookRepository).save(firstBook);
    }

    @Test
    @DisplayName("Find all books with pagination - Multiple books")
    void findAll_MultipleBooks_ReturnsPagedBooks() {
        Book firstBook = getBook(FIRST_BOOK_ID);
        Book secondBook = getBook(SECOND_BOOK_ID);
        BookDto firstBookDto = getBookDto(firstBook);
        BookDto secondBookDto = getBookDto(secondBook);
        Page<Book> pagedBooks = new PageImpl<>(List.of(firstBook, secondBook));

        when(bookRepository.findAll(PAGEABLE)).thenReturn(pagedBooks);
        when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
        when(bookMapper.toDto(secondBook)).thenReturn(secondBookDto);

        Page<BookDto> actual = bookService.findAll(PAGEABLE);
        Page<BookDto> pagedBookDtos = new PageImpl<>(List.of(firstBookDto, secondBookDto));

        assertEquals(pagedBookDtos, actual);
        verify(bookRepository).findAll(PAGEABLE);
        verify(bookMapper).toDto(firstBook);
        verify(bookMapper).toDto(secondBook);
    }

    @Test
    @DisplayName("Find book by ID when book exists")
    void findById_BookExists_ReturnsBookWithoutCategories() {
        Book firstBook = getBook(FIRST_BOOK_ID);
        BookWithoutCategoryIdsDto bookWithoutCategoryIdsDto = new BookWithoutCategoryIdsDto();

        when(bookRepository.findById(FIRST_BOOK_ID)).thenReturn(Optional.of(firstBook));
        when(bookMapper.toDtoWithoutCategories(firstBook))
                .thenReturn(bookWithoutCategoryIdsDto);
        BookWithoutCategoryIdsDto actual = bookService.findById(FIRST_BOOK_ID);

        assertEquals(bookWithoutCategoryIdsDto, actual);
        verify(bookRepository).findById(FIRST_BOOK_ID);
    }

    @Test
    @DisplayName("Find book by ID when book does not exist")
    void findById_BookNotExists_ThrowsException() {
        when(bookRepository.findById(NON_EXISTENT_BOOK_ID)).thenReturn(Optional.empty());
        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> bookService.findById(NON_EXISTENT_BOOK_ID));

        assertEquals("Can't find book by id: " + NON_EXISTENT_BOOK_ID, exception.getMessage());
    }

    @Test
    @DisplayName("Delete book by ID")
    void delete_BookExists_DeletesSuccessfully() {
        when(bookRepository.existsById(FIRST_BOOK_ID)).thenReturn(Boolean.TRUE);
        bookService.delete(FIRST_BOOK_ID);

        verify(bookRepository).existsById(FIRST_BOOK_ID);
        verify(bookRepository).deleteById(FIRST_BOOK_ID);
    }

    @Test
    @DisplayName("Delete book by ID - Book does not exist")
    void delete_BookNotExists_ThrowsEntityNotFoundException() {
        when(bookRepository.existsById(NON_EXISTENT_BOOK_ID)).thenReturn(Boolean.FALSE);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.delete(NON_EXISTENT_BOOK_ID));

        assertEquals("Book with id " + NON_EXISTENT_BOOK_ID
                + " does not exist.", exception.getMessage());
        verify(bookRepository).existsById(NON_EXISTENT_BOOK_ID);
        verify(bookRepository, never()).deleteById(NON_EXISTENT_BOOK_ID);
    }

    @Test
    @DisplayName("Update a book successfully")
    void update_BookExists_ReturnsUpdatedBookDto() {
        BookRequestDto bookRequestDto = getBookRequestDto();
        Set<Category> categories = Set.of(new Category(), new Category());
        Book firstBook = getBook(FIRST_BOOK_ID);
        BookDto firstBookDto = getBookDto(firstBook);

        when(bookRepository.findById(FIRST_BOOK_ID)).thenReturn(Optional.of(firstBook));
        when(categoryService.findCategoriesByIds(bookRequestDto.getCategoryIds()))
                .thenReturn(categories);
        when(bookRepository.save(firstBook)).thenReturn(firstBook);
        when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
        BookDto actual = bookService.update(FIRST_BOOK_ID, bookRequestDto);

        assertEquals(firstBookDto, actual);
        verify(categoryService).findCategoriesByIds(bookRequestDto.getCategoryIds());
        verify(bookRepository).save(firstBook);
    }

    @Test
    @DisplayName("Find books by category ID - Books found")
    void findBooksByCategoryId_BooksFound_ReturnsPagedBooks() {
        Book firstBook = getBook(FIRST_BOOK_ID);
        BookDto firstBookDto = getBookDto(firstBook);
        Book secondBook = getBook(SECOND_BOOK_ID);
        BookDto secondBookDto = getBookDto(secondBook);
        Page<Book> pagedBooks = new PageImpl<>(List.of(firstBook, secondBook));
        Long categoryId = 1L;

        when(bookRepository.findAllByCategoriesId(categoryId, PAGEABLE)).thenReturn(pagedBooks);
        when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
        when(bookMapper.toDto(secondBook)).thenReturn(secondBookDto);

        Page<BookDto> pagedBookDtos = new PageImpl<>(List.of(firstBookDto, secondBookDto));
        Page<BookDto> actual = bookService.findBooksByCategoryId(categoryId, PAGEABLE);

        assertEquals(pagedBookDtos, actual);
        verify(bookRepository).findAllByCategoriesId(categoryId, PAGEABLE);
        verify(bookMapper).toDto(firstBook);
        verify(bookMapper).toDto(secondBook);
    }

    @Test
    @DisplayName("Search books with filters and pagination")
    void search_BooksExist_ReturnsFilteredBooks() {
        BookSearchParametersDto searchParameters = new BookSearchParametersDto(
                new String[]{""},
                new String[]{""},
                new String[]{""}
        );
        Specification<Book> bookSpecification = mock(Specification.class);
        Book firstBook = getBook(FIRST_BOOK_ID);
        BookDto firstBookDto = getBookDto(firstBook);
        List<Book> bookList = List.of(firstBook);
        List<BookDto> bookDtoList = List.of(firstBookDto);
        Page<Book> pagedBooks = new PageImpl<>(bookList, PAGEABLE, bookList.size());

        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification, PAGEABLE)).thenReturn(pagedBooks);
        when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);

        Page<BookDto> actual = bookService.search(searchParameters, PAGEABLE);
        Page<BookDto> pagedBookDtos = new PageImpl<>(bookDtoList, PAGEABLE, bookDtoList.size());
        assertEquals(pagedBookDtos.getContent(), actual.getContent());
        assertEquals(pagedBookDtos.getTotalElements(), actual.getTotalElements());
        verify(bookSpecificationBuilder).build(searchParameters);
        verify(bookRepository).findAll(bookSpecification, PAGEABLE);
    }

    private BookRequestDto getBookRequestDto() {
        BookRequestDto bookRequestDto = new BookRequestDto();
        bookRequestDto.setCategoryIds(List.of(1L, 2L));
        return bookRequestDto;
    }

    private BookDto getBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategoryIds(new HashSet<>(List.of(1L, 2L)));
        return bookDto;
    }

    private Book getBook(Long id) {
        Set<Category> categories = new HashSet<>();
        categories.add(new Category());
        categories.add(new Category());

        Book book = new Book();
        book.setId(id);
        book.setTitle("Sample Book");
        book.setAuthor("Author Name");
        book.setIsbn("1234567890");
        book.setPrice(new BigDecimal("19.99"));
        book.setDescription("A sample book for testing purposes.");
        book.setCoverImage("cover_image.jpg");
        book.setCategories(categories);
        return book;
    }
}
