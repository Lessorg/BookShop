package test.project.bookshop.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(
        @NotBlank
        String name,
        String description
) {
}
