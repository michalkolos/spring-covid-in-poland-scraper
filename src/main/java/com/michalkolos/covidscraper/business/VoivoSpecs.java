package com.michalkolos.covidscraper.business;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VoivoSpecs {


	private static final Map<String, String[][]> VOIVO_COORDS = new HashMap<>() {
		{
//					                    lon         lat
			put("t00", new String[][]{{"19.47997", "52.06898"}, {"Cała Polska"}});
			put("t02", new String[][]{{"16.41069", "51.08950"}, {"dolnośląskie"}});
			put("t04", new String[][]{{"18.48822", "53.07270"}, {"kujawsko-pomorskie"}});
			put("t06", new String[][]{{"22.90027", "51.22072"}, {"lubelskie"}});
			put("t08", new String[][]{{"15.34275", "52.19617"}, {"lubuskie"}});
			put("t10", new String[][]{{"19.41760", "51.60487"}, {"łódzkie"}});
			put("t12", new String[][]{{"20.26933", "49.85895"}, {"małopolskie"}});
			put("t14", new String[][]{{"21.09645", "52.34576"}, {"mazowieckie"}});
			put("t16", new String[][]{{"17.89988", "50.64711"}, {"opolskie"}});
			put("t18", new String[][]{{"22.16912", "49.95367"}, {"podkarpackie"}});
			put("t20", new String[][]{{"22.92931", "53.26452"}, {"podlaskie"}});
			put("t22", new String[][]{{"17.98619", "54.15424"}, {"pomorskie"}});
			put("t24", new String[][]{{"18.99410", "50.33108"}, {"śląskie"}});
			put("t26", new String[][]{{"20.76909", "50.76339"}, {"świętokrzyskie"}});
			put("t28", new String[][]{{"20.82493", "53.85721"}, {"warmińsko-mazurskie"}});
			put("t30", new String[][]{{"17.24310", "52.33078"}, {"wielkopolskie"}});
			put("t32", new String[][]{{"15.54329", "53.58476"}, {"zachodniopomorskie"}});
		}
	};


	public static boolean exists(String id){
		return VOIVO_COORDS.containsKey(id);
	}


	public static String getName(String id){
		String[][] voivo = VOIVO_COORDS.get(id);
		return voivo[voivo.length - 1][0];
	}

	public static String getLon(String id, int no){
		String[][] voivo = VOIVO_COORDS.get(id);
		return voivo[0][0];
	}

	public static String getLat(String id, int no){
		String[][] voivo = VOIVO_COORDS.get(id);
		return voivo[0][1];
	}

	public static int Size(){
		return VOIVO_COORDS.size();
	}

	public static Set<String> getKeys(){
		return VOIVO_COORDS.keySet();
	}
}
