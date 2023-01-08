package tt.authorization.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import tt.authorization.service.UserService;

@Configuration
@EnableWebFluxSecurity
@AllArgsConstructor
public class WebFluxAuthorizationConfig {
    private final UserService userService;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return
                http.authorizeExchange(exchanges -> exchanges
                                .pathMatchers("/login", "/login/**").permitAll()
                                .anyExchange().authenticated()
                                .and().csrf().disable())
                        .formLogin().loginPage("/login")
                        .and().logout()
                        .requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/logout"))
                        .and().build();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userService);
        authenticationManager.setPasswordEncoder(userService.bCryptPasswordEncoder());
        return authenticationManager;
    }
}
