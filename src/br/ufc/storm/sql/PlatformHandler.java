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
	 */
	public static ContextContract getPlatform(int cc_id){
		Connection con = null; 
		ContextContract cc = new ContextContract();
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_PLATFORM_BY_CC_ID); 
			prepared.setInt(1, cc_id);
			ResultSet resultSet = prepared.executeQuery(); 

			while (resultSet.next()) { 
				AbstractComponentType act = new AbstractComponentType();
				cc.setAbstractComponent(act);
				cc.setCcId(resultSet.getInt("cc_id")); 
				cc.getAbstractComponent().setIdAc(resultSet.getInt("ac_id")); 
				cc.setCcName(resultSet.getString("cc_name"));
			} 
			if(cc.getAbstractComponent()==null){
				closeConnnection(con);
				return null;
			}
			try {
				cc.setAbstractComponent(AbstractComponentHandler.getAbstractComponent(cc.getAbstractComponent().getIdAc()));
			} catch (DBHandlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				cc.getContextArguments().addAll(ContextArgumentHandler.getContextArguments(cc.getCcId()));
			} catch (DBHandlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			closeConnnection(con);
			return cc; 
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return null;
	}


}
