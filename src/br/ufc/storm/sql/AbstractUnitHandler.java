package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.jaxb.AbstractUnitType;

public class AbstractUnitHandler extends DBHandler {
	private static final String INSERT_ABSTRACT_UNIT = "INSERT INTO abstract_unit ( ac_id, au_name) VALUES ((select ac_id from abstract_component where ac_name = ?), ?) RETURNING abstract_unit_id;";
	private static final String SELECT_ABTRACT_UNIT_ID = "select abstract_unit_id from abstract_unit where au_name = ?;";
	private static final String SELECT_ABTRACT_UNIT = "SELECT * FROM abstract_unit  WHERE abstract_unit_id = ?;";
	private static final String SELECT_ALL_ABTRACT_UNIT = "SELECT * FROM abstract_unit WHERE ac_id = ?;";

	/**
	 * This method add a new abstract unit
	 * @param ac_id Abstract component id
	 * @param au_name Abstract unit name
	 * @return Abstract unit id
	 * @throws DBHandlerException 
	 * @throws SQLException 
	 */
	public static int addAbstractUnit(int ac_id, String au_name) throws DBHandlerException, SQLException {
		Connection con = getConnection();
		int au_id = 0;
		PreparedStatement prepared = con.prepareStatement(INSERT_ABSTRACT_UNIT); 
		prepared.setInt(1, ac_id); 
		prepared.setString(2, au_name);  
		ResultSet result = prepared.executeQuery(); 
		if(result.next()){
			au_id = result.getInt("abstract_unit_id");
			return au_id;
		}else{
			throw new DBHandlerException("Abstract unit can't be added: ");	
		}
	}

	/**
	 * 
	 * @param auid
	 * @return
	 * @throws SQLException
	 * @throws DBHandlerException
	 */

	public static AbstractUnitType getAbstractUnit(int auid) throws SQLException, DBHandlerException{
		Connection con = getConnection(); 
		AbstractUnitType cut = null; 
		PreparedStatement prepared = con.prepareStatement(SELECT_ABTRACT_UNIT); 
		prepared.setInt(1, auid); 
		ResultSet resultSet = prepared.executeQuery(); 
		if(resultSet.next()) { 
			cut = new AbstractUnitType();
			cut.setAuName(resultSet.getString("au_name"));
			cut.setAcId(resultSet.getInt("ac_id"));
			cut.setAuId(auid);
		}else{
			throw new DBHandlerException("Abstract unit not found");
		}
		return cut;
	}

	/**
	 * 
	 * @param au_name
	 * @return
	 * @throws SQLException 
	 * @throws DBHandlerException 
	 */
	public static int getAbstractUnitID(String au_name) throws SQLException, DBHandlerException{
		Connection con = getConnection();  
		PreparedStatement prepared = con.prepareStatement(SELECT_ABTRACT_UNIT_ID); 
		prepared.setString(1, au_name); 
		ResultSet resultSet = prepared.executeQuery(); 
		if(resultSet.next()) { 
			return resultSet.getInt("abstract_unit_id");
		}else{
			throw new DBHandlerException("Abstract unit not found");
		}

	}

	/**
	 * 
	 * @param ac_id
	 * @return
	 * @throws SQLException
	 */
	public static List<AbstractUnitType> getAbstractUnits(int ac_id) throws SQLException {
		Connection con = getConnection();
		ArrayList<AbstractUnitType> auts = new ArrayList<AbstractUnitType>();
		PreparedStatement prepared = con.prepareStatement(SELECT_ALL_ABTRACT_UNIT);
		prepared.setInt(1, ac_id);
		ResultSet resultSet = prepared.executeQuery();
		while (resultSet.next()) {
			AbstractUnitType aut = new AbstractUnitType();
			aut.setAuName(resultSet.getString("au_name"));
			aut.setAuId(resultSet.getInt("abstract_unit_id"));
			auts.add(aut);
		} 
		return auts; 
	}
}
