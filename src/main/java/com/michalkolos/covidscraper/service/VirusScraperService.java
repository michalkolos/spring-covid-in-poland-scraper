package com.michalkolos.covidscraper.service;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Service
public class VirusScraperService {


	public static final String CSV_SPLIT_CHAR = ";";
	public static final String VIRUS_SOURCE_URL = "https://www.gov.pl/web/koronawirus/wykaz-zarazen-koronawirusem-sars-cov-2";
	public static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm";
	public static final int VOIVO_CSV_COLUMN = 0;
	public static final int CASES_CSV_COLUMN = 1;
	public static final int DEATHS_CSV_COLUMN = 2;
	public static final int SOURCE_ID_CSV_COLUMN = 3;

	SnapshotService snapshotService;

	@Autowired
	public VirusScraperService(SnapshotService snapshotService) {
		this.snapshotService = snapshotService;
	}

	public void collectData(){


		String dataString = null;

		try {
			// Here we create a document object and use JSoup to fetch the website
			Document mzDoc = Jsoup.connect(VIRUS_SOURCE_URL).get();


			// With the document fetched, we use JSoup's title() method to fetch the title
//			System.out.printf("Title: %s\n", mzDoc.title());

			Element rawData = mzDoc.getElementById("registerData");

			dataString = mzDoc.text();

			// In case of any IO errors, we want the messages written to the console
		} catch (IOException e) {
			e.printStackTrace();
		}

		//TODO: Read-up on assertions
		assert dataString != null;
		int dataStringStart = dataString.indexOf("{\"description\"");

		dataString = dataString.substring(dataStringStart);

		JSONObject obj = new JSONObject(dataString);



		String timeString = obj.getString("description");

		System.out.println("Description: \n" + timeString + "\n");

		int timeStringStart = timeString.indexOf("na : ") + 5;
		timeString = timeString.substring(timeStringStart);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
		snapshotService.createSnapshot(LocalDateTime.parse(timeString, formatter));

		dataString = obj.getString("data");

		System.out.print("Data: \n" + dataString + "\n");

		Scanner scanner = new Scanner(dataString);
		scanner.nextLine();
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] values = line.split(CSV_SPLIT_CHAR);

			snapshotService.addVirusDataPoint(values[VOIVO_CSV_COLUMN],
					Long.parseLong(values[CASES_CSV_COLUMN].replaceAll(" ", "")),
					Long.parseLong(values[DEATHS_CSV_COLUMN].replaceAll(" ", "")),
					values[SOURCE_ID_CSV_COLUMN]);

		}
		scanner.close();

		snapshotService.printSnapshot();
		snapshotService.saveSnapshot();

	}



	public void runTest(){
		System.out.println("Hello from scraper service!");
	}



}
