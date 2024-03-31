package com.cookiee.cookieeserver.login.google.controller;

import com.cookiee.cookieeserver.login.google.GoogleLoginService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/login/oauth2", produces = "application/json")
public class GoogleLoginController {
    private final GoogleLoginService gooleLoginServcie;

    public GoogleLoginController(GoogleLoginService googleLoginService, GoogleLoginService gooleLoginServcie){
        this.gooleLoginServcie = gooleLoginServcie;
    }

    @GetMapping("/code/{registrationId}")
    public void googleLogin(@RequestParam String code, @PathVariable String registrationId){
        gooleLoginServcie.socialLogin(code, registrationId);
    }
}
