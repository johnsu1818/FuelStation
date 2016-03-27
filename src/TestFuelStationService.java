import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

// This class needs java-json.jar and junit-4.12.jar and other Java libs
public class TestFuelStationService {
	
	//private static Integer stationId = null;

	// Verify "HYATT AUSTIN" station exists in response, and save the station ID
	@Test
	public void verifyStationAndAddress() throws JSONException, IOException
	{
		Integer stationId = null;
		
		// build the URL string to get all stations
		String s1 = "https://api.data.gov/nrel/alt-fuel-stations/v1.json?zip=78704&ev_network=ChargePoint%20Network&api_key=NXXzhtNcG5GIapiLwT4ikuNO74lrSUOT1ZMdFK7C";
		//String s = "https://api.data.gov/nrel/alt-fuel-stations/v1.json?api_key=NXXzhtNcG5GIapiLwT4ikuNO74lrSUOT1ZMdFK7C";
		
		// get a JSONObject contains the JSON response message
		JSONObject obj1 = getJsonObject(s1);
		
		JSONArray arr = obj1.getJSONArray("fuel_stations");
		for (int i = 0; i < arr.length(); i++)
		{
			if (arr.getJSONObject(i).getString("station_name").contains("HYATT AUSTIN"))
			{
				System.out.println(arr.getJSONObject(i).getString("station_name"));
				stationId = arr.getJSONObject(i).getInt("id");
				System.out.println(stationId);
				Assert.assertTrue("Station ID is null", stationId != null);
				break;
			}
		}
		
		// build the URL string to get station address by ID
		String s2 =  String.format("https://api.data.gov/nrel/alt-fuel-stations/v1/%s.json?api_key=NXXzhtNcG5GIapiLwT4ikuNO74lrSUOT1ZMdFK7C", stationId);
		
		// get a JSONObject contains the JSON response message
		JSONObject obj2 = getJsonObject(s2);
		String stationAddress = obj2.getJSONObject("alt_fuel_station").getString("street_address");
		System.out.println(stationAddress);
		Assert.assertTrue("", stationAddress.contains("208 Barton Springs Rd"));
	}

	public JSONObject getJsonObject( String s) throws IOException, JSONException
	{
		URL url = new URL(s);
		
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		
		if (conn.getResponseCode() != 200) 
		{
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}	
		
		// read from the URL
		Scanner scan = new Scanner(url.openStream());
		String str = new String();
		while (scan.hasNext())
			str += scan.nextLine();
		scan.close();
		conn.disconnect();
		
		// build a JSON object
		JSONObject obj = new JSONObject(str);
		return obj;
	}
	
	

}
