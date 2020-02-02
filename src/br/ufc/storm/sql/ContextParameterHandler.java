package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.CalculatedParameterType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.model.ACCPPair;
import br.ufc.storm.model.MaxElement;
import br.ufc.storm.xml.XMLHandler;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.ResolveException;
import br.ufc.storm.exception.XMLException;

public class ContextParameterHandler extends DBHandler {

	private final static String INSERT_CONTEXT_PARAMETER ="INSERT INTO context_parameter (bound_id, cp_name, ac_id, parameter_type, variance_id) VALUES ((select cc_id from context_contract where cc_name=?), ? ,(select ac_id from abstract_component where ac_name=?), ?,?) RETURNING cp_id;";
	private final static String SELECT_COMPONENT_PARAMETER = "select * from context_parameter where ac_id = ? AND parameter_type = ? and enabled=true;";
	private static final String SELECT_BOUND = "SELECT bound_id FROM context_parameter WHERE cp_id = ?;";
	private static final String INSERT_BOUND_VALUE = "INSERT INTO bound_value (cp_id, bound_value) VALUES (?,?);";
	private static final String SELECT_BOUND_VALUE = "SELECT bound_value FROM context_parameter WHERE cp_id=?;";
	private final static String SELECT_CONTEXT_PARAMETER = " select * from context_parameter where cp_id = ?;";
	private final static String SELECT_CONTEXT_PARAMETER_ID = " select cp_id from context_parameter where cp_name = ?;";
	private static final String SELECT_VARIABLE_CONTEXT_CONTRACT = "select A.cc_id from closed_arguments_context_contract A, context_argument B WHERE A.ca_id = B.ca_id AND B.variable_cp_id = ? AND B.cc_id = ?;";
	private final static String INSERT_CONTEXT_VARIABLE_REQUIRED ="INSERT INTO context_variable_required (cp_id, refers_to_var) VALUES (?,?);";
	private final static String INSERT_CONTEXT_VARIABLE_PROVIDED ="INSERT INTO context_variable_provided (cp_id, variable_name) VALUES (?,?);";
	private final static String SELECT_CONTEXT_VARIABLE_REQUIRED ="SELECT * FROM context_variable_required WHERE cp_id = ?;";
	private final static String SELECT_CONTEXT_VARIABLE_PROVIDED ="SELECT * FROM context_variable_provided WHERE cp_id = ?;";
	private static final String SELECT_CONTEXT_PARAMETER_SPECIALIZED_ID = "select * from context_parameter A, context_parameter_bound_specialization B where A.cp_id = ? AND A.cp_id = B.cp_id;";
	private final static String SELECT_ALL_PARAMETERS ="select * from context_parameter;";
	
	public final static int CONTEXT = 1;
	public final static int QUALITY = 2;
	public final static int COST = 3;
	public final static int CALCULATEDCONTEXT = 5;
	public final static int RANKING = 4;
	public static final int INCREASEKIND = 4;
	public static final int DECREASEKIND = 5;



	/**
	 * This method should test if a component do not generate infinite loops in composition walk
	 * 
	 * @param acid
	 * @param bound
	 * @return
	 */

