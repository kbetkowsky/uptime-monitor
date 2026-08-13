package com.kbetkowski.uptimemonitor.dto;

import com.kbetkowski.uptimemonitor.entity.CheckResult;
import com.kbetkowski.uptimemonitor.entity.CheckStatus;

import java.time.Instant;
import java.util.UUID;

public record CheckResultResponse(
        UUID id,
        UUID siteId,
        Instant checkedAt,
        Integer httpStatus,
        long responseTimeMs,
        CheckStatus status
) {
    public static CheckResultResponse from(CheckResult result) {
        return new CheckResultResponse(
                result.getId(),
                result.getSite().getId(),
                result.getCheckedAt(),
                result.getHttpStatus(),
                result.getResponseTimeMs(),
                result.getStatus()
        );
    }
}
