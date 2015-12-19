package br.ufc.storm.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufc.storm.jaxb.AbstractUnitType;
import br.ufc.storm.jaxb.ConcreteUnitType;
import br.ufc.storm.jaxb.UnitFileType;
import br.ufc.storm.properties.PropertiesHandler;

public class ConcreteUnitHandler extends DBHandler {
	private static final String INSERT_CONCRETE_UNIT = "INSERT INTO concrete_unit ( cc_id, au_id) VALUES (?, ?);";
	private static final String SELECT_CONCRETE_UNIT = "SELECT * FROM concrete_unit  WHERE unit_id = ?;";
	private static final String SELECT_CONCRETE_UNIT_ID = "select unit_id from concrete_unit where au_id = (SELECT abstract_unit_id from abstract_unit where au_name = ?);";
	private static final String INSERT_UNIT_FILE = "INSERT INTO files (file_name, u_id, file_extension, current_version, build_cfg, file_type, path) VALUES (?, ?, ?, ?, ?,?,?);";
	private static final String SELECT_UNIT_PATH = "SELECT * FROM concrete_unit A, context_contract B WHERE A.unit_id = ? AND B.cc_id = A.cc_id;";
	private static final String SELECT_UNIT_FILE = "SELECT * FROM files  WHERE file_id = ?;";
	private static final String SELECT_UNIT_FILES = "SELECT * FROM files  WHERE u_id = ?;";
	
	
	/**
	 * This method adds an unit 
	 * @param ccId 
	 * @param parent_name 
	 * @return True if well successfully
	 */

	public static boolean addConcreteUnit(String concrete_componentID, String au_id) {
		Connection con = null; 
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(INSERT_CONCRETE_UNIT); 
			prepared.setInt(1, Integer.parseInt(concrete_componentID)); 
			prepared.setInt(2, Integer.parseInt(au_id)); 
			prepared.executeUpdate(); 
		} catch (SQLException e) { 
			e.printStackTrace(); 
			closeConnnection(con);
			return false;
		} finally { 
			closeConnnection(con); 
		} 
		return true;
	}
	
	public static ConcreteUnitType getConcreteUnit(int uid){
		Connection con = null; 
		ConcreteUnitType cut = null;
		try {
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONCRETE_UNIT); 
			prepared.setInt(1, uid); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				cut = new ConcreteUnitType();
				cut.setUId(resultSet.getInt("unit_id"));
				cut.setAuId(resultSet.getInt("au_id"));
				cut.setCcId(resultSet.getInt("cc_id"));
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return cut;
	}
	
	/**
	 * This method returns a concrete unit id
	 * @param name
	 * @return
	 */

	public static int getConcreteUnitID(String name){
		Connection con = null; 
		int c_unit_id = -2;
		try {
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONCRETE_UNIT_ID); 
			prepared.setString(1, name); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				c_unit_id = resultSet.getInt("unit_id"); 
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
			closeConnnection(con);
			return -1;
		} finally { 
			closeConnnection(con); 
		} 
		return c_unit_id;
	}

	public static List<UnitFileType> getUnitFiles(int unit_id){
		Connection con = null; 
		ArrayList<UnitFileType> list = new ArrayList<UnitFileType>();
		try { 
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
		} catch (SQLException e) { 
			e.printStackTrace();
		} finally { 
			closeConnnection(con); 
		} 
		return list;
	}

	public static String getUnitFilePath(int file_id){
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
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			closeConnnection(con);
		}
		return file;
	}

	/**
	 * This method adds a file to an concrete unit
	 * @param name 
	 * @param parent_name 
	 * @return True if well successfully
	 */

	public static String addConcreteUnitFile(UnitFileType uft) {
		Connection con = null; 
		String c_name = null;

		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_UNIT_PATH);
			prepared.setInt(1, uft.getUid());
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				uft.setPath(ResolutionHandler.generateResolutionTree().findNode(resultSet.getInt("ac_id")).getPath()); 
				c_name = resultSet.getString("cc_name");
				uft.setPath(uft.getPath()+"."+c_name);
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
			e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return null;
	}
}
