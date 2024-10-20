package test.project.bookshop;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import test.project.bookshop.model.Book;
import test.project.bookshop.service.BookService;

@SpringBootApplication
public class BookShopApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookShopApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Book Title");
            book.setAuthor("Book Author");
            book.setIsbn("Book Isbn");
            book.setDescription("Book Description");
            book.setPrice(BigDecimal.valueOf(100));

            bookService.save(book);

            System.out.println(bookService.findAll());
        };
    }
}
