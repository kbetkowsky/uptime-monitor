package com.kbetkowski.uptimemonitor.dto;

import com.kbetkowski.uptimemonitor.entity.MonitoredSite;

import java.time.Instant;
import java.util.UUID;

public record SiteResponse(
        UUID id,
        String name,
        String url,
        boolean enabled,
        Instant createdAt
) {
    public static SiteResponse from(MonitoredSite monitoredSite) {
        return new SiteResponse(monitoredSite.getId(), monitoredSite.getName(), monitoredSite.getUrl(),
                monitoredSite.isEnabled(), monitoredSite.getCreatedAt());
    }
}
