package com.kbetkowski.uptimemonitor.repository;

import com.kbetkowski.uptimemonitor.entity.CheckResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CheckResultRepository extends JpaRepository<CheckResult, UUID> {
}
