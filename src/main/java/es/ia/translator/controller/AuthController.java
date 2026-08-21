package es.ia.translator.controller;

import es.ia.translator.exceptions.user.UserException;
import es.ia.translator.model.User;
import es.ia.translator.model.dto.LoginRequest;
import es.ia.translator.security.JwtUtil;
import es.ia.translator.service.UserAuthService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserAuthService userAuthService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @PostMapping("/login")
    public ResponseEntity<?> login( @Valid @RequestBody LoginRequest request, BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map( error -> error.getField() + ":" + error.getDefaultMessage())
                    .toList();
            throw new UserException(errors);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = (User) authentication.getPrincipal();

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getEmail()
        );

        return ResponseEntity.ok(Map.of("token", token));

    }


}
