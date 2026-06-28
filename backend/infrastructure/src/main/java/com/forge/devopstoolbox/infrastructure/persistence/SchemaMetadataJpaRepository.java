package com.forge.devopstoolbox.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface SchemaMetadataJpaRepository extends JpaRepository<SchemaMetadataEntity, Long> {

    List<SchemaMetadataEntity> findAllByOrderByKeyAsc();
}
