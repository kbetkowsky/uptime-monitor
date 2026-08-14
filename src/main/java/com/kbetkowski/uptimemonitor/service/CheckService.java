package com.kbetkowski.uptimemonitor.service;

import com.kbetkowski.uptimemonitor.entity.CheckResult;
import com.kbetkowski.uptimemonitor.entity.MonitoredSite;
import com.kbetkowski.uptimemonitor.repository.CheckResultRepository;
import com.kbetkowski.uptimemonitor.repository.MonitoredSiteRepository;
import com.kbetkowski.uptimemonitor.service.exception.SiteNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
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

    public void checkAllEnabled() {
        List<MonitoredSite> sites = monitoredSiteRepository.findAllByEnabledTrue();

        for (MonitoredSite site : sites) {
            CheckResult result = siteChecker.check(site);
            checkResultRepository.save(result);
            log.info("Checked {} - {} ({} ms)", site.getUrl(), result.getStatus(), result.getResponseTimeMs());
        }
    }

    public List<CheckResult> findRecent() {
        return checkResultRepository.findRecentWithSite();
    }
}
