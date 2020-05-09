package com.michalkolos.covidscraper.service;

import com.michalkolos.covidscraper.data.entity.VirusDataPoint;
import com.michalkolos.covidscraper.data.entity.Voivo;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class VirusScraperService {


	public static final String CSV_SPLIT_CHAR = ";";
	public static final String VIRUS_SOURCE_URL = "https://www.gov.pl/web/koronawirus/wykaz-zarazen-koronawirusem-sars-cov-2";
	public static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm";
	public static final int VOIVO_CSV_COLUMN = 0;
	public static final int CASES_CSV_COLUMN = 1;
	public static final int DEATHS_CSV_COLUMN = 2;
	public static final int SOURCE_ID_CSV_COLUMN = 3;

	private static final Map<String, String[][]> VOIVO_COORDS = new HashMap<>();

	static {
//							                        lon         lat
		VOIVO_COORDS.put("t00", new String[][]{{"19.47997", "52.06898"}, {"Cała Polska"}});
		VOIVO_COORDS.put("t02", new String[][]{{"16.41069", "51.08950"}, {"dolnośląskie"}});
		VOIVO_COORDS.put("t04", new String[][]{{"18.48822", "53.07270"}, {"kujawsko-pomorskie"}});
		VOIVO_COORDS.put("t06", new String[][]{{"22.90027", "51.22072"}, {"lubelskie"}});
		VOIVO_COORDS.put("t08", new String[][]{{"15.34275", "52.19617"}, {"lubuskie"}});
		VOIVO_COORDS.put("t10", new String[][]{{"19.41760", "51.60487"}, {"łódzkie"}});
		VOIVO_COORDS.put("t12", new String[][]{{"20.26933", "49.85895"}, {"małopolskie"}});
		VOIVO_COORDS.put("t14", new String[][]{{"21.09645", "52.34576"}, {"mazowieckie"}});
		VOIVO_COORDS.put("t16", new String[][]{{"17.89988", "50.64711"}, {"opolskie"}});
		VOIVO_COORDS.put("t18", new String[][]{{"22.16912", "49.95367"}, {"podkarpackie"}});
		VOIVO_COORDS.put("t20", new String[][]{{"22.92931", "53.26452"}, {"podlaskie"}});
		VOIVO_COORDS.put("t22", new String[][]{{"17.98619", "54.15424"}, {"pomorskie"}});
		VOIVO_COORDS.put("t24", new String[][]{{"18.99410", "50.33108"}, {"śląskie"}});
		VOIVO_COORDS.put("t26", new String[][]{{"20.76909", "50.76339"}, {"świętokrzyskie"}});
		VOIVO_COORDS.put("t28", new String[][]{{"20.82493", "53.85721"}, {"warmińsko-mazurskie"}});
		VOIVO_COORDS.put("t30", new String[][]{{"17.24310", "52.33078"}, {"wielkopolskie"}});
		VOIVO_COORDS.put("t32", new String[][]{{"15.54329", "53.58476"}, {"zachodniopomorskie"}});
	}

	private static final Logger log = LoggerFactory.getLogger(WeatherGatherService.class);





	public Map<String, VirusDataPoint> collectData(){

		Map<String, VirusDataPoint> virusMap = new HashMap<>();

		try {

			//TODO: Read-up on assertions
			String dataString = downloadRawDataString();
			LocalDateTime date = getDateFromRawDataString(dataString);
			List<List<String>> parsedData = parseRawDataToStringArray(dataString);

			parsedData.forEach((line) -> {
				VirusDataPoint virusDataPoint = new VirusDataPoint();

				virusDataPoint.setCases(Long.parseLong(line.get(CASES_CSV_COLUMN)
						.replaceAll(" ", "")));

				virusDataPoint.setDeaths(Long.parseLong(line.get(DEATHS_CSV_COLUMN)
						.replaceAll(" ", "")));

				virusDataPoint.setDateTime(date);

				virusMap.put(line.get(SOURCE_ID_CSV_COLUMN), virusDataPoint);
			});

		}catch (Exception e){
			log.error("Error getting virus data ({})", e.getMessage());
		}

		return virusMap;
	}





	public List<Voivo> collectVoivos(){

		List<Voivo> voivoSet = new ArrayList<>();

		try{
			String dataString = downloadRawDataString();
			List<List<String>> parsedData = parseRawDataToStringArray(dataString);

			parsedData.forEach((line) -> {
				Voivo voivo = new Voivo();

				voivo.setSourceId(line.get(SOURCE_ID_CSV_COLUMN));
				voivo.setName(line.get(VOIVO_CSV_COLUMN));

				voivoSet.add(voivo);
			});

		}catch (Exception e){
			log.error("Error getting Regions ({})", e.getMessage());
		}

		return voivoSet;
	}




	public void printToLogAllVirusData(Map<String, VirusDataPoint> virusMap){

		StringBuilder sb = new StringBuilder();

		sb.append("Virus data for all regions:").append(System.lineSeparator());

		virusMap.forEach((voivoCode, virusDataPoint)-> {
			if(VOIVO_COORDS.containsKey(voivoCode)){
				String[][] coords = VOIVO_COORDS.get(voivoCode);
				sb.append(String.format("%20s", coords[coords.length - 1][0]));
			}else{
				sb.append("Unknown code: ");
				sb.append(String.format("%6s", voivoCode));
			}

			sb.append(" - ");

			if(virusDataPoint != null) {
				sb.append(String.format("%10s", virusDataPoint.getDateTime()));
				sb.append(" Cases: ");
				sb.append(String.format("%10s", virusDataPoint.getCases()));
				sb.append(" Deaths: ");
				sb.append(String.format("%10s", virusDataPoint.getDeaths()));
			}else{
				sb.append(" No virus data");
			}
			sb.append(System.lineSeparator());
		});

		log.info(sb.toString());
	}




	private List<List<String>> parseRawDataToStringArray(String dataString) throws JSONException{

		JSONObject obj = new JSONObject(dataString);
		dataString = obj.getString("data");
		Scanner scanner = new Scanner(dataString);

		List<List<String>> parsedData = new ArrayList<List<String>>();
		scanner.nextLine();
		while (scanner.hasNextLine()) {
			List<String> parsedLine = new ArrayList<>();

			String line = scanner.nextLine();
			String[] values = line.split(CSV_SPLIT_CHAR);
			parsedData.add(Arrays.asList(values));
		}

		scanner.close();

		return parsedData;
	}




	private LocalDateTime getDateFromRawDataString(String dataString) throws JSONException {
		JSONObject obj = new JSONObject(dataString);

		String timeString = obj.getString("description");

		int timeStringStart = timeString.indexOf("na : ") + 5;
		timeString = timeString.substring(timeStringStart);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

		return LocalDateTime.parse(timeString, formatter);
	}




	private String downloadRawDataString() throws IOException{

		Document mzDoc = Jsoup.connect(VIRUS_SOURCE_URL).get();
		Element rawData = mzDoc.getElementById("registerData");
		String dataString = mzDoc.text();

		int dataStringStart = dataString.indexOf("{\"description\"");
		dataString = dataString.substring(dataStringStart);

		return dataString;
	}

}
