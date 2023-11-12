package com.antongoranov.demojwt.repository;

import com.antongoranov.demojwt.model.entity.TokenEntity;
import com.antongoranov.demojwt.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    @Modifying
    @Query("update TokenEntity t set t.token = :jwtToken where t.user = :userEntity")
    void refreshTokenByUser(@Param("userEntity") UserEntity userEntity,
                            @Param("jwtToken") String jwtToken);

    void deleteByUser(UserEntity user);

    Optional<TokenEntity> findByToken(String jwtToken);

    Optional<TokenEntity> findByUser(UserEntity user);
}
