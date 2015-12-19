package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.StormException;

public class ContextParameterHandler extends DBHandler {

	private final static String INSERT_CONTEXT_PARAMETER ="INSERT INTO context_parameter (bound_id, cp_name, ac_id) VALUES ((select cc_id from context_contract where cc_name=?), ? ,(select ac_id from abstract_component where ac_name=?)) RETURNING cp_id;";
	private final static String SELECT_COMPONENT_PARAMETER = "select * from context_parameter where ac_id = ?;";
	private final static String SELECT_COMPONENT_PARAMETER_BOUND = "select * from context_parameter where ac_id = ?;";
	private static final String SELECT_BOUND = "SELECT bound_id FROM context_parameter WHERE cp_id = ?;";
	private static final String SELECT_BOUND_VALUE = "SELECT bound_value FROM bound_value WHERE cp_id=?;";
	private final static String SELECT_CONTEXT_PARAMETER = " select * from context_parameter A, context_parameter_bound B where A.cp_id = ? AND A.cp_id = B.cp_id;";
	private final static String SELECT_CONTEXT_PARAMETER_ID = " select cp_id from context_parameter where cp_name = ?;";
	private static final String SELECT_VARIABLE_CONTEXT_CONTRACT = "select A.cc_id from closed_arguments_context_contract A, context_argument B WHERE A.ca_id = B.ca_id AND B.variable_cp_id = ? AND B.cc_id = ?;";
	
	/**
	 * This method should test if a component do not generate infinite loops in composition walk
	 * 
	 * @param acid
	 * @param bound
	 * @return
	 */

	public static boolean validateContexParameter(int acid, int bound){
		//TODO: Generated to evaluate the context parameters 
		return true;
	}

	/**
	 * This method adds a context parameter
	 * @param name Context parameter
	 * @param bound_name Bound name
	 * @param abstractcomponent_name Abstract component name
	 * @param context_variable_name Context variable name
	 * @return Context parameter id from added context parameter
	 */

	public static int addContextParameter(String name, String bound_name, String abstractcomponent_name, String context_variable_name, Connection con){
		int cp_id = -1;
		try { 
			if(con == null){
				throw new DBHandlerException("Null database connection");
			}
			if(validateContexParameter(AbstractComponentHandler.getAbstractComponentID(abstractcomponent_name), AbstractComponentHandler.getAbstractComponentID(bound_name))==false){
				throw new StormException("Composition tree violated");
			}
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_PARAMETER);
			prepared.setString(1, bound_name);
			prepared.setString(2, name);
			prepared.setString(3, abstractcomponent_name);
			//			prepared.setString(4, context_variable_name);
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				cp_id = result.getInt("cp_id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} catch (StormException e) {
			//			closeConnnection(con);
			return -1;
		} catch (DBHandlerException e) {
			e.printStackTrace();
		} 
		return cp_id;//getContextParameterID(name)
	}

	/**
	 * This method gets a context parameter id given a context parameter name
	 * @param cp_name Context parameter name
	 * @return context parameter id
	 */

	public static int getContextParameterID(String cp_name) {
		Connection con = null; 
		int CP_id = -2;
		try { 
			con = DBHandler.getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONTEXT_PARAMETER_ID); 
			prepared.setString(1, cp_name); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				CP_id = resultSet.getInt("cp_id"); 
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
			return -1;
		} finally { 
			DBHandler.closeConnnection(con); 
		} 
		return CP_id;
	}

	public static ContextParameterType getContextParameter(int cp_id) {
		Connection con = null; 
		ContextParameterType cp = new ContextParameterType();
		try { 
			con = DBHandler.getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONTEXT_PARAMETER); 
			prepared.setInt(1, cp_id); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				cp.setCpId(cp_id);
				cp.setName(resultSet.getString("cp_name"));
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
			cp = null;
		} finally { 
			DBHandler.closeConnnection(con); 
		} 
		return cp;
	}


	/**
	 * This method gets a context parameters from a given abstract component id
	 * @param ac_id Abstract component id
	 * @return Array of context parameters
	 */

	public static List<ContextParameterType> getAllContextParameterFromAbstractComponent(int ac_id, Connection con) {
		//		TODO: Validar se não vai fechar um ciclo entre limites e componentes. Criar uma tabela para validar esta possibilidade, se fechar ciclo, disparar uma exception.

		List<ContextParameterType> cpl = new ArrayList<ContextParameterType>();
		try {  
			PreparedStatement prepared = con.prepareStatement(SELECT_COMPONENT_PARAMETER); 
			prepared.setInt(1, ac_id);
			ResultSet resultSet = prepared.executeQuery(); 
			while (resultSet.next()) {
				ContextParameterType cp = new ContextParameterType();
				cp.setCpId(resultSet.getInt("cp_id"));
				cp.setName(resultSet.getString("cp_name"));
				int bound_id = resultSet.getInt("bound_id");

				try{
					if(bound_id != ac_id){
						cp.setBound(ContextContractHandler.getContextContractIncomplete(bound_id, con));
						//						System.out.println(cp.getBound().getAbstractComponent().getIdAc()+"5555555555555");
						//TODO: O Limite não precisa estar completo, basta ter o componente abstrato com id
						//						cp.setBound(DBHandler.getContextContract(bound_id));

					}else{
						throw new StormException("Context Parameter bound self referenced results in infinite loop");
					}
				}catch (StormException e) {
					ContextContract cc = new ContextContract();
					cc.setAbstractComponent(new AbstractComponentType());
					cc.getAbstractComponent().setIdAc(bound_id);
					cp.setBound(cc);
				}
				cp.setContextVariable(null); 
				cpl.add(cp);
			} 
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} 
		return cpl; 
	}


	/**
	 * This method gets a Variable Contract from its context parameter and context contract id
	 * @param cp_id
	 * @param cc_id
	 * @return
	 */
	public static ContextContract getVariableContract(int cp_id, int cc_id){
		ContextContract cc = null;
		Connection con = null; 
		try { 
			con = DBHandler.getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_VARIABLE_CONTEXT_CONTRACT); 
			prepared.setInt(1, cp_id);
			prepared.setInt(2, cc_id);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				cc=ContextContractHandler.getContextContract(resultSet.getInt("cc_id")); 
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
			cc = null;
		} finally { 
			DBHandler.closeConnnection(con); 
		} 
		return cc;
	}

	/**
	 * This method returns an abstract component name given an id
	 * @param cp_id
	 * @return abstract component name
	 */

	public static Integer getBoundValue(int cp_id){
		Connection con = null; 
		Integer value = null;
		try { 
			con = DBHandler.getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_BOUND_VALUE); 
			prepared.setInt(1, cp_id); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				value = resultSet.getInt("bound_value"); 
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} finally { 
			DBHandler.closeConnnection(con); 
		} 
		return value;
	}


	/**
	 * This method returns an abstract component name given an id
	 * @param cp_id
	 * @return abstract component name
	 */

	public static Integer getBoundID(int cp_id){
		Connection con = null; 
		Integer bound_id = null;
		try { 
			con = DBHandler.getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_BOUND); 
			prepared.setInt(1, cp_id); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				bound_id = resultSet.getInt("bound_id"); 
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} finally { 
			DBHandler.closeConnnection(con); 
		} 
		return bound_id;
	}

}
