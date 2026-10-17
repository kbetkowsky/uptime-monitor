package com.kbetkowski.uptimemonitor.dto;

import com.kbetkowski.uptimemonitor.entity.CheckResult;
import com.kbetkowski.uptimemonitor.entity.CheckStatus;
import com.kbetkowski.uptimemonitor.entity.MonitoredSite;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class RecentCheckResponseTest {

    MonitoredSite site = new MonitoredSite("Test", "https://test.com");
    CheckResult checkResult = new CheckResult(site, 200, 50L);

    @Test
    void shouldMapSiteAndStatusFromCheckResult() {
        RecentCheckResponse response = RecentCheckResponse.from(checkResult);

        assertAll("recent check mapping",
                () -> assertEquals(CheckStatus.UP, response.status()),
                () -> assertEquals("Test", response.siteName()),
                () -> assertEquals("https://test.com", response.siteUrl())
                )
        ;
    }
}
