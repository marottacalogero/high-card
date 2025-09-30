package it.sara.demo.controller;

import it.sara.demo.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    public Map<String, String> token(@RequestParam String username,
                                     @RequestParam String password) {
        // TODO: sostituisci con logica reale di login
        if (!"user".equals(username) || !"pass".equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
        String jwt = tokenService.generateToken(username, List.of("USER"));
        return Map.of("token", jwt);
    }
}