package com.biza.domain;

import java.time.Instant;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass @Getter
public abstract class AuditEntity {
    @Column(name="created_at", nullable=false, updatable=false)
    private Instant createdAt = Instant.now();

    @UpdateTimestamp
    @Column(name="updated_at", nullable=false)
    private Instant updatedAt;
}
