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
    public BookDto findByid(Long id) {
        return bookMapper.toDto(bookRepository.findByid(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id)));
    }
}
