package com.kbetkowski.uptimemonitor.controller;

import com.kbetkowski.uptimemonitor.dto.RecentCheckResponse;
import com.kbetkowski.uptimemonitor.service.CheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checks")
public class CheckController {
    private final CheckService checkService;

    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }

    @GetMapping("/recent")
    public ResponseEntity<List<RecentCheckResponse>> recent() {
        List<RecentCheckResponse> sites = checkService.findRecent()
                .stream().map(RecentCheckResponse::from).toList();
        return ResponseEntity.ok(sites);
    }
}
