package ps.com.viajeros.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF
                .cors(withDefaults())           // Habilitar CORS
                .authorizeHttpRequests(auth -> auth
                        // Permitir acceso sin autenticación a Swagger UI y sus recursos
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Permitir login sin autenticación
                        .requestMatchers("/api/auth/login").permitAll()
                        // Permitir login sin autenticación
                        .requestMatchers("/api/user/register").permitAll()
                        // Proteger todas las demás rutas
                        .requestMatchers("/api/user/send-otp").permitAll()
                        .requestMatchers("/api/user/recovery-mail").permitAll()
                        .requestMatchers("/api/user/reset-password").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // Proteger todas las demás rutas
                        .anyRequest().authenticated()
                );


        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();


    }
}
/*
*.anyRequest().authenticated()
*
*
*
* *

 @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF
                .authorizeHttpRequests(auth -> auth
                        // Permitir acceso sin autenticación a Swagger UI y sus recursos
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Permitir login sin autenticación
                        .requestMatchers("/api/auth/login").permitAll()
                        // Proteger todas las demás rutas
                        .anyRequest().authenticated()
                );


        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    *
    *
    *
    *
    *
      .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // Permitir acceso sin autenticación a todas las rutas
                )
                .formLogin(withDefaults());  // Puedes habilitar el formulario de login si es necesario

        // No agregar el filtro JWT temporalmente para desactivarlo
        return http.build();
* /
 */