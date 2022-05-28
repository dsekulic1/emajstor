package etf.unsa.ba.nwt.emajstor.apigateway.config;


import etf.unsa.ba.nwt.emajstor.apigateway.filters.JwtTokenAuthenticationFilter;
import etf.unsa.ba.nwt.emajstor.apigateway.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtUtils jwtUtils;

    private final String[] adminRoutes = {"/user/api/**"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println(http.toString());
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                // Add a filter to validate the tokens with every request
                .addFilterAfter(new JwtTokenAuthenticationFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
                // authorization requests config
                .authorizeRequests()
                // allow all who are accessing "auth" service
                //.antMatchers(adminRoutes).hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/user/api/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/review/api/**").permitAll()
                .antMatchers(HttpMethod.GET, "/job/api/**").permitAll()
                .antMatchers(HttpMethod.POST, "/job/api/**").permitAll()
                .antMatchers(HttpMethod.GET, "/communication/api/**").permitAll()
                // must be an admin if trying to access admin area (authentication is also required here)
                .antMatchers(adminRoutes).hasRole("ADMIN")
                // Any other request must be authenticated
                .anyRequest().authenticated();
    }

    @Bean
    public JwtUtils jwtConfig() {
        return new JwtUtils();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "PATCH"));
        configuration.setAllowCredentials(false);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
