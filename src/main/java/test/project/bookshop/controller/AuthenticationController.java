package test.project.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import test.project.bookshop.dto.user.UserLoginRequestDto;
import test.project.bookshop.dto.user.UserLoginResponseDto;
import test.project.bookshop.dto.user.UserRegistrationRequestDto;
import test.project.bookshop.dto.user.UserResponseDto;
import test.project.bookshop.exception.RegistrationException;
import test.project.bookshop.security.AuthenticationService;
import test.project.bookshop.service.UserService;

@Tag(name = "User Registration", description = "Operations related to user registration")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user",
            description = "Registers a new user with the provided details")
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @Operation(
            summary = "Login a user",
            description = "Authenticates a user and returns a JWT token"
    )
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
