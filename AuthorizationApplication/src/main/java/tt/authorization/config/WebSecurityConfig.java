package tt.authorization.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import tt.authorization.service.UserService;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;

    /* @Bean
     public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
         return
                 http.authorizeExchange(exchanges -> exchanges
                                 .pathMatchers(
                                          "/login/**", "/registration", "/activate/*",
                                         "/recoveryPassword", "/recovery", "/recovery/*").permitAll()
                                 .pathMatchers("/confirm").hasRole("ADMIN")
                                 .pathMatchers("/app").hasAnyRole("USER","ADMIN")
                                 .anyExchange().authenticated()
                                 .and().csrf().disable())
                         .formLogin().loginPage("/login")
                         .authenticationFailureHandler((exchange, exception) -> Mono.error(exception))
                         .authenticationSuccessHandler(new WebFilterChainServerAuthenticationSuccessHandler())
                         .and().httpBasic()
                         .and()
                         .logout()
                         .requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/logout"))
                         .and().build();
     }*/
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/", "/login/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").successForwardUrl("/api").permitAll()
                .and().rememberMe()
                .and().logout().permitAll().logoutSuccessUrl("/login");
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(userService.bCryptPasswordEncoder());
    }
   /* @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userService);
        authenticationManager.setPasswordEncoder(userService.bCryptPasswordEncoder());
        return authenticationManager;
    }*/
}
