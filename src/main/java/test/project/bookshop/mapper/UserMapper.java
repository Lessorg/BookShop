package test.project.bookshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import test.project.bookshop.config.MapperConfig;
import test.project.bookshop.dto.user.UserRegistrationRequestDto;
import test.project.bookshop.dto.user.UserResponseDto;
import test.project.bookshop.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    User toUser(UserRegistrationRequestDto userDto);
}
