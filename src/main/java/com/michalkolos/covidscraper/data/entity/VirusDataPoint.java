package com.michalkolos.covidscraper.data.entity;

import javax.persistence.*;

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

	@ManyToOne
	@JoinColumn
	private Snapshot snapshot;

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

	public Snapshot getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(Snapshot snapshot) {
		this.snapshot = snapshot;
	}

	public Voivo getVoivo() {
		return voivo;
	}

	public void setVoivo(Voivo voivo) {
		this.voivo = voivo;
	}
}
