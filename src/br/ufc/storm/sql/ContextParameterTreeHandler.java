package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.webservices.CoreServices;

/**
 * Tree Generators Website
 * >>>http://mshang.ca/syntree/
 * >>>http://ironcreek.net/phpsyntaxtree/?
 * >>>http://yohasebe.com/rsyntaxtree/
 * @author wagner
 *
 */

public class ContextParameterTreeHandler {
	private final static String SELECT_ABSTRACTCOMPONENT = "select ac_name,ac_id,supertype_id from abstract_component where ac_id = ? AND enabled = TRUE;";
	private final static String SELECT_ABSTRACTCOMPONENT_BY_SUPERTYPE = "select ac_name,ac_id,supertype_id from abstract_component where supertype_id = ? AND enabled = TRUE;";
	private final static String SELECT_ABSTRACTCOMPONENT_PARAMETERS ="select * from context_parameter where ac_id = ?;";

	
public static void main(String[] args) {
	System.out.println(CoreServices.getContextParameterTree(3));
}	
	
public static String getComponentParameterTree(int ac_id){
	try {
		String name = null;
		Connection con = DBHandler.getConnection();
		PreparedStatement prepared = con.prepareStatement(SELECT_ABSTRACTCOMPONENT); 
		prepared.setInt(1, ac_id);
		ResultSet resultSet = prepared.executeQuery(); 
		if(resultSet.next()) { 
			name = "["+resultSet.getString("ac_name")+" "+getNode(ac_id);
		}
		name+="]";
		return name;
	} catch (SQLException e) {
		return null;
	}
}
	
	
public static String getNode(int ac_id){
	try {
		String name = "";
		Connection con = DBHandler.getConnection();
		PreparedStatement prepared = con.prepareStatement(SELECT_ABSTRACTCOMPONENT_PARAMETERS); 
		prepared.setInt(1, ac_id);
		ResultSet resultSet = prepared.executeQuery(); 
		Vector<Integer> v = new Vector<>();
		while(resultSet.next()) { 
			if(resultSet.getString("bound_id")!=null){
				try {
					name = name+" ["+resultSet.getString("cp_name")+"("+AbstractComponentHandler.getAbstractComponentName(ContextContractHandler.getContextContractAC_ID(resultSet.getInt("bound_id")))+")"+" ]";
				} catch (DBHandlerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				if(resultSet.getString("bound_value")!=null){
					name = name+" ["+resultSet.getString("cp_name")+"("+resultSet.getString("bound_value")+")"+" ]";
				}else{
					name = name+" ["+resultSet.getString("cp_name")+" ]";
				}
			}
			
		}
		return name;
	} catch (SQLException e) {
		return null;
	}
}



//public static String getNode(int ac_id){
//	try {
//		String name = "";
//		Connection con = DBHandler.getConnection();
//		PreparedStatement prepared = con.prepareStatement(SELECT_ABSTRACTCOMPONENT_BY_SUPERTYPE); 
//		prepared.setInt(1, ac_id);
//		ResultSet resultSet = prepared.executeQuery(); 
//		Vector<Integer> v = new Vector<>();
//		while(resultSet.next()) { 
//			name = name+" "+getTree(resultSet.getInt("ac_id"));
//		}
//		
//		return name;
//	} catch (SQLException e) {
//		return null;
//	}
//}




}
