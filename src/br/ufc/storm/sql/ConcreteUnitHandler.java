package br.ufc.storm.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.jaxb.ConcreteUnitType;
import br.ufc.storm.jaxb.UnitFileType;
import br.ufc.storm.properties.PropertiesHandler;

public class ConcreteUnitHandler extends DBHandler {
	private static final String INSERT_CONCRETE_UNIT = "INSERT INTO concrete_unit ( cc_id, au_id) VALUES (?, ?) RETURNING unit_id; ";
	private static final String SELECT_CONCRETE_UNIT = "SELECT * FROM concrete_unit  WHERE unit_id = ?;";
	private static final String SELECT_CONCRETE_UNIT_BY_CCID = "SELECT * FROM concrete_unit  WHERE cc_id = ?;";
	private static final String SELECT_CONCRETE_UNIT_ID = "select unit_id from concrete_unit where au_id = (SELECT abstract_unit_id from abstract_unit where au_name = ?);";
	private static final String INSERT_UNIT_FILE = "INSERT INTO files (file_name, u_id, file_extension, current_version, build_cfg, file_type, path) VALUES (?, ?, ?, ?, ?,?,?);";
	private static final String SELECT_UNIT_PATH = "SELECT * FROM concrete_unit A, context_contract B WHERE A.unit_id = ? AND B.cc_id = A.cc_id;";
	private static final String SELECT_UNIT_FILE = "SELECT * FROM files A, file_type B  WHERE A.file_id = ?;";
	private static final String SELECT_UNIT_FILES = "SELECT * FROM files  WHERE u_id = ?;";

	
	/**
	 * This method creates a new concrete unit from an abstract unit
	 * @param contextContract_id Context contract id
	 * @param au_id Abstract unit id
	 * @return 
	 * @return 
	 * @throws SQLException 
	 */

	public static int addConcreteUnit(Integer contextContract_id, Integer au_id) throws DBHandlerException {
		int unit_id = -1;
		try {
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(INSERT_CONCRETE_UNIT); 
			prepared.setInt(1, contextContract_id);
			prepared.setInt(2, au_id); 
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				unit_id = result.getInt("unit_id");
			}
		} catch (NumberFormatException e) {
			throw new DBHandlerException("A error occurred while parsing int: ", e);
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
		return unit_id;
	}

	/**
	 * This method gets a concrete unit
	 * @param uid unit id
	 * @return Returns the selected concrete unit
	 * @throws DBHandlerException
	 * @throws SQLException 
	 */

	public static ConcreteUnitType getConcreteUnit(int uid) throws DBHandlerException{

		try {
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONCRETE_UNIT); 
			prepared.setInt(1, uid);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				ConcreteUnitType cut = new ConcreteUnitType();
				cut.setUId(resultSet.getInt("unit_id"));
				cut.setAuId(resultSet.getInt("au_id"));
				cut.setCcId(resultSet.getInt("cc_id"));
				return cut;
			}else{
				throw new DBHandlerException("Concrete unit not found");
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 

	}

	/**
	 * This method returns a concrete unit id
	 * @param abstractUnitName Abstract unit name 
	 * @return Concrete unit id
	 * @throws DBHandlerException 
	 * @throws SQLException 
	 */

	public static int getConcreteUnitID(String abstractUnitName) throws DBHandlerException{

		try {
			Connection con = null; 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONCRETE_UNIT_ID); 
			prepared.setString(1, abstractUnitName);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				return resultSet.getInt("unit_id"); 
			}else{
				throw new DBHandlerException("Concrete unit not found");
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 

	}

	/**
	 * Get all unit files
	 * @param unit_id Concrete unit id
	 * @return List of files metadata
	 * @throws SQLException 
	 */

	public static List<UnitFileType> getUnitFiles(int unit_id) throws DBHandlerException{

		try {
			Connection con = null; 
			ArrayList<UnitFileType> list = new ArrayList<UnitFileType>();

			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_UNIT_FILES); 
			prepared.setInt(1, unit_id);
			ResultSet resultSet = prepared.executeQuery(); 
			while(resultSet.next()) { 
				UnitFileType uft = new UnitFileType();
				uft.setBuildCfg(resultSet.getString("build_cfg"));
				uft.setExtension(resultSet.getString("file_extension"));
				uft.setFilename(resultSet.getString("file_name"));
				uft.setFiletype(resultSet.getInt("file_type"));
				uft.setPath(resultSet.getString("path"));
				uft.setVersion(resultSet.getInt("current_version"));
				uft.setUid(unit_id);
				uft.setFileId(resultSet.getInt("file_id"));
				list.add(uft); 
			}
			return list;
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 

	}

	/**
	 * This method generates a file path
	 * @param file_id File id
	 * @return Path of file
	 * @throws DBHandlerException
	 */

	public static String getUnitFilePath(int file_id) throws DBHandlerException{
		String file = null;
		Connection con = null;
		try {
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_UNIT_FILE);
			prepared.setInt(1, file_id);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) {
				try {
					file = PropertiesHandler.getProperty("core.library.path")+"/"+resultSet.getString("path").replace('.', '/')+"/"+resultSet.getString("file_name")+"."+resultSet.getString("file_extension");
					return file;
				} catch (IOException e) {
					throw new DBHandlerException("An I/O error occurred: ", e);
				}
			}else{
				throw new DBHandlerException("Unit file not found");
			}
		}catch(SQLException e){
			throw new DBHandlerException("A sql error occurred: ", e);
		}

	}

	/**
	 * This method adds a file to an concrete unit
	 * @param name 
	 * @param parent_name 
	 * @return True if well successfully
	 * @throws DBHandlerException 
	 * @throws SQLException 
	 */

	public static String addConcreteUnitFile(UnitFileType uft) throws DBHandlerException {

		try {
			Connection con = getConnection();  
			PreparedStatement prepared = con.prepareStatement(SELECT_UNIT_PATH);
			prepared.setInt(1, uft.getUid());
			System.out.println(prepared);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				uft.setPath(ResolutionHandler.generateResolutionTree().findNode(resultSet.getInt("ac_id")).getPath()); 
				String c_name = resultSet.getString("cc_name");
				uft.setPath(uft.getPath()+"."+c_name);
			}else{
				throw new DBHandlerException("Concrete unit not found");
			}
			prepared = con.prepareStatement(INSERT_UNIT_FILE); 
			prepared.setString(1, uft.getFilename()); 
			prepared.setInt(2, uft.getUid());  
			prepared.setString(3, uft.getExtension()); 
			prepared.setInt(4, uft.getVersion()); 
			prepared.setString(5, uft.getBuildCfg());
			prepared.setInt(6, uft.getFiletype());
			prepared.setString(7, uft.getPath());
			prepared.executeUpdate(); 
			return uft.getPath();
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}

	}

	
	public static List<ConcreteUnitType> getConcreteUnits(Integer ccId) throws DBHandlerException {
		try {
			Connection con = null; 
			ArrayList<ConcreteUnitType> list = new ArrayList<>();
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONCRETE_UNIT_BY_CCID); 
			prepared.setInt(1, ccId);
			ResultSet resultSet = prepared.executeQuery(); 
			while(resultSet.next()) { 
				ConcreteUnitType cut = new ConcreteUnitType();
				cut.setUId(resultSet.getInt("unit_id"));
				list.add(cut); 
			}
			return list;
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}
}
