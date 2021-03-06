package br.ufc.storm.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import br.ufc.storm.properties.PropertiesHandler;

/**
 * 
 * @author wagner
 *
 */

public class DBHandler {
	private static Connection con = null;

	/**
	 * This method gets a database connection
	 * @return Connection with database
	 * @throws SQLException
	 */

	public static Connection getConnection() throws SQLException { 
		if(con==null || con.isClosed()){
			try {
				if(!Boolean.parseBoolean(PropertiesHandler.getProperty("core.database.testing"))){
					try {

						Context envCtx = (Context) new InitialContext().lookup("java:comp/env");
						DataSource ds = (DataSource) envCtx.lookup("jdbc/Core");
						con = ds.getConnection();
					}catch (Exception e) {
						throw new SQLException("Connection can not be created");
					}
				}else{
					try {
						con = DriverManager.getConnection("jdbc:postgresql://"+PropertiesHandler.getProperty("core.database.address")+":"+PropertiesHandler.getProperty("core.databaase.port")+"/Core?", PropertiesHandler.getProperty("core.database.user"), PropertiesHandler.getProperty("core.database.password"));
					} catch (IOException e) {
						throw new SQLException("Connection can not be created");
					}
				}
			} catch (IOException e) {
				throw new SQLException("Connection can not be created");
			}
		}
		return con; 
	} 

	public static Connection getConnection(Connection con) throws SQLException { 
		if(con!=null){
			return con;
		}else{
			return getConnection();
		}
	}

}