	public static boolean validateContexParameter(Integer acid, Integer bound){
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
	 * @throws ResolveException 
	 */

	//	public static int addContextParameter(ContextParameterType cp, String ac_name) throws StormException, DBHandlerException{
	//		int cp_id;
	//		try { 
	//			Connection con = getConnection();
	//			if(validateContexParameter(AbstractComponentHandler.getAbstractComponentID(ac_name), AbstractComponentHandler.getAbstractComponentID(cp.getBound().getAbstractComponent().getName()))==false){
	//				throw new StormException("Composition tree violated");
	//			}
	//			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_PARAMETER);
	//			prepared.setString(1, cp.getBound().getCcName());
	//			prepared.setString(2, cp.getName());
	//			prepared.setString(3, ac_name);
	//			ResultSet result = prepared.executeQuery();
	//			if(result.next()){
	//				cp_id = result.getInt("cp_id");
	//				if(cp.getBound().getCcName() == null){
	//					addBoundValue(cp_id, cp.getBoundValue());
	//				}
	//			}else{
	//				throw new DBHandlerException("Something goes wrong while trying insert context paramente: ");
	//			}
	//			
	//			if(cp.getContextVariableProvided() != null){
	//				addSharedVariableProvided(cp.getContextVariableProvided(), cp_id);
	//				
	//			}
	//			return cp_id;
	//		} catch (SQLException e) {
	//			throw new DBHandlerException("An error occurred while trying add context parameter: "+e.getMessage());
	//		} 
	//
	//	}


	public static int addContextParameter(String name, String bound_name, String abstractcomponent_name, String context_variable_name, String boundValue, String required_variable_name, Map<String, Integer> map, Integer parameter_type, int variance) throws DBHandlerException, ResolveException{
		//		TODO: Adicionar variavel compartilhada
		//		Listar todas variáveis compartilhadas, criar método que a partir de um ac, encontra todas as variáveis compartilhadas com aninhados
		if(parameter_type==null){
			throw new ResolveException("Context parameter type can't be null for parameter: "+name);
		}
		int cp_id;
		try { 
			Connection con = getConnection();
			if(validateContexParameter(AbstractComponentHandler.getAbstractComponentID(abstractcomponent_name), AbstractComponentHandler.getAbstractComponentID(bound_name))==false){
				throw new ResolveException("Composition tree violated");
			}
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_PARAMETER);
			prepared.setString(1, bound_name);
			prepared.setString(2, name);
			prepared.setString(3, abstractcomponent_name);
			prepared.setInt(4, parameter_type);
			prepared.setInt(5, variance);
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				cp_id = result.getInt("cp_id");
				if(bound_name == null){
					addBoundValue(cp_id, boundValue);
				}
			}else{
				throw new DBHandlerException("Something goes wrong while trying insert context paramente: ");
			}

			if(context_variable_name != null){
				addSharedVariableProvided(context_variable_name, cp_id);

			}else{
				if(required_variable_name != null){
					Integer refers_to_var = map.get(required_variable_name);
					if(refers_to_var != null){
						addRequiredSharedVariable(cp_id, refers_to_var);
					}else{
						throw new DBHandlerException("Shared variable not found");
					}
				}
			}

			return cp_id;
		} catch (SQLException e) {
			throw new DBHandlerException("An error occurred while trying add context parameter: ", e);
		} 

	}

	/**
	 * This method adds a context parameter that uses a shared variable
	 * @param name
	 * @param bound_name
	 * @param abstractcomponent_name
	 * @param context_variable_name
	 * @return
	 * @throws StormException
	 * @throws DBHandlerException
	 */
	//	public static int addContextParameterUser(String name, String bound_name, String abstractcomponent_name, String required_variable_name, Map<String, Integer> map) throws StormException, DBHandlerException{
	//		//		TODO: Adicionar variavel compartilhada
	//		//		Listar todas variáveis compartilhadas, criar método que a partir de um ac, encontra todas as variáveis compartilhadas com aninhados
	//		int cp_id;
	//		try {
	//			Connection con = getConnection();
	//			if(validateContexParameter(AbstractComponentHandler.getAbstractComponentID(abstractcomponent_name), AbstractComponentHandler.getAbstractComponentID(bound_name))==false){
	//				throw new StormException("Composition tree violated");
	//			}
	//			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_PARAMETER);
	//			prepared.setString(1, bound_name);
	//			prepared.setString(2, name);
	//			prepared.setString(3, abstractcomponent_name);
	//			ResultSet result = prepared.executeQuery();
	//			if(result.next()){
	//				cp_id = result.getInt("cp_id");
	//			}else{
	//				throw new DBHandlerException("Something goes wrong while trying insert context paramente: ");
	//			}
	//			if(required_variable_name != null){
	//				Integer refers_to_var = map.get(required_variable_name);
	//				if(refers_to_var != null){
	//					addRequiredSharedVariable(cp_id, refers_to_var);
	//				}else{
	//					throw new DBHandlerException("Shared variable not found");
	//				}
	//			}
	//			return cp_id;
	//		} catch (SQLException e) {
	//			throw new DBHandlerException("An error occurred while trying add context parameter: "+e.getMessage());
	//		} 
	//
	//	}

	/**
	 * 
	 * @param name
	 * @param cp_id
	 * @throws DBHandlerException
	 */
	public static void addSharedVariableProvided(String name, int cp_id ) throws DBHandlerException{
		try { 
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_VARIABLE_PROVIDED);
			prepared.setInt(1, cp_id);
			prepared.setString(2, name);
			prepared.executeUpdate();
		} catch (SQLException e) {
			throw new DBHandlerException("An error occurred while trying add context parameter: ", e);
		} 
	}

	/**
	 * 
	 * @param cp_id
	 * @param required_var_cp_id
	 * @throws DBHandlerException
	 */
	public static void addRequiredSharedVariable(int cp_id, int required_var_cp_id) throws DBHandlerException{
		try { 
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_VARIABLE_REQUIRED);
			prepared.setInt(1, cp_id);
			prepared.setInt(2, required_var_cp_id);
			prepared.executeUpdate();
		} catch (SQLException e) {
			throw new DBHandlerException("An error occurred while trying add context parameter: ", e);
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
			throw new DBHandlerException("A sql error occurred: ", e);
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
				cp.setKind(resultSet.getInt("variance_id"));
				cp.setCpUnit(resultSet.getString("cp_unit"));
			}

			try {
				cp.setBound(ContextContractHandler.getContextContractIncomplete(getBoundID(cp_id)));
			} catch (DBHandlerException e) {
				cp.setBoundValue(getBoundValue(cp_id));
			}

			try {
				cp.setContextVariableProvided(ContextParameterHandler.getProvidedVariable(cp_id));
			} catch (DBHandlerException e) {
				// Do nothing
			}

			try {
				cp.setContextVariableRequiredId(ContextParameterHandler.getRequiredVariable(cp_id));
			} catch (DBHandlerException e) {
				// Do nothing
			}

			//			prepared = con.prepareStatement(SELECT_CONTEXT_PARAMETER_SPECIALIZED_ID); 
			//			prepared.setInt(1, cp_id); 
			//			resultSet = prepared.executeQuery(); 
			//			if(resultSet.next()) { 
			//				cp.setCpId(resultSet.getInt("refers_to_cpid"));
			//				cp.setName(resultSet.getString("cp_name"));
			//			}
			return cp;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}


	/**
	 * This method gets a context parameters from a given abstract component id
	 * @param ac_id Abstract component id
	 * @return Array of context parameters
	 * @throws DBHandlerException 
	 */

	public static List<ContextParameterType> getAllContextParameterFromAbstractComponent(int ac_id, List<ContextParameterType> listCPS) throws DBHandlerException {
		//		TODO: Validar se não vai fechar um ciclo entre limites e componentes. Criar uma tabela para validar esta possibilidade, se fechar ciclo, disparar uma exception.
		List<ContextParameterType> cpl = new ArrayList<ContextParameterType>();
		cpl.addAll(listCPS);
		try {  
			PreparedStatement prepared = getConnection().prepareStatement(SELECT_COMPONENT_PARAMETER);
			prepared.setInt(1, ac_id);
			prepared.setInt(2, ContextParameterHandler.CONTEXT);
			ResultSet resultSet = prepared.executeQuery();
			while (resultSet.next()) {
				ContextParameterType cp = null;
				cp = new ContextParameterType();
				cp.setCpId(resultSet.getInt("cp_id"));
				cp.setName(resultSet.getString("cp_name"));
				cp.setKind(resultSet.getInt("variance_id"));
				//				cp.setNumericDomain(resultSet.getString("bound_value_domain"));
				Integer bound_id = resultSet.getInt("bound_id");
				cp.setCpUnit(resultSet.getString("cp_unit"));
				
//				int parameter_type = resultSet.getInt("parameter_type");
				
				try{
					if(cp.getKind() >= 3){
						cp.setBoundValue(resultSet.getString("bound_value"));
					}
					if(bound_id != ac_id && bound_id!=0){
						cp.setBound(ContextContractHandler.getContextContractIncomplete(bound_id));
					}else{
						throw new ResolveException("Context Parameter ("+cp.getName()+") bound self referenced results in infinite loop");
					}

				}catch (ResolveException e) {
					ContextContract cc = new ContextContract();
					cc.setAbstractComponent(new AbstractComponentType());
					cc.getAbstractComponent().setIdAc(bound_id);
					cp.setBound(cc);
				}
				cpl.add(cp);
			} 
			return cpl;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}
	
	/**
	 * This method lists all context parameters that will be bind to respective abstract component
	 * @param ac_id
	 * @param listCPS
	 * @return
	 * @throws DBHandlerException
	 */
	public static List<ACCPPair> getAllContextParameters() throws DBHandlerException {
		//		TODO: Validar se não vai fechar um ciclo entre limites e componentes. Criar uma tabela para validar esta possibilidade, se fechar ciclo, disparar uma exception.
		List<ACCPPair> cpl = new LinkedList<>();
		try {  
			PreparedStatement prepared = getConnection().prepareStatement(SELECT_ALL_PARAMETERS);
			ResultSet resultSet = prepared.executeQuery();
			while (resultSet.next()) {
				Object cp;
				int parameter_type = resultSet.getInt("parameter_type");
				int ac_id = resultSet.getInt("ac_id");
				if(parameter_type==1) {
					cp = new ContextParameterType();
					((ContextParameterType) cp).setCpId((resultSet.getInt("cp_id")));
					((ContextParameterType) cp).setName(resultSet.getString("cp_name"));
					((ContextParameterType) cp).setKind(resultSet.getInt("variance_id"));
					//				cp.setNumericDomain(resultSet.getString("bound_value_domain"));
					Integer bound_id = resultSet.getInt("bound_id");
					((ContextParameterType) cp).setCpUnit(resultSet.getString("cp_unit"));
					
					try{
						if(((ContextParameterType) cp).getKind() >= 3){
							((ContextParameterType) cp).setBoundValue(resultSet.getString("bound_value"));
						}
						if(bound_id != ac_id && bound_id!=0){
							ContextContract cc = new ContextContract();
							cc.setCcId(bound_id);
							((ContextParameterType) cp).setBound(cc);
						}else{
							throw new ResolveException("Context Parameter ("+((ContextParameterType) cp).getName()+") bound self referenced results in infinite loop");
						}

					}catch (ResolveException e) {
						ContextContract cc = new ContextContract();
						cc.setAbstractComponent(new AbstractComponentType());
						cc.getAbstractComponent().setIdAc(bound_id);
						((ContextParameterType) cp).setBound(cc);
					}
				}else {
					cp = new CalculatedParameterType();
					((CalculatedParameterType) cp).setCalcId(resultSet.getInt("cp_id"));
					((CalculatedParameterType) cp).setName(resultSet.getString("cp_name"));
					((CalculatedParameterType) cp).setKindId(resultSet.getInt("variance_id"));
					((CalculatedParameterType) cp).setCpUnit(resultSet.getString("cp_unit"));
				}				
				cpl.add(new ACCPPair(ac_id, cp, parameter_type));
			} 
			return cpl;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}

	public static List<ContextParameterType> mergeContextParameterFromAbstractComponent(List<ContextParameterType> list2) throws DBHandlerException {
		Hashtable <Integer , ContextParameterType> history = new Hashtable<>();
		ArrayList<ContextParameterType> list = new ArrayList<>();
		for(ContextParameterType cp:list2){
			Integer cpt_id = getParameterGeneralized(cp.getCpId());
			if(cpt_id==null){
				history.put(cp.getCpId(), cp);
			}else{
				history.put(cp.getCpId(), cp);
				while(cpt_id!= null){
					cp.setCpId(cpt_id); 
					if(history.get(cpt_id)!= null){
						history.remove(cpt_id); 
					}
					cpt_id = getParameterGeneralized(cp.getCpId());
				}
			}
		}
		Enumeration<ContextParameterType> aux = history.elements();
			for(int i = 0; i < history.size(); i++){
				list.add(aux.nextElement());
			}
		return list;
	}


	public static Integer getParameterGeneralized(int cp_id){


		try {
			PreparedStatement prepared;
			prepared = getConnection().prepareStatement(SELECT_CONTEXT_PARAMETER_SPECIALIZED_ID);
			prepared.setInt(1, cp_id);
			ResultSet resultSet = prepared.executeQuery();
			if(resultSet.next()) {
				ContextParameterType cpt = new ContextParameterType();
				cpt.setCpId(resultSet.getInt("refers_to_cpid"));
				cpt.setName(resultSet.getString("cp_name"));
				return cpt.getCpId();
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

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
			throw new DBHandlerException("A sql error occurred: ", e);
		}
	}

	/**
	 * This method returns an abstract component name given an id
	 * @param cp_id
	 * @return abstract component name
	 * @throws DBHandlerException 
	 */

	public static String getBoundValue(int cp_id) throws DBHandlerException{
		try { 
			Connection con = DBHandler.getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_BOUND_VALUE); 
			prepared.setInt(1, cp_id); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				return resultSet.getString("bound_value"); 
			}else{
				throw new DBHandlerException("Bound not found with cp_id = "+cp_id);
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 

	}

	public static void addBoundValue(int cp_id, String value) throws DBHandlerException{
		try { 
			Connection con = DBHandler.getConnection(); 
			PreparedStatement prepared = con.prepareStatement(INSERT_BOUND_VALUE); 
			prepared.setInt(1, cp_id); 
			prepared.setString(2, value);
			prepared.executeUpdate(); 
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
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
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}

	public static String getProvidedVariable(int cp_id) throws DBHandlerException{
		try { 
			PreparedStatement prepared = DBHandler.getConnection().prepareStatement(SELECT_CONTEXT_VARIABLE_PROVIDED); 
			prepared.setInt(1, cp_id); 
			ResultSet resultSet = prepared.executeQuery();
			if(resultSet.next()) { 
				return resultSet.getString("variable_name");
			}else{
				throw new DBHandlerException("Variable provided not found");
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}

	public static Integer getRequiredVariable(int cp_id) throws DBHandlerException{
		try { 
			PreparedStatement prepared = DBHandler.getConnection().prepareStatement(SELECT_CONTEXT_VARIABLE_REQUIRED); 
			prepared.setInt(1, cp_id); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				return resultSet.getInt("refers_to_var");
			}else{
				throw new DBHandlerException("Variable required not found");
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}


	public static ContextParameterType findCp(AbstractComponentType ac, int cp_id){
		for(ContextParameterType cpt: ac.getContextParameter()){
			if(cpt.getCpId()==cp_id){
				return cpt;
			}
		}
		return null;
	}
	
	public static Integer findCpId(AbstractComponentType ac, String cp_name){
		for(ContextParameterType cpt: ac.getContextParameter()){
			if(cpt.getName().equals(cp_name)){
				return cpt.getCpId();
			}
		}
		return null;
	}

	public static Integer getKindFromContextParameter(int cp_id, AbstractComponentType ac){
		for(ContextParameterType cp : ac.getContextParameter()){
			if(cp.getCpId()==cp_id){
				return cp.getKind();
			}
		}
		return null;
	}


}
