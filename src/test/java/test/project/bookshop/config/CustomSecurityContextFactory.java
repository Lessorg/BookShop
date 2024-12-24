package test.project.bookshop.config;

import java.util.Set;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import test.project.bookshop.model.Role;

public class CustomSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        test.project.bookshop.model.User user = new test.project.bookshop.model.User();
        user.setId(customUser.id());
        user.setEmail(customUser.email());
        user.setPassword("password");
        Role role = new Role();
        role.setName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(role));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication auth = new UsernamePasswordAuthenticationToken(user,
                null, user.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
