package org.example.userservicemay24.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.userservicemay24.config.UserServiceConfigs;
import org.example.userservicemay24.exceptions.*;
import org.example.userservicemay24.models.Token;
import org.example.userservicemay24.models.User;
import org.example.userservicemay24.repository.TokenRepository;
import org.example.userservicemay24.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserServiceConfigs userServiceConfigs;
    private final TokenRepository tokenRepository;

    @Autowired
    public UserServiceImpl(TokenRepository tokenRepository, UserRepository userRepository, UserServiceConfigs userServiceConfigs) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.userServiceConfigs = userServiceConfigs;
    }

    @Override
    public User signup(String name, String email, String password) throws Exception {
        Optional<User> userByEmail = this.userRepository.findUserByEmail(email);
        if(userByEmail.isPresent()){
            throw new UserExistsException("User is already present");
        }
        String encodedPassword = this.userServiceConfigs.getBCryptPasswordInstance().encode(password);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        return this.userRepository.save(user);
    }

    @Override
    public Token login(String email, String password) throws Exception {
        Optional<User> userByEmail = this.userRepository.findUserByEmail(email);
        User user = userByEmail.orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean matches = this.userServiceConfigs.getBCryptPasswordInstance().matches(password, user.getPassword());
        if (matches) {
            int count = this.tokenRepository.filterActiveUsers(user.getId());
            if(count > 2){
                throw new InvalidLoginRequestException("can't be allow more than 2 session");
            }
            String value = RandomStringUtils.randomAlphanumeric(128);
            Calendar c = Calendar.getInstance();
            c.add(Calendar.HOUR, 1);
            Date everyFiveMinutes = c.getTime();//EPOC
            Token token = new Token();
            token.setUser(user);
            token.setValue(value);
            token.setExpiresAt(everyFiveMinutes);
            token.setActive(true);
            return this.tokenRepository.save(token);
        } else {
            throw new PasswordMissMatchException("Password is incorrect");
        }
    }

    @Override
    public Token validateToken(String tokenValue) throws InvalidTokenException, ExpiredTokenExcepton {
        Optional<Token> tokenByValue = this.tokenRepository.findTokenByValue(tokenValue);
        Token token = tokenByValue.orElseThrow(() -> new InvalidTokenException("Please use valid token"));
//        Date now = new Date();
//        if (now.after(token.getExpiresAt()) || !token.isActive())
//            throw new ExpiredTokenExcepton("the token has expired");
        return token;
    }

    @Override
    public void logout(String tokenValue) throws Exception {
        Optional<Token> tokenByValue = this.tokenRepository.findTokenByValue(tokenValue);
        Token token = tokenByValue.orElseThrow(() -> new InvalidTokenException("Please use valid token"));
        if (token.isActive()) {
            token.setActive(false);
            this.tokenRepository.save(token);
        }
    }
}
