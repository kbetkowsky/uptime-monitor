package com.kbetkowski.uptimemonitor.dto;

import com.kbetkowski.uptimemonitor.repository.SiteUptimeProjection;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SiteUptimeResponseTest {
    private SiteUptimeProjection projection(long total, long up) {
        return new SiteUptimeProjection() {
            @Override
            public String getSiteName() {
                return "Test";
            }

            @Override
            public String getSiteUrl() {
                return "https://test.com";
            }

            @Override
            public long getTotalChecks() {
                return total;
            }

            @Override
            public long getUpChecks() {
                return up;
            }
        };
    }

    @Test
    void shouldCountPercent() {
        SiteUptimeResponse result = SiteUptimeResponse.from(projection(120, 119));
        assertEquals(99.17, result.uptimePercent(), 0.001);
    }

    @Test
    void shouldCountZeroPercentUptime() {
        SiteUptimeResponse result = SiteUptimeResponse.from(projection(120, 0));
        assertEquals(0.0, result.uptimePercent(), 0.001);
    }

    @Test
    void shouldCount100PercentUptime() {
        SiteUptimeResponse result = SiteUptimeResponse.from(projection(120, 120));
        assertEquals(100.0, result.uptimePercent(), 0.001);
    }
}
