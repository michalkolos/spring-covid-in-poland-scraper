package com.michalkolos.covidscraper.data.repository;

import com.michalkolos.covidscraper.data.entity.VirusDataPoint;
import com.michalkolos.covidscraper.data.entity.Voivo;
import com.michalkolos.covidscraper.data.entity.WeatherDataPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface WeatherDataPointRepository extends JpaRepository<WeatherDataPoint, Long> {

	boolean existsByGatheredTimeAndVoivo(LocalDateTime gatheredTime, Voivo voivo);

	WeatherDataPoint findAllByGatheredTimeBetweenAndVoivo(
			LocalDateTime gatheredTime, LocalDateTime gatheredTime2, Voivo voivo);

}

