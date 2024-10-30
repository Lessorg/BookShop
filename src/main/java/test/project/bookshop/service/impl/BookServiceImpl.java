package test.project.bookshop.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.project.bookshop.dto.BookDto;
import test.project.bookshop.dto.BookRequestDto;
import test.project.bookshop.exception.EntityNotFoundException;
import test.project.bookshop.mapper.BookMapper;
import test.project.bookshop.model.Book;
import test.project.bookshop.repository.BookRepository;
import test.project.bookshop.service.BookService;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(BookRequestDto bookRequestDto) {
        Book book = bookMapper.toBook(bookRequestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(findBookById(id));
    }

    @Override
    public BookDto delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id " + id + " does not exist.");
        }
        Book book = findBookById(id);

        bookRepository.deleteById(id);
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto update(Long id, BookRequestDto requestDto) {
        Book book = findBookById(id);

        book = bookMapper.updateBookFromDto(requestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    private Book findBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id));
    }
}
