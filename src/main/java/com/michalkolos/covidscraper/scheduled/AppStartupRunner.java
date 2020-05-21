package com.michalkolos.covidscraper.scheduled;

import com.michalkolos.covidscraper.data.entity.VirusDataPoint;
import com.michalkolos.covidscraper.data.entity.Voivo;
import com.michalkolos.covidscraper.data.entity.WeatherDataPoint;
import com.michalkolos.covidscraper.business.service.DataPersistenceService;
import com.michalkolos.covidscraper.business.service.VirusDataService;
import com.michalkolos.covidscraper.business.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AppStartupRunner implements ApplicationRunner {

	VirusDataService virusDataService;
	WeatherDataService weatherDataService;
	DataPersistenceService dataPersistenceService;

	@Autowired
	public AppStartupRunner(
			VirusDataService virusDataService,
			WeatherDataService weatherDataService,
			DataPersistenceService dataPersistenceService) {

		this.virusDataService = virusDataService;
		this.weatherDataService = weatherDataService;
		this.dataPersistenceService = dataPersistenceService;
	}







	@Override
	public void run(ApplicationArguments args) throws Exception {

//		virusScraperService.collectData();

		List<Voivo> voivos = virusDataService.collectVoivos();

		Map<String, VirusDataPoint> virusMap = virusDataService.collectData();
		virusDataService.printToLogAllVirusData(virusMap);

		Map<String, WeatherDataPoint> weatherMap = weatherDataService.collectData();
		weatherDataService.printToLogAllWeatherData(weatherMap);


		dataPersistenceService.syncVoivos(voivos);
		dataPersistenceService.saveVirusDataPoints(virusMap);
		dataPersistenceService.saveWeatherDataPoints(weatherMap);

	}
}
