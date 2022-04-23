package etf.unsa.ba.nwt.emajstor.apigateway.filters;

import etf.unsa.ba.nwt.emajstor.apigateway.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private static Logger logger = LoggerFactory.getLogger(JwtTokenAuthenticationFilter.class);

    public JwtTokenAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils= jwtUtils;
    }


    private List<GrantedAuthority> fetchRoles(String x) {
        Set<String> rl = new HashSet<>();
        rl.add("ROLE_USER");
        if (x.contains("ADMIN"))  rl.add("ROLE_ADMIN");
        return rl.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

//        if(header == null || !header.startsWith("Bearer")) {
//            chain.doFilter(request, response);
//            return;
//        }

        String token = header.replace("Bearer ", "");
        logger.info(token);
        logger.info("|" + jwtUtils.getJwtSecret());
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtUtils.getJwtSecret()).parseClaimsJws(token).getBody();

            String username = claims.getSubject();
            logger.info(username);
            if(username != null) {

                List<String> authorities = (List<String>) claims.get("authorities");

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, fetchRoles(claims.get("authorities").toString()));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }

}