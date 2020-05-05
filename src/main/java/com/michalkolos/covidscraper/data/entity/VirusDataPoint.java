package com.michalkolos.covidscraper.data.entity;

import javax.persistence.*;

@Entity
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

}
