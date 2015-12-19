package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComponentKindHandler extends DBHandler {
	private final static String SELECT_KIND_ID = "select kind_id from component_kind where kind_name=?;";
	private final static String SELECT_KIND_NAME = "select kind_name from component_kind where kind_id=?;";
	

	/**
	 * This method gets a kind id
	 * @param kind Kind name
	 * @return Kind id
	 */

	public static int getKindID(String kind) {
		Connection con = null; 
		int kind_id = -2;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_KIND_ID); 
			prepared.setString(1, kind); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				kind_id = resultSet.getInt("kind_id"); 
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
			closeConnnection(con);
			return -1;
		} finally { 
			closeConnnection(con); 
		} 
		return kind_id;
	}


	/**
	 * This method gets a kind name
	 * @param id Kind id
	 * @return Kind name
	 */

	public static String getKindName(int id) {
		Connection con = null; 
		String name = null;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_KIND_NAME); 
			prepared.setInt(1, id); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				name = resultSet.getString("kind_name"); 
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return name;
	}


}
