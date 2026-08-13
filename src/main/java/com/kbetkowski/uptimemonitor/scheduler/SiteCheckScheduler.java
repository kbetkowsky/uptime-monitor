package com.kbetkowski.uptimemonitor.scheduler;

import com.kbetkowski.uptimemonitor.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SiteCheckScheduler {
    private final CheckService checkService;

    @Scheduled(fixedDelay = 60000)
    public void checkAllSites() {
        checkService.checkAllEnabled();
    }
}
