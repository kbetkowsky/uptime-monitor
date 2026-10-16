package com.kbetkowski.uptimemonitor.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest
    @CsvSource({"199, DOWN", "200, UP", "399, UP", "400, DOWN", "500, DOWN", ", DOWN"})
    void shouldResolveStatusAtBoundaries(Integer httpStatus, CheckStatus expectedStatus) {
        CheckResult result = new CheckResult(site, httpStatus, 100);

        assertEquals(expectedStatus, result.getStatus());
    }
}
