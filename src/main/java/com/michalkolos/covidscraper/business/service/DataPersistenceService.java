package com.michalkolos.covidscraper.business.service;

import com.michalkolos.covidscraper.data.entity.VirusDataPoint;
import com.michalkolos.covidscraper.data.entity.Voivo;
import com.michalkolos.covidscraper.data.entity.WeatherDataPoint;
import com.michalkolos.covidscraper.data.repository.VirusDataPointRepository;
import com.michalkolos.covidscraper.data.repository.VoivoRepository;
import com.michalkolos.covidscraper.data.repository.WeatherDataPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;


@Service
public class DataPersistenceService {

	VoivoRepository voivoRepository;
	VirusDataPointRepository virusDataPointRepository;
	WeatherDataPointRepository weatherDataPointRepository;

	@Autowired
	public DataPersistenceService(
			VoivoRepository voivoRepository,
			VirusDataPointRepository virusDataPointRepository,
			WeatherDataPointRepository weatherDataPointRepository) {

		this.voivoRepository = voivoRepository;
		this.virusDataPointRepository = virusDataPointRepository;
		this.weatherDataPointRepository = weatherDataPointRepository;
	}


	@Transactional
	public void syncVoivos(List<Voivo> voivos){


		for(int i = 0; i < voivos.size(); i++){
			if(voivoRepository.existsBySourceId(voivos.get(i).getSourceId())){
				Voivo existingVoivo = voivoRepository.findBySourceId(voivos.get(i).getSourceId());
				voivos.set(i, existingVoivo);
			}else{
				voivoRepository.save(voivos.get(i));
			}
		}
	}


	@Transactional
	public void saveVirusDataPoints(Map<String, VirusDataPoint> virusMap){

		virusMap.forEach((String voivoCode, VirusDataPoint dataPoint)->{
			Voivo voivo = voivoRepository.findBySourceId(voivoCode);
			dataPoint.setVoivo(voivo);

			if(!virusDataPointRepository.existsByDateTimeAndVoivo(
					dataPoint.getDateTime(), dataPoint.getVoivo())){

				virusDataPointRepository.save(dataPoint);
			}
		});

	}


	@Transactional
	public void saveWeatherDataPoints(Map<String, WeatherDataPoint> weatherMap){

		weatherMap.forEach((String voivoCode, WeatherDataPoint dataPoint)->{
			Voivo voivo = voivoRepository.findBySourceId(voivoCode);
			dataPoint.setVoivo(voivo);

			if (!weatherDataPointRepository.existsByGatheredTimeAndVoivo(
					dataPoint.getGatheredTime(), dataPoint.getVoivo())) {

				weatherDataPointRepository.save(dataPoint);
			}
		});
	}
}
