package com.kbetkowski.uptimemonitor.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckResultTest {

    private final MonitoredSite site = new MonitoredSite("Test",
            "https://test.com");

    @Test
    void shouldBeUpWhenHttpStatusIs200() {
        CheckResult result = new CheckResult(site, 200, 120);

        assertEquals(CheckStatus.UP, result.getStatus());
    }

    @Test
    void shouldBeUpWhenHttpStatusIs301() {
        CheckResult result = new CheckResult(site, 301, 80);

        assertEquals(CheckStatus.UP, result.getStatus());
    }

    @Test
    void shouldBeDownWhenHttpStatusIs500() {
        CheckResult result = new CheckResult(site, 500, 100);

        assertEquals(CheckStatus.DOWN, result.getStatus());
    }

    @Test
    void shouldBeDownWhenThereIsNoResponse() {
        CheckResult result = new CheckResult(site, null, 5000);

        assertEquals(CheckStatus.DOWN, result.getStatus());
    }
}
