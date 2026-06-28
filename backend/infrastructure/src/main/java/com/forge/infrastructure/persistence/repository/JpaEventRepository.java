package com.forge.infrastructure.persistence.repository;

import com.forge.infrastructure.persistence.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaEventRepository extends JpaRepository<EventEntity, String> {
    List<EventEntity> findAllByOrderByTimestampDesc();
}
