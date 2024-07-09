package org.example.userservicemay24.repository;

import org.example.userservicemay24.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findTokenByValue(String value);
}
