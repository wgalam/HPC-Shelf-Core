package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.ufc.storm.exception.DBHandlerException;

public class ComponentKindHandler extends DBHandler {
	private final static String SELECT_KIND_ID = "select kind_id from component_kind where kind_name=?;";
	private final static String SELECT_KIND_NAME = "select kind_name from component_kind where kind_id=?;";
	private final static String INSERT_KIND = "INSERT INTO component_kind (kind_name, description) VALUES (?,?) RETURNING kind_id;";

	/**
	 * This method creates a new component kind
	 * @param name Name of kind
	 * @param description Description of kind
	 * @return This method returns the component id
	 */

	private static int addKind(String name, String description) {
		try {
			Connection con = DBHandler.getConnection();
			PreparedStatement prepared = con.prepareStatement(INSERT_KIND); 
			prepared.setString(1, name);
			prepared.setString(2, description);
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				return result.getInt("kind_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * This method gets a kind id
	 * @param kind_name Kind name
	 * @return Kind id
	 * @throws SQLException 
	 * @throws DBHandlerException 
	 */

	public static int getKindID(String kind_name) throws SQLException, DBHandlerException {
		Connection con = getConnection(); 
		int kind_id = 0;
		PreparedStatement prepared = con.prepareStatement(SELECT_KIND_ID); 
		prepared.setString(1, kind_name); 
		ResultSet resultSet = prepared.executeQuery(); 
		if(resultSet.next()) { 
			kind_id = resultSet.getInt("kind_id"); 
			return kind_id;
		}else{
			throw new DBHandlerException("Component kind not found");
		}
	}

	/**
	 * This method gets a kind name
	 * @param id Kind id
	 * @return Kind name
	 * @throws SQLException 
	 * @throws DBHandlerException 
	 */

	public static String getKindName(int id) throws SQLException, DBHandlerException {
		Connection con = getConnection();  
		PreparedStatement prepared = con.prepareStatement(SELECT_KIND_NAME); 
		prepared.setInt(1, id); 
		ResultSet resultSet = prepared.executeQuery(); 
		if(resultSet.next()) { 
			return resultSet.getString("kind_name"); 
		}else{
			throw new DBHandlerException("Component kind not found");
		}
	}
}
