package tt.authorization.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;
import tt.authorization.service.UserService;

@Configuration
@EnableWebFluxSecurity
@AllArgsConstructor
public class WebSecurityConfig {
    private final UserService userService;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return
                http.authorizeExchange(exchanges -> exchanges
                                .pathMatchers(
                                        "/login", "/login/**", "/registration",
                                        "/confirmPassword", "/confirmEmail", "/recoveryPassword").permitAll()
                                .pathMatchers("/confirm").hasRole("ADMIN")
                                .pathMatchers("/app").hasAnyRole("USER","ADMIN")
                                .anyExchange().authenticated()
                                .and().csrf().disable())
                        .formLogin().loginPage("/login")
                        .authenticationFailureHandler((exchange, exception) -> Mono.error(exception))
                        .authenticationSuccessHandler(new WebFilterChainServerAuthenticationSuccessHandler())
                        .and().httpBasic()
                        .and().logout()
                        .requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/logout"))
                        .and().build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userService);
        authenticationManager.setPasswordEncoder(bCryptPasswordEncoder());
        return authenticationManager;
    }
}
