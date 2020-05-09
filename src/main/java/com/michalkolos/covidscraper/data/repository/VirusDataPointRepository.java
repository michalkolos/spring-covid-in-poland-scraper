package com.michalkolos.covidscraper.data.repository;

import com.michalkolos.covidscraper.data.entity.VirusDataPoint;
import com.michalkolos.covidscraper.data.entity.Voivo;
import com.michalkolos.covidscraper.data.entity.WeatherDataPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface VirusDataPointRepository extends JpaRepository<VirusDataPoint, Long> {
	boolean existsByDateTimeAndVoivo(LocalDateTime dateTime, Voivo voivo);

	VirusDataPoint findAllByDateTimeBetweenAndVoivo(
			LocalDateTime dateTime, LocalDateTime dateTime2, Voivo voivo);

}
