package com.kbetkowski.uptimemonitor.service;

import com.kbetkowski.uptimemonitor.entity.CheckResult;
import com.kbetkowski.uptimemonitor.entity.MonitoredSite;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SiteChecker {
    private final RestClient restClient;

    public SiteChecker(RestClient restClient) {
        this.restClient = restClient;
    }

    public CheckResult check(MonitoredSite site) {
        long start = System.currentTimeMillis();
        Integer status;
        try {
            status = restClient.get()
                    .uri(site.getUrl())
                    .exchange((clientRequest, clientResponse) ->
                            clientResponse.getStatusCode().value());
        } catch (Exception e) {
            status = null;
        }

        long duration = System.currentTimeMillis() - start;
        return new CheckResult(site, status, duration);
    }
}
