package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.XMLException;
import br.ufc.storm.jaxb.ComputationalSystemType;
import br.ufc.storm.xml.XMLHandler;

public class SessionHandler extends DBHandler {
	private static final String SELECT_SESSION = "select session_id, user_id, start_time, backend_id, profile_id, platform_id from session where session_id = ?;"; 
	private static final String CREATE_SESSION = "INSERT into session (user_id) VALUES (?) RETURNING session_id;";
	//	private static final String UPDATE_SESSION = "UPDATE session SET platform_state=true, backend_id = ?, profile_id = ?, platform_id = ? WHERE session_id = ?;";
	private static final String DESTROY_SESSION = "INSERT INTO session_history (session_id, user_id, start_time, backend_id, profile_id, platform_id) SELECT session_id, user_id, start_time, backend_id, profile_id, platform_id FROM session WHERE session_id = ?;"+
			"UPDATE session_history SET end_time = now() WHERE session_id = ?;"+
			"DELETE FROM session WHERE session_id = ?;";
	private static final String UPDATE_URI = "UPDATE session SET agent_uri = ? WHERE session_id = ?;";
	private static final String UPDATE_CST = "UPDATE session SET cst = ? WHERE session_id = ?;";
	private static final String SELECT_CST = "select cst from session where session_id = ?;"; 
	

	public static int createSession(int userID) throws DBHandlerException {
		Connection con = null; 
		int sessionID=-1;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(CREATE_SESSION); 
			prepared.setInt(1, userID); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				sessionID = resultSet.getInt("session_id"); 
				return sessionID;
			}else{
				throw new DBHandlerException("Can not create session with user_id: "+userID);
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}

	public static boolean destroySession(int sessionID) throws DBHandlerException {
		int rows = -1;
		try { 
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(DESTROY_SESSION);
			prepared.setInt(1, sessionID);
			prepared.setInt(2, sessionID);
			prepared.setInt(3, sessionID);
			prepared.executeUpdate(); 
			return true;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		}
	}

	@SuppressWarnings("deprecation")
	public static int getSessionTime(int sessionID) throws DBHandlerException {
		Timestamp time = null;
		Timestamp now = null;
		try { 
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_SESSION); 
			prepared.setInt(1, sessionID); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				time = resultSet.getTimestamp("start_time"); 
			}else{
				throw new DBHandlerException("Error while trying get start time from database serverserver");
			}
			prepared = con.prepareStatement("select now();"); 
			resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				now = resultSet.getTimestamp("now"); 
				return now.getSeconds()-time.getSeconds();
			}else{
				throw new DBHandlerException("Error while trying get end time from database serverserver");
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}
	
	public static ComputationalSystemType getComputationalSystem(int sessionID) throws DBHandlerException {
		ComputationalSystemType cst;
		try { 
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CST); 
			prepared.setInt(1, sessionID);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) {
				cst = XMLHandler.getComputationalSystemType(resultSet.getString("cst")); 
			}else{
				throw new DBHandlerException("Can not retrieve computational system");
			}
			return cst;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} catch (XMLException e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	public static boolean setCst(int sessionID, ComputationalSystemType cst) throws DBHandlerException {
		String cs = XMLHandler.getComputationalSystem(cst);
		Connection con = null; 
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(UPDATE_CST); 
			prepared.setString(1, cs); 
			prepared.setInt(2,sessionID);
			if(prepared.execute()){
				return true;
			}else{
				return false;
			}			
		} catch (SQLException e) { 
			throw new DBHandlerException("Can not set computational system: ", e);
		} 
		
	}

	public static boolean setUri(String sessionID, String uri) throws DBHandlerException {
		Connection con = null; 
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(UPDATE_URI); 
			prepared.setString(1, uri); 
			prepared.setInt(2,Integer.parseInt(sessionID));
			if(prepared.execute()){
				return true;
			}else{
				return false;
			}			
		} catch (SQLException e) { 
			throw new DBHandlerException("Can not set uri: ", e);
		} 
		
	}
}


