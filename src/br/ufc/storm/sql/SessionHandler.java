package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SessionHandler extends DBHandler {
	private static final String SELECT_SESSION = "select session_id, user_id, start_time, backend_id, profile_id, platform_id from session where session_id = ?;"; 
	private static final String CREATE_SESSION = "INSERT into session (user_id) VALUES (?) RETURNING session_id;";
	//	private static final String UPDATE_SESSION = "UPDATE session SET platform_state=true, backend_id = ?, profile_id = ?, platform_id = ? WHERE session_id = ?;";
	private static final String DESTROY_SESSION = "INSERT INTO session_history (session_id, user_id, start_time, backend_id, profile_id, platform_id) SELECT session_id, user_id, start_time, backend_id, profile_id, platform_id FROM session WHERE session_id = ?;"+
			"UPDATE session_history SET end_time = now() WHERE session_id = ?;"+
			"DELETE FROM session WHERE session_id = ?;";


	public static int createSession(int userID) {
		Connection con = null; 
		int sessionID=-1;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(CREATE_SESSION); 
			prepared.setInt(1, userID); 
			System.out.println(prepared);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				sessionID = resultSet.getInt("session_id"); 
			}

		} catch (SQLException e) { 
			e.printStackTrace(); 
			closeConnnection(con);
			return sessionID;
		} finally { 
			closeConnnection(con); 
		} 
		return sessionID;

	}

	public static boolean destroySession(int sessionID) {
		Connection con = null; 
		int rows = -1;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(DESTROY_SESSION);
			prepared.setInt(1, sessionID);
			prepared.setInt(2, sessionID);
			prepared.setInt(3, sessionID);
			rows = prepared.executeUpdate(); 
		} catch (SQLException e) { 
			e.printStackTrace();
			closeConnnection(con);
			return false;
		} finally { 
			closeConnnection(con); 
		} 
		if(rows > 0){
			return true;	
		}else{
			return false;
		}

	}

	@SuppressWarnings("deprecation")
	public static int getSessionTime(int sessionID) {
		Connection con = null; 
		Timestamp time = null;
		Timestamp now = null;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_SESSION); 
			prepared.setInt(1, sessionID); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				time = resultSet.getTimestamp("start_time"); 
			}
			prepared = con.prepareStatement("select now();"); 
			resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				now = resultSet.getTimestamp("now"); 
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
			closeConnnection(con);
			return -1;
		} finally { 
			closeConnnection(con); 
		}
		return now.getSeconds()-time.getSeconds(); 
	}
}


