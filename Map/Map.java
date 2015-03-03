import java.util.*;

public class Map {

	genericStack<Coordinate> path = new genericStack< Coordinate< Latitude, Longitude > >();
	
	public class Coordinate<Latitude, Longitude> {
		private Latitude lat;
		private Longitude lon;
		
		public Coordinate<Latitude, Longitude>() {}
		public Coordinate<Latitude, Longitude>(Latitude l1, Longitude l2) {
			lat = l1;
			lon = l2;
		}
		
		public void setLat( Latitude latitude ) { lat = latitude; }
		public void setLon( Longitude longitude ) { lon = longitude; }
		
		public Latitude getLat() { return lat; }
		public Longitude getLon() { return lon; }
	}
}
