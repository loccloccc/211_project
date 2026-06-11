package ra.project._11_project.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ra.project._11_project.security.principal.CustomUserDetailsService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println("=================================");
        System.out.println("JWT FILTER RUNNING");
        System.out.println("REQUEST URI = " + request.getRequestURI());

        String header = request.getHeader("Authorization");

        System.out.println("HEADER = " + header);

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            System.out.println("TOKEN = " + token);

            boolean valid = jwtProvider.validateToken(token);

            System.out.println("VALID TOKEN = " + valid);

            if (valid) {

                String username =
                        jwtProvider.getUsernameFromToken(token);

                System.out.println("USERNAME = " + username);

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(username);

                System.out.println(
                        "AUTHORITIES = "
                                + userDetails.getAuthorities()
                );

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(auth);

                System.out.println(
                        "AUTHENTICATION = "
                                + SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                );
            }
        }

        filterChain.doFilter(request, response);
    }
}