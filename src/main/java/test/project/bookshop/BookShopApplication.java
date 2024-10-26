package test.project.bookshop;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import test.project.bookshop.dto.BookRequestDto;
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
            BookRequestDto book1 = new BookRequestDto();
            book1.setTitle("Sample Book 1");
            book1.setAuthor("Author A");
            book1.setIsbn("9781234567897");
            book1.setPrice(BigDecimal.valueOf(19.99));
            book1.setDescription("This is a sample book description.");
            book1.setCoverImage("http://example.com/cover1.jpg");

            bookService.save(book1);

            BookRequestDto book2 = new BookRequestDto();
            book2.setTitle("Sample Book 2");
            book2.setAuthor("Author B");
            book2.setIsbn("9789876543210");
            book2.setPrice(BigDecimal.valueOf(24.99));
            book2.setDescription("Another sample book description.");
            book2.setCoverImage("http://example.com/cover2.jpg");

            bookService.save(book2);

            System.out.println("HHH" + bookService.findAll());
        };
    }
}
