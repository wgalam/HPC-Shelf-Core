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
	 * @throws StormException 
	 * @throws DBHandlerException 
	 */

	public static int addContextParameter(String name, String bound_name, String abstractcomponent_name, String context_variable_name) throws StormException, DBHandlerException{
		try { 
			Connection con = getConnection();
			if(validateContexParameter(AbstractComponentHandler.getAbstractComponentID(abstractcomponent_name), AbstractComponentHandler.getAbstractComponentID(bound_name))==false){
				throw new StormException("Composition tree violated");
			}
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_PARAMETER);
			prepared.setString(1, bound_name);
			prepared.setString(2, name);
			prepared.setString(3, abstractcomponent_name);
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				return result.getInt("cp_id");
			}else{
				throw new DBHandlerException("Something goes wrong while trying insert context paramente: ");
			}
		} catch (SQLException e) {
			throw new DBHandlerException("An error occurred while trying add context parameter: "+e.getMessage());
		} 

	}

	/**
	 * This method gets a context parameter id given a context parameter name
	 * @param cp_name Context parameter name
	 * @return context parameter id
	 * @throws DBHandlerException 
	 */

	public static int getContextParameterID(String cp_name) throws DBHandlerException {
		Connection con = null; 
		int CP_id = -2;
		try { 
			con = DBHandler.getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONTEXT_PARAMETER_ID); 
			prepared.setString(1, cp_name); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				CP_id = resultSet.getInt("cp_id"); 
			}else{
				throw new DBHandlerException("Context Parameter not found with cp_name = "+cp_name);
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		}
		return CP_id;
	}

	/**
	 * 
	 * @param cp_id
	 * @return
	 * @throws DBHandlerException 
	 */
	public static ContextParameterType getContextParameter(int cp_id) throws DBHandlerException {
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
			return cp;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		} 

	}


	/**
	 * This method gets a context parameters from a given abstract component id
	 * @param ac_id Abstract component id
	 * @return Array of context parameters
	 * @throws DBHandlerException 
	 */

	public static List<ContextParameterType> getAllContextParameterFromAbstractComponent(int ac_id) throws DBHandlerException {
		//		TODO: Validar se não vai fechar um ciclo entre limites e componentes. Criar uma tabela para validar esta possibilidade, se fechar ciclo, disparar uma exception.
		List<ContextParameterType> cpl = new ArrayList<ContextParameterType>();
		try {  
			PreparedStatement prepared = getConnection().prepareStatement(SELECT_COMPONENT_PARAMETER); 
			prepared.setInt(1, ac_id);
			ResultSet resultSet = prepared.executeQuery(); 
			while (resultSet.next()) {
				ContextParameterType cp = new ContextParameterType();
				cp.setCpId(resultSet.getInt("cp_id"));
				cp.setName(resultSet.getString("cp_name"));
				int bound_id = resultSet.getInt("bound_id");
				try{
					if(bound_id != ac_id){
						cp.setBound(ContextContractHandler.getContextContractIncomplete(bound_id));
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
			return cpl; 
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		} 

	}


	/**
	 * This method gets a Variable Contract from its context parameter and context contract id
	 * @param cp_id
	 * @param cc_id
	 * @return
	 * @throws DBHandlerException 
	 */
	public static ContextContract getVariableContract(int cp_id, int cc_id) throws DBHandlerException{
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
				return cc;
			}else{
				throw new DBHandlerException("Context variable not found with cp_id = "+cp_id);
			}

		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		}
	}

	/**
	 * This method returns an abstract component name given an id
	 * @param cp_id
	 * @return abstract component name
	 * @throws DBHandlerException 
	 */

	public static Integer getBoundValue(int cp_id) throws DBHandlerException{
		try { 
			Connection con = DBHandler.getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_BOUND_VALUE); 
			prepared.setInt(1, cp_id); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				return resultSet.getInt("bound_value"); 
			}else{
				throw new DBHandlerException("Bound not found with cp_id = "+cp_id);
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		} 
		
	}


	/**
	 * This method returns an abstract component name given an id
	 * @param cp_id
	 * @return abstract component name
	 * @throws DBHandlerException 
	 */

	public static Integer getBoundID(int cp_id) throws DBHandlerException{
		try { 
			Connection con = DBHandler.getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_BOUND); 
			prepared.setInt(1, cp_id); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				return resultSet.getInt("bound_id"); 
			}else{
				throw new DBHandlerException("Bound not found with cp_id = "+cp_id);
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		} 
	}

}
