package com.kbetkowski.uptimemonitor.dto;

import com.kbetkowski.uptimemonitor.repository.SiteUptimeProjection;

public record SiteUptimeResponse(
        String siteName,
        String siteUrl,
        long totalChecks,
        long upChecks,
        double uptimePercent
) {
    public static SiteUptimeResponse from(SiteUptimeProjection p) {
        double percent = 100.0 * p.getUpChecks() / p.getTotalChecks();
        return new SiteUptimeResponse(
                p.getSiteName(),
                p.getSiteUrl(),
                p.getTotalChecks(),
                p.getUpChecks(),
                Math.round(percent * 100) / 100.0
        );
    }
}