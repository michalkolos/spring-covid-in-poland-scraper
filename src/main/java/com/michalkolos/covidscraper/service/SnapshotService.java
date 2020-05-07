package com.michalkolos.covidscraper.service;

import com.michalkolos.covidscraper.data.entity.Snapshot;
import com.michalkolos.covidscraper.data.entity.VirusDataPoint;
import com.michalkolos.covidscraper.data.entity.Voivo;
import com.michalkolos.covidscraper.data.repository.SnapshotRepository;
import com.michalkolos.covidscraper.data.repository.VirusDataPointRepository;
import com.michalkolos.covidscraper.data.repository.VoivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class SnapshotService {

	private SnapshotRepository snapshotRepository;
	private VirusDataPointRepository virusDataPointRepository;
	private VoivoRepository voivoRepository;


	private Snapshot snapshot;

	@Autowired
	public SnapshotService(SnapshotRepository snapshotRepository,
	                       VirusDataPointRepository virusDataPointRepository,
	                       VoivoRepository voivoRepository) {

		this.snapshotRepository = snapshotRepository;
		this.virusDataPointRepository = virusDataPointRepository;
		this.voivoRepository = voivoRepository;
	}

	public void createSnapshot(LocalDateTime dateTime) {
		snapshot = new Snapshot();
		snapshot.setDateTime(dateTime);
		snapshot.setVirusDataPoints(new HashSet<>());
	}

	public void addVirusDataPoint(String voioName, long cases, long deaths, String sourceId) {
		Set<VirusDataPoint> dataPoints = snapshot.getVirusDataPoints();

		AtomicBoolean dataPointExists = new AtomicBoolean(false);

		dataPoints.forEach(dataPoint -> {
			if (dataPoint.getVoivo().getSourceId().equals(sourceId)) {
				dataPointExists.set(true);
			}
		});

		if (!dataPointExists.get()) {
			VirusDataPoint virusDataPoint = new VirusDataPoint();
			virusDataPoint.setCases(cases);
			virusDataPoint.setDeaths(deaths);
			virusDataPoint.setSnapshot(snapshot);

			Voivo voivo = new Voivo();
			voivo.setName(voioName);
			voivo.setSourceId(sourceId);
			voivo.setVirusDataPoints(new HashSet<>());
//			voivo.getVirusDataPoints().add(virusDataPoint);

			virusDataPoint.setVoivo(voivo);

			snapshot.getVirusDataPoints().add(virusDataPoint);
		}

	}

	public void printSnapshot() {

		System.out.println(snapshot.getDateTime().toString());

		StringBuilder sb = new StringBuilder();

		for (VirusDataPoint virusDataPoint : snapshot.getVirusDataPoints()) {
			sb.append(String.format("%20s", virusDataPoint.getVoivo().getName()));
			sb.append(":");
			sb.append("   cases: ");
			sb.append(String.format("%10s", virusDataPoint.getCases()));
			sb.append("   deaths: ");
			sb.append(String.format("%10s", virusDataPoint.getDeaths()));
			sb.append("   ID: ");
			sb.append(virusDataPoint.getVoivo().getSourceId());
			sb.append(System.lineSeparator());
		}

		System.out.println(sb.toString() + System.lineSeparator());
	}

	@Transactional
	public void saveSnapshot() {
		if (isSnapshotNew()) {

			snapshotRepository.save(snapshot);

			snapshot.getVirusDataPoints().forEach((VirusDataPoint dataPoint)->{
				syncVoivo(dataPoint);
				virusDataPointRepository.save(dataPoint);
			});
		}
	}

	private boolean isSnapshotNew() {
		return snapshot != null &&
				!snapshotRepository.existsSnapshotByDateTime(snapshot.getDateTime());
	}

	private void syncVoivo(VirusDataPoint dataPoint) {
		String sourceId = dataPoint.getVoivo().getSourceId();

		if (voivoRepository.existsBySourceId(sourceId)) {
			dataPoint.setVoivo(voivoRepository.findBySourceId(sourceId));
		}else{
			voivoRepository.save(dataPoint.getVoivo());
		}
	}
}
