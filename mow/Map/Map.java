import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

/**
 * @author Dave Mohrhardt
 * 
 * These are just some test methods I have been toying with.  The distanceFinder method is what is really important
 * here.
 */

public class Map<Latitude, Longitude> {
	
	public class Coordinate {
		private Latitude lat;
		private Longitude lon;
		
		public Coordinate() {}
		public Coordinate(Latitude l1, Longitude l2) {
			lat = l1;
			lon = l2;
		}
		
		public void setLat( Latitude latitude ) { lat = latitude; }
		public void setLon( Longitude longitude ) { lon = longitude; }
		
		public Latitude getLat() { return lat; }
		public Longitude getLon() { return lon; }
	}
	
	private final Coordinate home;		// Acts as the head of the list.
	private Coordinate dest;				// The destination for the drone.
	
	public Map() { home = null; }												// Null Map constructor
	
	public Map( Longitude lat, Latitude lon ) {
		home = new Coordinate(lon, lat);
		//path.push(home);
	}
	
	public boolean receivedDestination( Coordinate dest ) {
		if(dest != null) 
			return true;
		else 
			return false;
	}
	
	public void distanceFinder(String origin, String dest, String key, String mode) throws IOException {
		URL url = new URL("https://maps.googleapis.com/maps/api/dinstancematrix/json?origins=Vancouver+BC|Seattle&destinations=San+Francisco|Victoria+BC");
		HttpURLConnection connect = (HttpURLConnection) url.openConnection();
		String line, output = "";
		BufferedReader reader = new BufferedReader(
		new InputStreamReader( connect.getInputStream() )
		);
		
		while( (line = reader.readLine()) != null ) {
			output += line;
		}
		System.out.println(output);
	}
}