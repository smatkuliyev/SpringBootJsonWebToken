package com.techproed.SpringBootJWT_Study;

public class AuthenticationResponse {// App'den gelen token'i saklayacak class
    // bu class'dan create edilen obj app'den gelen token bulunduracak
    private final String jwt;

    public AuthenticationResponse(String jwt) {//final jwt initialize etmek icin cons create edildi
        this.jwt = jwt;
    }

    public String getJwt() {//App'den gelen token validate method'unda onaylanmasi icin okunmasi lazim onun icin getter create edildi
        return jwt;
    }
}
