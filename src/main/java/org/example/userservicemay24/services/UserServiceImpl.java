package org.example.userservicemay24.services;

import org.example.userservicemay24.config.UserServiceConfigs;
import org.example.userservicemay24.exceptions.UserExistsException;
import org.example.userservicemay24.models.User;
import org.example.userservicemay24.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserServiceConfigs userServiceConfigs;

    @Autowired
    public UserServiceImpl(UserServiceConfigs userServiceConfigs, UserRepository userRepository) {
        this.userServiceConfigs = userServiceConfigs;
        this.userRepository = userRepository;
    }

    @Override
    public User signup(String name, String email, String password) throws UserExistsException {
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
}
