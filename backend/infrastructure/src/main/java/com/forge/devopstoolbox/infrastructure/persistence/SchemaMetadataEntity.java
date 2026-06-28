package com.forge.devopstoolbox.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "schema_metadata")
class SchemaMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String key;

    @Column(nullable = false)
    private String value;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected SchemaMetadataEntity() {
    }

    String getKey() {
        return key;
    }

    String getValue() {
        return value;
    }
}
