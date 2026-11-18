
package com.klu.controller;

import com.klu.dto.JwtRequest;
import com.klu.dto.JwtResponse;
import com.klu.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jwt")
public class JwtController {

    private final JwtUtil jwtUtil;

    public JwtController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/validate")
    public ResponseEntity<JwtResponse> validateToken(
            @RequestBody JwtRequest request) {

        try {
            if (request == null || request.getToken() == null
                    || request.getToken().isBlank()) {
                throw new IllegalArgumentException("Empty token");
            }

            Claims claims =
                    jwtUtil.validateToken(request.getToken());

            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            return ResponseEntity.ok(
                new JwtResponse(
                    true,
                    username,
                    role,
                    "Token is valid"
                )
            );

        } catch (JwtException | IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                    new JwtResponse(
                        false,
                        null,
                        null,
                        "Invalid token"
                    )
                );
        }
    }
}
