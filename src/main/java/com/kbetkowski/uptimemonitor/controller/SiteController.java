package com.kbetkowski.uptimemonitor.controller;

import com.kbetkowski.uptimemonitor.dto.CreateSiteRequest;
import com.kbetkowski.uptimemonitor.dto.SiteResponse;
import com.kbetkowski.uptimemonitor.entity.MonitoredSite;
import com.kbetkowski.uptimemonitor.service.SiteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sites")
public class SiteController {
    private final SiteService service;

    public SiteController(SiteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SiteResponse> create(@Valid @RequestBody CreateSiteRequest request) {
        MonitoredSite site = service.create(request.name(), request.url());
        return ResponseEntity.status(HttpStatus.CREATED).body(SiteResponse.from(site));
    }

    @GetMapping
    public ResponseEntity<List<SiteResponse>> findAll() {
        List<SiteResponse> sites = service.findAll().stream().map(SiteResponse::from)
                .toList();
        return ResponseEntity.ok(sites);
    }
}
