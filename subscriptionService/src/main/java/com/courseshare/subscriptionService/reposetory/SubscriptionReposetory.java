package com.courseshare.subscriptionService.reposetory;

import java.util.List;
import java.util.Optional;

import com.courseshare.subscriptionService.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionReposetory extends JpaRepository<SubscriptionEntity,Long> {

    List<SubscriptionEntity> findByUserId(long userId);
}
