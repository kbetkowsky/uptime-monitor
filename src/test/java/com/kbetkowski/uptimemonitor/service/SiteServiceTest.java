package com.kbetkowski.uptimemonitor.service;

import com.kbetkowski.uptimemonitor.entity.MonitoredSite;
import com.kbetkowski.uptimemonitor.repository.MonitoredSiteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SiteServiceTest {
    @Mock
    private MonitoredSiteRepository monitoredSiteRepository;

    @InjectMocks
    private SiteService siteService;

    @Test
    void shouldRejectDuplicateSite() {
        when(monitoredSiteRepository.existsByUrl("test.com")).thenReturn(true);
        assertThrows(IllegalStateException.class,
                () -> siteService.create("test", "test.com"));
        verify(monitoredSiteRepository, never()).save(any());
    }

    @Test
    void shouldCreateSite() {
        when(monitoredSiteRepository.existsByUrl("test.com")).thenReturn(false);
        when(monitoredSiteRepository.save(any())).thenReturn(new MonitoredSite("test",
                "test.com"));
        MonitoredSite check = siteService.create("test", "test.com");
        assertNotNull(check);
        ArgumentCaptor<MonitoredSite> captor = ArgumentCaptor.forClass(MonitoredSite.class);
        verify(monitoredSiteRepository).save(captor.capture());
        MonitoredSite saved = captor.getValue();
        assertEquals("test.com", saved.getUrl());
        assertEquals("test", saved.getName());
    }
}
