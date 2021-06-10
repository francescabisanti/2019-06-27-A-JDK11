package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> listAllCategorie(){
		String sql = "SELECT DISTINCT e.offense_category_id AS c "
				+ "FROM EVENTS e  "
				+ "ORDER BY c ASC " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getString("c"));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> listAllAnni(){
		String sql = "SELECT DISTINCT YEAR(e.reported_date) AS c "
				+ "FROM EVENTS e "
				+ "ORDER BY c ASC" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getInt("c"));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	
	public List<String> listAllVertici(String categoria, Integer anno){
		String sql = "SELECT DISTINCT e.offense_type_id AS c "
				+ "FROM EVENTS e "
				+ "WHERE YEAR(e.reported_date)=? AND e.offense_category_id=? "
				+ "" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setString(2, categoria);
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getString("c"));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Adiacenza> listAllAdiacenze(String categoria, Integer anno){
		String sql = "SELECT DISTINCT e1.offense_type_id AS t1, e2.offense_type_id AS t2, COUNT(DISTINCT(e1.district_id)) AS peso "
				+ "FROM EVENTS e1, EVENTS e2 "
				+ "WHERE YEAR(e1.reported_date)=? AND YEAR(e1.reported_date)=YEAR(e2.reported_date) AND  e1.offense_category_id=? "
				+ "AND e1.offense_category_id=e2.offense_category_id AND e1.offense_type_id> e2.offense_type_id "
				+ " AND e1.district_id =e2.district_id "
				+ "GROUP BY e1.offense_type_id, e2.offense_type_id "
				+ "HAVING peso>0 "
				+ " ORDER BY peso DESC ";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setString(2, categoria);
			List<Adiacenza> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				String tipo1= res.getString("t1");
				String tipo2= res.getString("t2");
				Double peso= res.getDouble("peso");
				Adiacenza a = new Adiacenza (tipo1,tipo2,peso);
				list.add(a);
				
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
}
