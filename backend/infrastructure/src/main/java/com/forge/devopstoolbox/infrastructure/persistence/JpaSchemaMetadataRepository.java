package com.forge.devopstoolbox.infrastructure.persistence;

import com.forge.devopstoolbox.core.model.AppMetadata;
import com.forge.devopstoolbox.core.port.SchemaMetadataRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
class JpaSchemaMetadataRepository implements SchemaMetadataRepository {

    private final SchemaMetadataJpaRepository jpaRepository;

    JpaSchemaMetadataRepository(SchemaMetadataJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<AppMetadata> findAll() {
        return jpaRepository.findAllByOrderByKeyAsc().stream()
                .map(this::toDomain)
                .toList();
    }

    private AppMetadata toDomain(SchemaMetadataEntity entity) {
        return new AppMetadata(entity.getKey(), entity.getValue());
    }
}
