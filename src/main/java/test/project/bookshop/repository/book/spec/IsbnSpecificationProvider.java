package test.project.bookshop.repository.book.spec;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import test.project.bookshop.model.Book;
import test.project.bookshop.repository.specefication.SpecificationProvider;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String ISBN_COLUMN_NAME = "isbn";

    @Override
    public String getKey() {
        return ISBN_COLUMN_NAME;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, cb) -> root.get(ISBN_COLUMN_NAME)
                .in(Arrays.stream(params).toArray());
    }
}
