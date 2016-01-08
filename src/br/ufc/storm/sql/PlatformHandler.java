package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.ContextContract;

public class PlatformHandler extends DBHandler {

	private static final String SELECT_PLATFORM_BY_CC_ID = "select * from context_contract A, component_platform B where A.cc_id = B.platform_cc_id and B.cc_id = ?;";


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
				cc.setAbstractComponent(AbstractComponentHandler.getAbstractComponent(cc.getAbstractComponent().getIdAc()));
				cc.getContextArguments().addAll(ContextArgumentHandler.getContextArguments(cc.getCcId()));
				return cc;
			}else{
				throw new DBHandlerException("Platform with abstract component id = "+cc_id+" was not found");
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}


}
