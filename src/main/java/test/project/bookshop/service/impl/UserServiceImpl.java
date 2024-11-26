package test.project.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import test.project.bookshop.dto.user.UserRegistrationRequestDto;
import test.project.bookshop.dto.user.UserResponseDto;
import test.project.bookshop.exception.RegistrationException;
import test.project.bookshop.mapper.UserMapper;
import test.project.bookshop.model.Role;
import test.project.bookshop.model.User;
import test.project.bookshop.repository.role.RoleRepository;
import test.project.bookshop.repository.user.UserRepository;
import test.project.bookshop.service.UserService;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register user with email "
                    + requestDto.getEmail());
        }
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        User user = userMapper.toUser(requestDto);
        user.setRoles(roleRepository.findByName(Role.RoleName.ROLE_USER));
        return userMapper.toUserDto(userRepository.save(user));
    }
}
