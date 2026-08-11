package com.kbetkowski.uptimemonitor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "monitored_sites")
public class MonitoredSite {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private boolean enabled;
    @Column(nullable = false)
    private Instant createdAt;

    public MonitoredSite(String name, String url) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Url cannot be blank");
        }
        this.id = UUID.randomUUID();
        this.name = name;
        this.url = url;
        this.enabled = true;
        this.createdAt = Instant.now();
    }
}
