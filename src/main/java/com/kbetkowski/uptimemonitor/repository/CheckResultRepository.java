package com.kbetkowski.uptimemonitor.repository;

import com.kbetkowski.uptimemonitor.entity.CheckResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CheckResultRepository extends JpaRepository<CheckResult, UUID> {
    @Query("select c from CheckResult c join fetch c.site order by c.checkedAt desc limit 50")
    List<CheckResult> findRecentWithSite();
}
