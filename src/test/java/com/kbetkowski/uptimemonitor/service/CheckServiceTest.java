package com.kbetkowski.uptimemonitor.service;

import com.kbetkowski.uptimemonitor.entity.CheckResult;
import com.kbetkowski.uptimemonitor.entity.MonitoredSite;
import com.kbetkowski.uptimemonitor.repository.CheckResultRepository;
import com.kbetkowski.uptimemonitor.repository.MonitoredSiteRepository;
import com.kbetkowski.uptimemonitor.service.exception.SiteNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckServiceTest {
    @Mock
    private MonitoredSiteRepository monitoredSiteRepository;
    @Mock
    private SiteChecker siteChecker;
    @Mock
    private CheckResultRepository checkResultRepository;

    @InjectMocks
    private CheckService checkService;

    @Test
    void shouldThrowWhenSiteNotFound() {
        UUID randomUUID = UUID.randomUUID();
        when(monitoredSiteRepository.findById(randomUUID)).thenReturn(Optional.empty());
        assertThrows(SiteNotFoundException.class, () -> checkService.checkSite(randomUUID));
        verify(siteChecker, never()).check(any());
    }

    @Test
    void shouldSaveResultWhenSiteExist() {
        UUID id = UUID.randomUUID();
        MonitoredSite site = new MonitoredSite("Test", "https://test.com");
        CheckResult result = new CheckResult(site, 200, 50L);
        when(monitoredSiteRepository.findById(id)).thenReturn(Optional.of(site));
        when(siteChecker.check(site)).thenReturn(result);

        checkService.checkSite(id);

        verify(checkResultRepository).save(result);
    }

    @Test
    void shouldSaveResultsWhenCheckAllEnabledRun() {
        MonitoredSite site = new MonitoredSite("Test", "https://test.com");
        MonitoredSite site2 = new MonitoredSite("Test2", "https://test2.com");
        CheckResult result = new CheckResult(site, 200, 50L);
        CheckResult result2 = new CheckResult(site2, 200, 50L);
        when(monitoredSiteRepository.findAllByEnabledTrue()).thenReturn(List.of(site, site2));
        when(siteChecker.check(site)).thenReturn(result);
        when(siteChecker.check(site2)).thenReturn(result2);

        checkService.checkAllEnabled();

        verify(checkResultRepository).save(result);
        verify(checkResultRepository).save(result2);
        verify(checkResultRepository, times(2)).save(any());
    }

    @Test
    void shouldFindRecent() {
        MonitoredSite site = new MonitoredSite("Test", "https://test.com");
        MonitoredSite site2 = new MonitoredSite("Test2", "https://test2.com");
        CheckResult result = new CheckResult(site, 200, 50L);
        CheckResult result2 = new CheckResult(site2, 200, 50L);
        List<CheckResult> list = List.of(result, result2);
        when(checkResultRepository.findRecentWithSite()).thenReturn(list);

        List<CheckResult> recent = checkService.findRecent();

        assertEquals(list, recent);
    }
}
