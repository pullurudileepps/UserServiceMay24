package org.example.userservicemay24.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UserServiceConfigs {
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordInstance(){
        return new BCryptPasswordEncoder();
    }
}
