package com.cookiee.cookieeserver.user.repository;

//import com.cookiee.cookieeserver.domain.AuthProvider;
import com.cookiee.cookieeserver.global.domain.AuthProvider;
import com.cookiee.cookieeserver.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long UserId);

    Optional<User> findByAuthProviderAndSocialId(AuthProvider authProvider, String socialId);
}
