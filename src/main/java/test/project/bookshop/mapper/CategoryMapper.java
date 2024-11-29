package test.project.bookshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import test.project.bookshop.config.MapperConfig;
import test.project.bookshop.dto.category.CategoryDto;
import test.project.bookshop.dto.category.CategoryRequestDto;
import test.project.bookshop.model.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    Category toCategory(CategoryRequestDto categoryRequestDto);

    @Mapping(target = "id", ignore = true)
    void updateCategoryFromDto(CategoryRequestDto categoryRequestDto,
                               @MappingTarget Category category);
}
