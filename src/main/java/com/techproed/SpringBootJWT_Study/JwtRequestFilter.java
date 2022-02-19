package com.techproed.SpringBootJWT_Study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Service //@Component da kullanilabilir
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {//
        final String authorizationHeader = request.getHeader("Authorization");//postmandaki Authorization header altındaki token alıp String jwt veriable  atanacak

        String username = null;//username baslangıc degeri bos null degerini methodlarla alacak
        String jwt = null;//token baslangıc degeri bos null degerini methodlarla alacak

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {//postmand(App'den gelen)a token varsa(null değilse)  ve basında Bearer varsa
            jwt = authorizationHeader.substring(7);//postmandaki token'i'bos olan 34. satırdaki jwt'ye ata
            username = jwtUtil.extractUsername(jwt);//extractUsername() methoda jwt run ederek  username 33. satırdaki String username veriable ata(tokendan gelen username)
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {//&&sonrası->(sprinboot framework) security ile alakalı herhangi bir problem yoksa
            //tokendan gelen username varsa(null degilse) user'ın tum detay datalarını userDetails obj ata
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {//token ile user compare karsılastıracak validate edecek
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());//userDetail ve Authorities al
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));//request olusturuluyor
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);//request ve response karsılastır-->true ise App run olur
    }


}
