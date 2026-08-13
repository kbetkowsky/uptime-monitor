package com.kbetkowski.uptimemonitor.service;

import com.kbetkowski.uptimemonitor.entity.CheckResult;
import com.kbetkowski.uptimemonitor.entity.MonitoredSite;
import com.kbetkowski.uptimemonitor.repository.CheckResultRepository;
import com.kbetkowski.uptimemonitor.repository.MonitoredSiteRepository;
import com.kbetkowski.uptimemonitor.service.exception.SiteNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CheckService {
    private final MonitoredSiteRepository monitoredSiteRepository;
    private final SiteChecker siteChecker;
    private final CheckResultRepository checkResultRepository;

    public CheckService(MonitoredSiteRepository monitoredSiteRepository, SiteChecker siteChecker, CheckResultRepository checkResultRepository) {
        this.monitoredSiteRepository = monitoredSiteRepository;
        this.siteChecker = siteChecker;
        this.checkResultRepository = checkResultRepository;
    }

    public CheckResult checkSite(UUID siteId) {
        MonitoredSite site = monitoredSiteRepository.findById(siteId)
                .orElseThrow(() -> new SiteNotFoundException("Site not found: " + siteId));
        CheckResult result = siteChecker.check(site);
        return checkResultRepository.save(result);
    }
}
