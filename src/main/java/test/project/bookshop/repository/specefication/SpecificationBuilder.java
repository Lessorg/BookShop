package test.project.bookshop.repository.specefication;

import org.springframework.data.jpa.domain.Specification;
import test.project.bookshop.dto.book.BookSearchParametersDto;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParametersDto searchParameters);
}
