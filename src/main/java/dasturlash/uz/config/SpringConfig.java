package dasturlash.uz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SpringConfig {

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // authentication
        String password = UUID.randomUUID().toString();
        System.out.println("User Password Mazgi: " + password);

        UserDetails user = User.builder()
                .username("user")
                .password("{noop}" + password)
                .roles("USER")
                .build();

        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(user));
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // authorization
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET, "/task").permitAll()
                    .requestMatchers(HttpMethod.GET, "/task/*").permitAll()
                    .anyRequest()
                    .authenticated();
        });

        http.httpBasic(Customizer.withDefaults()); // httpBasic-dan foydanalish uchun u enable qilindi (ishlatmoqchi ekanligimiz yozilfi).

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
