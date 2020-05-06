package com.michalkolos.covidscraper.data.repository;

import com.michalkolos.covidscraper.data.entity.VirusDataPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirusDataPointRepository extends JpaRepository<VirusDataPoint, Long> {

}
