package test.project.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import test.project.bookshop.dto.user.UserRegistrationRequestDto;
import test.project.bookshop.dto.user.UserResponseDto;
import test.project.bookshop.exception.EntityNotFoundException;
import test.project.bookshop.exception.RegistrationException;
import test.project.bookshop.mapper.UserMapper;
import test.project.bookshop.model.Role;
import test.project.bookshop.model.ShoppingCart;
import test.project.bookshop.model.User;
import test.project.bookshop.repository.role.RoleRepository;
import test.project.bookshop.repository.shopping.cart.ShoppingCartRepository;
import test.project.bookshop.repository.user.UserRepository;
import test.project.bookshop.service.UserService;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register user with email "
                    + requestDto.getEmail());
        }
        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(roleRepository.findByName(Role.RoleName.ROLE_USER));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        userRepository.save(user);
        shoppingCartRepository.save(shoppingCart);

        return userMapper.toUserDto(user);
    }

    @Override
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: "
                        + email));
    }
}
