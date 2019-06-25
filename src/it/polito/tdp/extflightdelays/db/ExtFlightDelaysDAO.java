package it.polito.tdp.extflightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.extflightdelays.model.Airline;
import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Flight;
import it.polito.tdp.extflightdelays.model.Rotta;

public class ExtFlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		String sql = "SELECT * from airlines";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRLINE")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> loadAllAirports() {
		String sql = "SELECT * FROM airports";
		List<Airport> result = new ArrayList<Airport>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport = new Airport(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRPORT"),
						rs.getString("CITY"), rs.getString("STATE"), rs.getString("COUNTRY"), rs.getDouble("LATITUDE"),
						rs.getDouble("LONGITUDE"), rs.getDouble("TIMEZONE_OFFSET"));
				result.add(airport);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights() {
		String sql = "SELECT * FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight flight = new Flight(rs.getInt("ID"), rs.getInt("AIRLINE_ID"), rs.getInt("FLIGHT_NUMBER"),
						rs.getString("TAIL_NUMBER"), rs.getInt("ORIGIN_AIRPORT_ID"),
						rs.getInt("DESTINATION_AIRPORT_ID"),
						rs.getTimestamp("SCHEDULED_DEPARTURE_DATE").toLocalDateTime(), rs.getDouble("DEPARTURE_DELAY"),
						rs.getDouble("ELAPSED_TIME"), rs.getInt("DISTANCE"),
						rs.getTimestamp("ARRIVAL_DATE").toLocalDateTime(), rs.getDouble("ARRIVAL_DELAY"));
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Airport> getAirports(Integer voliMin, Map<Integer,Airport> aIdMap){
		String sql = "select a.ID, a.IATA_CODE, a.AIRPORT, a.CITY, a.STATE, a.COUNTRY, a.LATITUDE, a.LONGITUDE, a.TIMEZONE_OFFSET " + 
				"from flights f, airports a " + 
				"where f.origin_airport_id = a.id or f.destination_airport_id = a.id " + 
				"group by a.id " + 
				"having count(f.id)>= ? ";
		List<Airport> result = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, voliMin);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Airport airport = new Airport(rs.getInt("a.ID"), rs.getString("a.IATA_CODE"), rs.getString("a.AIRPORT"),
						rs.getString("a.CITY"), rs.getString("a.STATE"), rs.getString("a.COUNTRY"), rs.getDouble("a.LATITUDE"),
						rs.getDouble("a.LONGITUDE"), rs.getDouble("a.TIMEZONE_OFFSET"));
				
				aIdMap.put(airport.getId(), airport);
				result.add(airport);
			}

			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	public List<Rotta> getRotte(Map<Integer,Airport> aIdMap){
		String sql="select f.origin_airport_id as id1, f.destination_airport_id as id2, Avg(f.ELAPSED_TIME) as peso " + 
				"from flights f " + 
				"group by f.origin_airport_id, f.destination_airport_id ";
		
		List<Rotta> result = new ArrayList<Rotta>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport partenza = aIdMap.get(rs.getInt("id1"));
				Airport destinazione = aIdMap.get(rs.getInt("id2"));
				
				if(partenza!=null && destinazione!=null) {
				
					Rotta rotta= new Rotta(partenza, destinazione, rs.getDouble("peso"));
					result.add(rotta);
				}
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
}
