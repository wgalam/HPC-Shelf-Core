package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.ufc.storm.jaxb.AbstractUnitType;

public class AbstractUnitHandler extends DBHandler {
	private static final String INSERT_ABSTRACT_UNIT = "INSERT INTO abstract_unit ( ac_id, au_name) VALUES ((select ac_id from abstract_component where ac_name = ?), ?) RETURNING abstract_unit_id;";
	private static final String SELECT_ABTRACT_UNIT_ID = "select abstract_unit_id from abstract_unit where au_name = ?;";
	private static final String SELECT_ABTRACT_UNIT = "SELECT * FROM abstract_unit  WHERE abstract_unit_id = ?;";

	/**
	 * 
	 * @param au_name
	 * @param au_name
	 * @return
	 */
	public static int addAbstractUnit(int ac_id, String au_name){
		Connection con = null; 
		int au_id = -1;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(INSERT_ABSTRACT_UNIT); 
			prepared.setInt(1, ac_id); 
			prepared.setString(2, au_name);  
			ResultSet result = prepared.executeQuery(); 
			if(result.next()){
				au_id = result.getInt("abstract_unit_id");
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
			au_id = -1;
		} finally { 
			closeConnnection(con); 
		} 
		return au_id;
	}
	
	
	public static AbstractUnitType getAbstractUnit(int auid){
		Connection con = null; 
		AbstractUnitType cut = null;
		try {
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_ABTRACT_UNIT); 
			prepared.setInt(1, auid); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				cut = new AbstractUnitType();
				cut.setAuName(resultSet.getString("au_name"));
				cut.setAcId(resultSet.getInt("ac_id"));
				cut.setAuId(auid);
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return cut;
	}

	/**
	 * 
	 * @param au_name
	 * @return
	 */
	public static int getAbstractUnitID(String au_name){
		Connection con = null; 
		int au_id = -2;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_ABTRACT_UNIT_ID); 
			prepared.setString(1, au_name); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				au_id = resultSet.getInt("abstract_unit_id"); 
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
			return -1;
		} finally { 
			closeConnnection(con); 
		} 
		return au_id;
	}


}
