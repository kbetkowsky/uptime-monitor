package com.kbetkowski.uptimemonitor.repository;

import com.kbetkowski.uptimemonitor.entity.MonitoredSite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MonitoredSiteRepository extends JpaRepository<MonitoredSite, UUID> {
    boolean existsByUrl(String url);
}
