package com.cookiee.cookieeserver.user.repository;

import com.cookiee.cookieeserver.user.domain.UserV1;
import com.cookiee.cookieeserver.user.domain.UserV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryV1 extends JpaRepository<UserV1, Long> {
    Optional<UserV1> findByDeviceId(String deviceId);

}
