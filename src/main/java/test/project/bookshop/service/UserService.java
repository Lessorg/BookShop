package test.project.bookshop.service;

import test.project.bookshop.dto.user.UserRegistrationRequestDto;
import test.project.bookshop.dto.user.UserResponseDto;
import test.project.bookshop.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
