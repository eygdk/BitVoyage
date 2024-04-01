package com.backend.BitVoyage.repository;

import com.backend.BitVoyage.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderId(String providerId); // providerId를 기준으로 사용자를 찾는 메소드
}
