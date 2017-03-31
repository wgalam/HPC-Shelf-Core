package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import br.ufc.storm.webservices.CoreServices;

/**
 * Tree Generators Website
 * >>>http://mshang.ca/syntree/
 * >>>http://ironcreek.net/phpsyntaxtree/?
 * >>>http://yohasebe.com/rsyntaxtree/
 * @author wagner
 */

public class AbstractComponentTreeHandler {
	private final static String SELECT_ABSTRACTCOMPONENT = "select ac_name,ac_id,supertype_id from abstract_component where ac_id = ? AND enabled = TRUE;";
	private final static String SELECT_ABSTRACTCOMPONENT_BY_SUPERTYPE = "select ac_name,ac_id,supertype_id from abstract_component where supertype_id = ? AND enabled = TRUE;";

//	Adicionar um espaço após o primeiro filho para corrigir o erro de sintaxe
	
public static void main(String[] args) {
	System.out.println(CoreServices.getDerivationTree(96));
}	
	
public static String getTree(int ac_id){
	try {
		String name = null;
		Connection con = DBHandler.getConnection();
		PreparedStatement prepared = con.prepareStatement(SELECT_ABSTRACTCOMPONENT); 
		prepared.setInt(1, ac_id);
		ResultSet resultSet = prepared.executeQuery(); 
		if(resultSet.next()) { 
			name = " ["+resultSet.getString("ac_name")+getNode(ac_id);
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
		PreparedStatement prepared = con.prepareStatement(SELECT_ABSTRACTCOMPONENT_BY_SUPERTYPE); 
		prepared.setInt(1, ac_id);
		ResultSet resultSet = prepared.executeQuery(); 
		Vector<Integer> v = new Vector<>();
		while(resultSet.next()) { 
			name = name+getTree(resultSet.getInt("ac_id"));
		}
		
		return name;
	} catch (SQLException e) {
		return null;
	}
}






}
