package com.forge.infrastructure.persistence;

import com.forge.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, String> {
    List<NotificationEntity> findAllByOrderByTimestampDesc();
}
