package test.project.bookshop.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import test.project.bookshop.dto.user.UserLoginRequestDto;
import test.project.bookshop.dto.user.UserLoginResponseDto;
import test.project.bookshop.utils.JwtUtil;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String token = jwtUtil.generateToken(request.email());
        return new UserLoginResponseDto(token);
    }
}
