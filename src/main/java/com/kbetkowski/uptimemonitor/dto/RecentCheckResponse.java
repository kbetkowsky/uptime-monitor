package com.kbetkowski.uptimemonitor.dto;

import com.kbetkowski.uptimemonitor.entity.CheckResult;
import com.kbetkowski.uptimemonitor.entity.CheckStatus;

import java.time.Instant;

public record RecentCheckResponse(
        String siteName,
        String siteUrl,
        CheckStatus status,
        Integer httpStatus,
        Instant checkedAt
) {
    public static RecentCheckResponse from(CheckResult result) {
        return new RecentCheckResponse(result.getSite().getName(),
                result.getSite().getUrl(),result.getStatus(), result.getHttpStatus(), result.getCheckedAt());
    }
}
