package org.example.userservicemay24.controller;

import org.example.userservicemay24.dtos.LoginRequestDto;
import org.example.userservicemay24.dtos.LogoutRequestDto;
import org.example.userservicemay24.dtos.SignUpRequestDto;
import org.example.userservicemay24.dtos.ValidationTokenRequestDto;
import org.example.userservicemay24.exceptions.*;
import org.example.userservicemay24.models.Token;
import org.example.userservicemay24.models.User;
import org.example.userservicemay24.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sayHello")
    public String sayHello(){
        return "Hello Dileep, How are you!";
    }

    @PostMapping("/signUp")
    private ResponseEntity<User> signUp(@RequestBody SignUpRequestDto requestDto) {
        try {
            validationRequest(requestDto);
            User user = this.userService.signup(requestDto.getName(), requestDto.getEmail(), requestDto.getPassword());
            return new ResponseEntity<>(user, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }

    @PostMapping("/login")
    private ResponseEntity<Token> login(@RequestBody LoginRequestDto requestDto) {
        try {
            if (requestDto.getEmail().isEmpty() || requestDto.getPassword().isEmpty())
                throw new InvalidLoginRequestException("Email or Password cannot be empty");
            Token login = this.userService.login(requestDto.getEmail(), requestDto.getPassword());
            return new ResponseEntity<>(login, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/validate-token")
    private ResponseEntity<Token> validateToken(@RequestBody ValidationTokenRequestDto requestDto) {
        try {
            if (requestDto.getToken().isEmpty())
                throw new Exception("Token cannot be empty");
            Token token = this.userService.validateToken(requestDto.getToken());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (ExpiredTokenExcepton ETE) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    private ResponseEntity<Void> logout(@RequestBody LogoutRequestDto requestDto) {
        try {
            if (requestDto.getToken().isEmpty())
                throw new InvalidTokenException("Token cannot be empty");
            this.userService.logout(requestDto.getToken());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private void validationRequest(SignUpRequestDto requestDto) throws SignupRequestException {
        if (requestDto.getName().isEmpty()) {
            throw new SignupRequestException("user name should be present");
        }
        if (requestDto.getEmail().isEmpty()) {
            throw new SignupRequestException("email cannot be empty");
        }
        if (requestDto.getPassword().isEmpty()) {
            throw new SignupRequestException("password cannot be empty");
        }
    }
}