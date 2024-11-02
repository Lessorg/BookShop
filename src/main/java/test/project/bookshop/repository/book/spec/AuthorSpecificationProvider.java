package test.project.bookshop.repository.book.spec;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import test.project.bookshop.model.Book;
import test.project.bookshop.repository.SpecificationProvider;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String AUTHOR_COLUMN_NAME = "author";

    @Override
    public String getKey() {
        return AUTHOR_COLUMN_NAME;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, cb) -> root.get(AUTHOR_COLUMN_NAME)
                .in(Arrays.stream(params).toArray());
    }
}
