package com.blogger.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.blogger.entities.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByRefreshToken(String refreshToken);
	
	@Query(value = "SELECT rt.* FROM REFRESH_TOKENS rt " +
            "INNER JOIN USER_DETAILS ud ON rt.user_id = ud.id " +
            "WHERE ud.EMAIL = :userEmail and rt.revoked = false ", nativeQuery = true)
    List<RefreshToken> findAllRefreshTokenByUserEmailId(String userEmail);
}
