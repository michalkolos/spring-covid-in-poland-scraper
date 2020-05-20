package com.michalkolos.covidscraper.service;

import com.michalkolos.covidscraper.data.entity.VirusDataPoint;
import com.michalkolos.covidscraper.data.entity.Voivo;
import com.michalkolos.covidscraper.data.repository.VirusDataPointRepository;
import com.michalkolos.covidscraper.data.repository.VoivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
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
			List<VirusDataPoint> dataPoints = voivo.getVirusDataPoints();

			Collections.sort(dataPoints);

			if(dataPoints.size() < rowNo){
				rowNo = dataPoints.size();
			}

			dataPoints = dataPoints.subList(0, rowNo);

			voivo.setVirusDataPoints(dataPoints);
		}

		return voivos;
	}
}
