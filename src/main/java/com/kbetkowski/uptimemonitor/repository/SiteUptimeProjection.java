package com.kbetkowski.uptimemonitor.repository;

public interface SiteUptimeProjection {
    String getSiteName();
    String getSiteUrl();
    long getTotalChecks();
    long getUpChecks();
}
