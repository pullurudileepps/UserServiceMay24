package org.example.userservicemay24.repository;

import org.example.userservicemay24.models.Token;
import org.example.userservicemay24.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findTokenByValue(String value);
    @Query(value = "select count(*) from token t where t.user_id = :id and t.is_active = 1",nativeQuery = true)
    int filterActiveUsers(@Param("id") int id);
}
