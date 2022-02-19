package com.techproed.SpringBootJWT_Study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
    @GetMapping(path = "/students")
    public String getStudent(String student) {
        return "kursat Turgut beye hayırlı offerler";
    }

    @Autowired
    private JwtUtil  jwtUtil;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        //authenticate() method username ve pass ile token
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                authenticationRequest.getPassword()));//authenticationManager obj ile call edilen authenticate()--> method ile username ve pass ile token create ediyor.

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());//username alarak userdetails'e ulasıp dataları userDetails veriable atandıı

        final String jwt = jwtUtil.generateToken(userDetails);//userDetails obj ile generateToken()-->method call edilerek jwt obj token  olusturdu

        return ResponseEntity.ok(new AuthenticationResponse(jwt));//jwt obj dataları AuthenticationResponse obj atandı
    }
}
