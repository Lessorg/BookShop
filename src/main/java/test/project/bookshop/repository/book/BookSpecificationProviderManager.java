package test.project.bookshop.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import test.project.bookshop.model.Book;
import test.project.bookshop.repository.SpecificationProvider;
import test.project.bookshop.repository.SpecificationProviderManager;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProvider;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProvider.stream().filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No specification provider found for key: " + key));
    }
}
