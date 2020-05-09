package com.michalkolos.covidscraper.data.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "VIRUS_DATA")
public class VirusDataPoint {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "VIRUS_DATA_POINT_ID")
	private long id;

	@Column(name = "CASES")
	private long cases;

	@Column(name = "DEATHS")
	private long deaths;

	@Column(name = "DATE_TIME")
	LocalDateTime dateTime;



	@ManyToOne
	@JoinColumn
	private Voivo voivo;








	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCases() {
		return cases;
	}

	public void setCases(long cases) {
		this.cases = cases;
	}

	public long getDeaths() {
		return deaths;
	}

	public void setDeaths(long deaths) {
		this.deaths = deaths;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}


	public Voivo getVoivo() {
		return voivo;
	}

	public void setVoivo(Voivo voivo) {
		this.voivo = voivo;
	}
}
