package com.courseshare.AutorizationService.reposetory;

import java.util.Optional;

import com.courseshare.AutorizationService.entity.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenReposetory extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findById(long id);
}
