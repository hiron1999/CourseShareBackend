package com.courseshare.AutorizationService.reposetory;

import java.util.Optional;

import com.courseshare.AutorizationService.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialReposetory extends JpaRepository<UserCredential,Long> {


    Optional<UserCredential> findByEmailId(String emailid);
}
