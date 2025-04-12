package learn.review_tique.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.cors();

        http.authorizeRequests()
                // TODO add antMatchers here to configure access to specific API endpoints
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                .antMatchers("/api/v1/user/authenticate").permitAll()
                .antMatchers("/api/v1/games").permitAll()
                .antMatchers("/api/v1/games/**").permitAll()
                .antMatchers("/api/v1/developers/**").permitAll()
                .antMatchers("/api/v1/platforms/**").permitAll()
                .antMatchers("/api/v1/genres/**").permitAll()
                .antMatchers("/api/v1/games/{gameId}").permitAll()
                .antMatchers("/api/v1/user/register/user").permitAll()
                .antMatchers("/api/v1/user/register/critic").permitAll()
                .antMatchers("/api/v1/user/{userId}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/reviews/{reviewId}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/reactions/{reviewId}/{userId}").permitAll()
                .antMatchers("/api/v1/reviews/game/{gameId}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/reviews").permitAll()
                .antMatchers( "/api/v1/games/search").permitAll()
                // require authentication for any request...
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtRequestFilter(authenticationManager(), converter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("*");
            }
        };
    }
}
