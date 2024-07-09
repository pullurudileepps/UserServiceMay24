package org.example.userservicemay24.services;

import org.example.userservicemay24.exceptions.ExpiredTokenExcepton;
import org.example.userservicemay24.exceptions.InvalidTokenException;
import org.example.userservicemay24.exceptions.PasswordMissMatchException;
import org.example.userservicemay24.exceptions.UserNotFoundException;
import org.example.userservicemay24.models.Token;
import org.example.userservicemay24.models.User;

public interface UserService {
    User signup(String name, String email, String password) throws Exception;
    Token login(String email, String password) throws Exception;
    Token validateToken(String tokenValue) throws InvalidTokenException, ExpiredTokenExcepton;
    void logout(String tokenValue) throws Exception;
}

