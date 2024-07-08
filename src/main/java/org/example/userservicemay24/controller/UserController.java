package org.example.userservicemay24.controller;

import org.example.userservicemay24.dtos.LoginRequestDto;
import org.example.userservicemay24.dtos.SignUpRequestDto;
import org.example.userservicemay24.dtos.ValidationTokenRequestDto;
import org.example.userservicemay24.exceptions.SignupRequestException;
import org.example.userservicemay24.models.Token;
import org.example.userservicemay24.models.User;
import org.example.userservicemay24.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
        return null;
    }

    @PostMapping("/validate-token")
    private ResponseEntity<Token> validateToken(@RequestBody ValidationTokenRequestDto requestDto) {
        return null;
    }

    private void validationRequest(SignUpRequestDto requestDto) {
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