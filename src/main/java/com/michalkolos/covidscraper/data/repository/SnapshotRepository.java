package com.michalkolos.covidscraper.data.repository;

import com.michalkolos.covidscraper.data.entity.Snapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {

	public Snapshot findByDateTime(LocalDateTime dateTime);
	public boolean existsSnapshotByDateTime(LocalDateTime dateTime);
}
