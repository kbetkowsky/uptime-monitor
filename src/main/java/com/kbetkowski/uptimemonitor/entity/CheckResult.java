package com.kbetkowski.uptimemonitor.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "check_results")
public class CheckResult {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private MonitoredSite site;

    @Column(nullable = false)
    private Instant checkedAt;

    @Column
    private Integer httpStatus;

    @Column(nullable = false)
    private long responseTimeMs;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CheckStatus status;

    public CheckResult(MonitoredSite site, Integer httpStatus, long responseTimeMs) {
        if (site == null) {
            throw new IllegalArgumentException("Site cannot be null");
        }
        this.id = UUID.randomUUID();
        this.site = site;
        this.httpStatus = httpStatus;
        this.responseTimeMs = responseTimeMs;
        this.checkedAt = Instant.now();
        this.status = resolveStatus(httpStatus);
    }

    private static CheckStatus resolveStatus(Integer httpStatus) {
        if (httpStatus == null) {
            return CheckStatus.DOWN;
        }
        if (httpStatus >= 200 && httpStatus < 400) {
            return CheckStatus.UP;
        }
        return CheckStatus.DOWN;
    }
}
