package etf.unsa.ba.nwt.emajstor.user.security;

import etf.unsa.ba.nwt.emajstor.user.service.PersonDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final PersonDetailsService personDetailsService;
    private final JwtUtils jwtUtils;
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    public AuthTokenFilter(PersonDetailsService personDetailsService, JwtUtils jwtUtils) {
        this.personDetailsService = personDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String headerAuth = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (StringUtils.hasText(headerAuth)) {
            if (headerAuth.startsWith("Bearer ")) {
                jwtToken = headerAuth.substring(7);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token must begin with Bearer keyword");
                return;
            }
            try {
                username = jwtUtils.extractUsername(jwtToken);
            } catch (Exception ignore) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT error");
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            PersonDetails personDetails = personDetailsService.loadUserByUsername(username);
            if (jwtUtils.validateToken(jwtToken, personDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(personDetails, null, null);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);

    }

}
