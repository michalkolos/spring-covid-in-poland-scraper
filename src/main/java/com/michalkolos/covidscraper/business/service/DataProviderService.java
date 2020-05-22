package com.michalkolos.covidscraper.business.service;

import com.michalkolos.covidscraper.data.entity.VirusDataPoint;
import com.michalkolos.covidscraper.data.entity.Voivo;
import com.michalkolos.covidscraper.data.entity.WeatherDataPoint;
import com.michalkolos.covidscraper.data.repository.VirusDataPointRepository;
import com.michalkolos.covidscraper.data.repository.VoivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class DataProviderService {

	VirusDataPointRepository virusDataPointRepository;
	VoivoRepository voivoRepository;


	@Autowired
	public DataProviderService(
			VirusDataPointRepository virusDataPointRepository,
			VoivoRepository voivoRepository) {

		this.virusDataPointRepository = virusDataPointRepository;
		this.voivoRepository = voivoRepository;
	}


	public List<VirusDataPoint> getVirusRows(int rowNo){
		return virusDataPointRepository.findAll();
	}


	public List<Voivo> getDataRows(int rowNo) throws InvalidParameterException {

		if(rowNo < 1){
			throw new InvalidParameterException("Row number must be a positive integer");
		}

		List<Voivo> voivos = voivoRepository.findAll();

		for(Voivo voivo : voivos){
			List<VirusDataPoint> virusDataPoints = voivo.getVirusDataPoints();
			List<WeatherDataPoint> weatherDataPoints = voivo.getWeatherDataPoints();

			Collections.sort(virusDataPoints);
			Collections.reverse(virusDataPoints);
			Collections.sort(weatherDataPoints);
			Collections.reverse(weatherDataPoints);

			if(virusDataPoints.size() < rowNo){
				rowNo = virusDataPoints.size();
			}

			uncumulateData(virusDataPoints);
			virusDataPoints = virusDataPoints.subList(0, rowNo);

			voivo.setVirusDataPoints(virusDataPoints);

			LocalDateTime oldestDate = virusDataPoints.get(virusDataPoints.size() - 1).getDateTime();

			for(int i = 0; i < weatherDataPoints.size(); i++){
				if(weatherDataPoints.get(i).getGatheredTime().isBefore(oldestDate)){
					weatherDataPoints = weatherDataPoints.subList(0, i);
				}
			}

			voivo.setWeatherDataPoints(weatherDataPoints);
		}

		return voivos;
	}


	private void uncumulateData(List<VirusDataPoint> virusDataPoints){
		if(virusDataPoints.size() < 2) return;

		long previousCases;
		long previousDeaths;

		for(int i = 0; i < virusDataPoints.size() - 1; i++){
			previousCases = virusDataPoints.get(i + 1).getCases();
			previousDeaths = virusDataPoints.get(i + 1).getDeaths();

			long currentCases = virusDataPoints.get(i).getCases();
			currentCases = currentCases - previousCases;
			virusDataPoints.get(i).setCases(currentCases);

			long currentDeaths = virusDataPoints.get(i).getDeaths();
			currentDeaths = currentDeaths - previousDeaths;
			virusDataPoints.get(i).setDeaths(currentDeaths);


		}
	}
}
