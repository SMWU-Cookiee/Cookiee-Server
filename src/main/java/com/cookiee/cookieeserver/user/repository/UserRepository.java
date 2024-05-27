package com.cookiee.cookieeserver.user.repository;

//import com.cookiee.cookieeserver.domain.AuthProvider;
import com.cookiee.cookieeserver.global.domain.AuthProvider;
import com.cookiee.cookieeserver.user.domain.UserV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserV2, Long> {
    Optional<UserV2> findByUserId(Long UserId);

    Optional<UserV2> findBySocialLoginTypeAndSocialId(AuthProvider socialLoginType, String socialId);

    Optional<UserV2> findBySocialId(String socialId);

    Optional<UserV2> findByEmail(String email);
}