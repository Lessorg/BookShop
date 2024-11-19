package test.project.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.project.bookshop.dto.user.UserRegistrationRequestDto;
import test.project.bookshop.dto.user.UserResponseDto;
import test.project.bookshop.exception.RegistrationException;
import test.project.bookshop.mapper.UserMapper;
import test.project.bookshop.model.User;
import test.project.bookshop.repository.user.UserRepository;
import test.project.bookshop.service.UserService;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user");
        }
        User savedUser = userRepository.save(userMapper.toUser(requestDto));
        return userMapper.toUserDto(savedUser);
    }
}
