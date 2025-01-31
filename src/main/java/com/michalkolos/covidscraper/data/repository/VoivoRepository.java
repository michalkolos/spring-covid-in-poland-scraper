package com.michalkolos.covidscraper.data.repository;

import com.michalkolos.covidscraper.data.entity.Voivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoivoRepository extends JpaRepository<Voivo, Long> {
	public Voivo findBySourceId(String sourceId);
	public boolean existsBySourceId(String sourceId);
	public List<Voivo> findAll();
}
