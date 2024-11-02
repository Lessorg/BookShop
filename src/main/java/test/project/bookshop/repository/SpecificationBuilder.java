package test.project.bookshop.repository;

import org.springframework.data.jpa.domain.Specification;
import test.project.bookshop.dto.BookSearchParametersDto;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParametersDto searchParameters);
}
