package com.kbetkowski.uptimemonitor.controller;

import com.kbetkowski.uptimemonitor.dto.CheckResultResponse;
import com.kbetkowski.uptimemonitor.dto.CreateSiteRequest;
import com.kbetkowski.uptimemonitor.dto.SiteResponse;
import com.kbetkowski.uptimemonitor.entity.CheckResult;
import com.kbetkowski.uptimemonitor.entity.MonitoredSite;
import com.kbetkowski.uptimemonitor.service.CheckService;
import com.kbetkowski.uptimemonitor.service.SiteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sites")
public class SiteController {
    private final SiteService siteService;
    private final CheckService checkService;

    public SiteController(SiteService siteService, CheckService checkService) {
        this.siteService = siteService;
        this.checkService = checkService;
    }


    @PostMapping
    public ResponseEntity<SiteResponse> create(@Valid @RequestBody CreateSiteRequest request) {
        MonitoredSite site = siteService.create(request.name(), request.url());
        return ResponseEntity.status(HttpStatus.CREATED).body(SiteResponse.from(site));
    }

    @GetMapping
    public ResponseEntity<List<SiteResponse>> findAll() {
        List<SiteResponse> sites = siteService.findAll().stream().map(SiteResponse::from)
                .toList();
        return ResponseEntity.ok(sites);
    }

    @PostMapping("/{id}/check")
    public ResponseEntity<CheckResultResponse> check(@PathVariable UUID id) {
        CheckResult result = checkService.checkSite(id);
        return ResponseEntity.ok(CheckResultResponse.from(result));
    }
}
