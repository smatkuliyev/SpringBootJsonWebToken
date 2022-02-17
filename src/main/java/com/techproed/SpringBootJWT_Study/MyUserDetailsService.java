package com.techproed.SpringBootJWT_Study;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {
// JWT icin MyUserDetailsService class mutlaka create edilmeli. loadUserByUsername() method ile app'e user upload edilmeli

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // bu method app'e user'in username password ve authorizes tanimladi --> cubuk adan tum vasiflariyla create edildi
        return new User("admin", "nimda", new ArrayList<>()); //new ArrayList<>() -> collection type,
        // authorizes birden fazla ve sabilt oldugu icin depolayacak bit vor array create edildi
    }
}
