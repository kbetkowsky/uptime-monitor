package com.kbetkowski.uptimemonitor.repository;

import com.kbetkowski.uptimemonitor.entity.CheckResult;
import com.kbetkowski.uptimemonitor.entity.CheckStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface CheckResultRepository extends JpaRepository<CheckResult, UUID> {

    @Query("select c from CheckResult c join fetch c.site order by c.checkedAt desc limit 50")
    List<CheckResult> findRecentWithSite();

    @Query("""
           select s.name as siteName,
                  s.url as siteUrl,
                  count(c) as totalChecks,
                  sum(case when c.status = :upStatus then 1 else 0 end) as upChecks
           from CheckResult c
           join c.site s
           where c.checkedAt >= :since
           group by s.id, s.name, s.url
           """)
    List<SiteUptimeProjection> uptimeStatsSince(@Param("since") Instant since,
                                                @Param("upStatus") CheckStatus upStatus);
}