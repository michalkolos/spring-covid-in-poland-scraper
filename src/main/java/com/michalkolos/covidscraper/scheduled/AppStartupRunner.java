package com.michalkolos.covidscraper.scheduled;

import com.michalkolos.covidscraper.data.entity.VirusDataPoint;
import com.michalkolos.covidscraper.data.entity.Voivo;
import com.michalkolos.covidscraper.data.entity.WeatherDataPoint;
import com.michalkolos.covidscraper.service.DataPersistenceService;
import com.michalkolos.covidscraper.service.VirusScraperService;
import com.michalkolos.covidscraper.service.WeatherGatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AppStartupRunner implements ApplicationRunner {

	VirusScraperService virusScraperService;
	WeatherGatherService weatherGatherService;
	DataPersistenceService dataPersistenceService;

	@Autowired
	public AppStartupRunner(
			VirusScraperService virusScraperService,
			WeatherGatherService weatherGatherService,
            DataPersistenceService dataPersistenceService) {

		this.virusScraperService = virusScraperService;
		this.weatherGatherService = weatherGatherService;
		this.dataPersistenceService = dataPersistenceService;
	}







	@Override
	public void run(ApplicationArguments args) throws Exception {

//		virusScraperService.collectData();

		List<Voivo> voivos = virusScraperService.collectVoivos();

		Map<String, VirusDataPoint> virusMap = virusScraperService.collectData();
		virusScraperService.printToLogAllVirusData(virusMap);

		Map<String, WeatherDataPoint> weatherMap = weatherGatherService.collectData();
		weatherGatherService.printToLogAllWeatherData(weatherMap);


		dataPersistenceService.syncVoivos(voivos);
		dataPersistenceService.saveVirusDataPoints(virusMap);
		dataPersistenceService.saveWeatherDataPoints(weatherMap);

	}
}
