package com.kbetkowski.uptimemonitor.repository;

import com.kbetkowski.uptimemonitor.entity.CheckResult;
import com.kbetkowski.uptimemonitor.entity.CheckStatus;
import com.kbetkowski.uptimemonitor.entity.MonitoredSite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class CheckResultRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

    @Autowired
    private CheckResultRepository checkResultRepository;

    @Autowired
    private MonitoredSiteRepository monitoredSiteRepository;

    @Test
    void shouldCOuntCheckingMsWithin24H() {
        MonitoredSite site = monitoredSiteRepository.save(
                new MonitoredSite("Test", "https://test.com"));
        checkResultRepository.save(new CheckResult(site, 200, 100));
        checkResultRepository.save(new CheckResult(site, 200, 120));
        checkResultRepository.save(new CheckResult(site, null, 5000));

        Instant since = Instant.now().minus(1, ChronoUnit.HOURS);

        List<SiteUptimeProjection> result =
                checkResultRepository.uptimeStatsSince(since, CheckStatus.UP);

        assertEquals(1, result.size());
        assertEquals(3, result.getFirst().getTotalChecks());
        assertEquals(2, result.getFirst().getUpChecks());
        assertEquals("https://test.com", result.getFirst().getSiteUrl());
    }

    @Test
    void shouldReturnNothingWhenChecksAreExceeded() {
        MonitoredSite site = monitoredSiteRepository.save(
                new MonitoredSite("Test", "https://test.com"));

        checkResultRepository.save(new CheckResult(site, 200, 100));

        Instant since = Instant.now().plusSeconds(60);

        List<SiteUptimeProjection> result =
                checkResultRepository.uptimeStatsSince(since, CheckStatus.UP);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNotReturnSiteWithoutChecks() {
        MonitoredSite checked = monitoredSiteRepository.save(
                new MonitoredSite("Test", "https://test.com"));

        monitoredSiteRepository.save(
                new MonitoredSite("Test2", "https://test2.com"));

        checkResultRepository.save(new CheckResult(checked, 200, 100));

        Instant since = Instant.now().minus(1, ChronoUnit.HOURS);

        List<SiteUptimeProjection> result =
                checkResultRepository.uptimeStatsSince(since, CheckStatus.UP);

        assertEquals(1, result.size());
        assertEquals("https://test.com", result.getFirst().getSiteUrl());
    }
}
