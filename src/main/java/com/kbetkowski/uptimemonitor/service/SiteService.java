package com.kbetkowski.uptimemonitor.service;

import com.kbetkowski.uptimemonitor.entity.MonitoredSite;
import com.kbetkowski.uptimemonitor.repository.MonitoredSiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {
    private final MonitoredSiteRepository repository;

    public SiteService(MonitoredSiteRepository repository) {
        this.repository = repository;
    }

    public MonitoredSite create(String name, String url) {
        if (repository.existsByUrl(url)) {
            throw new IllegalStateException("Url already exists");
        }
        return repository.save(new MonitoredSite(name, url));
    }

    public List<MonitoredSite> findAll() {
        return repository.findAll();
    }
}
