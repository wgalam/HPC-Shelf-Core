package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.PlatformProfileType;

public class PlatformHandler extends DBHandler {

	private static final String SELECT_PLATFORM_BY_CC_ID = "select * from context_contract A, component_platform B, maintainer C where A.cc_id = B.component_platform_cc_id and B.profile_cc_id = ?;";
	private static final String SELECT_IP_BY_CC_ID = "select ip from platform_owner A, maintainer B where A.maintainer_id = B.maintainer_id AND A.platform_cc_id = ?;";
	private static final String INSERT_PLATFORM_OWNER = "INSERT INTO platform_owner (platform_cc_id, maintainer_id) VALUES (?,(SELECT B.maintainer_id FROM maintainer B, public.user A, maintainer_manager C WHERE A.user_id = C.user_id AND B.maintainer_id = C.maintainer_id AND A.user_id = ?));";
	private static final String SELECT_PORT_BY_CC_ID = "select port from platform_owner A, maintainer B where A.maintainer_id = B.maintainer_id AND A.platform_cc_id = ?;";
	private static final String SELECT_URI_BY_CC_ID = "select uri from platform_owner A, maintainer B where A.maintainer_id = B.maintainer_id AND A.platform_cc_id = ?;";


	/**
	 * This method gets a context contract that represents a platform
	 * @param cc_id
	 * @return
	 * @throws DBHandlerException 
	 */
	public static ContextContract getPlatform(int cc_id) throws DBHandlerException{
		ContextContract cc = new ContextContract();
		try { 
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_PLATFORM_BY_CC_ID); 
			prepared.setInt(1, cc_id);
			ResultSet resultSet = prepared.executeQuery(); 
			
			if(resultSet.next()) { 
				AbstractComponentType act = new AbstractComponentType();
				cc.setAbstractComponent(act);
				cc.setCcId(resultSet.getInt("cc_id")); 
				cc.getAbstractComponent().setIdAc(resultSet.getInt("ac_id")); 
				cc.setCcName(resultSet.getString("cc_name"));
				cc.setKindId(resultSet.getInt("kind_id")); 
				cc.setAbstractComponent(AbstractComponentHandler.getAbstractComponent(cc.getAbstractComponent().getIdAc()));
				cc.getContextArguments().addAll(ContextArgumentHandler.getContextArguments(cc.getCcId()));
				return cc;
			}else{
				throw new DBHandlerException("Platform with abstract component id = "+cc_id+" was not found or platform not assigned for computation component");
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}
	
	public static String getPlatformIP(int cc_id) throws DBHandlerException{
		try { 
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_IP_BY_CC_ID); 
			prepared.setInt(1, cc_id);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				return resultSet.getString("ip");
			}else{
				throw new DBHandlerException("Platform with abstract component id = "+cc_id+" was not found");
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}
	
	public static String getPlatformUri(int cc_id) throws DBHandlerException{
		try { 
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_URI_BY_CC_ID); 
			prepared.setInt(1, cc_id);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				return resultSet.getString("ip");
			}else{
				throw new DBHandlerException("Platform with abstract component id = "+cc_id+" was not found");
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}

	public static Integer getPlatformPort(int cc_id) throws DBHandlerException{
		try { 
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_PORT_BY_CC_ID); 
			prepared.setInt(1, cc_id);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				return resultSet.getInt("port");
			}else{
				throw new DBHandlerException("Platform with abstract component id = "+cc_id+" was not found");
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}
	
	public static PlatformProfileType getPlatformProfile(int cc_id) throws DBHandlerException{
		ContextContract cc = new ContextContract();
		try {
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_PLATFORM_BY_CC_ID); 
			prepared.setInt(1, cc_id);
			ResultSet resultSet = prepared.executeQuery(); 

			if(resultSet.next()) { 
				PlatformProfileType ppt = new PlatformProfileType();
				AbstractComponentType act = new AbstractComponentType();
				cc.setAbstractComponent(act);
				cc.setCcId(resultSet.getInt("cc_id")); 
				cc.getAbstractComponent().setIdAc(resultSet.getInt("ac_id")); 
				cc.setKindId(resultSet.getInt("kind_id")); 
				cc.setCcName(resultSet.getString("cc_name"));
				cc.setAbstractComponent(AbstractComponentHandler.getAbstractComponent(cc.getAbstractComponent().getIdAc()));
				cc.getContextArguments().addAll(ContextArgumentHandler.getContextArguments(cc.getCcId()));
				ppt.setPlatformContract(cc);
				ppt.setNetworkIpAddress(resultSet.getString("ip"));
				return ppt;
			}else{
				throw new DBHandlerException("Platform with abstract component id = "+cc_id+" was not found");
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}

	public static void addPlatformOwner(ContextContract cc) throws DBHandlerException {
		try {
			PreparedStatement prepared = getConnection().prepareStatement(INSERT_PLATFORM_OWNER); 
			prepared.setInt(1, cc.getCcId());
			prepared.setInt(2, cc.getOwnerId());
			prepared.execute();
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}
		
	}


}
