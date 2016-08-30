package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;

import org.apache.axis2.jaxws.common.config.AddressingWSDLExtensionValidator;

import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.AbstractUnitType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.SliceType;
import br.ufc.storm.model.ResolutionNode;
import br.ufc.storm.xml.XMLHandler;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.ResolveException;
import br.ufc.storm.exception.XMLException;

public class AbstractComponentHandler extends DBHandler{
	private final static String SELECT_ALL_ABSTRACTCOMPONENT = "select ac_name,ac_id,supertype_id from abstract_component;";
	private final static String INSERT_ABSTRACT_COMPONENT ="INSERT INTO abstract_component (ac_name, supertype_id) VALUES (?, ?) RETURNING ac_id;" ;
	private final static String SELECT_COMPONENT_ID = "select ac_id from abstract_component where ac_name=?;";
	private final static String SELECT_ABSTRACT_COMPONENT_BY_ID = "select * from abstract_component WHERE ac_id = ?;";
	private final static String SELECT_ABSTRACT_COMPONENT_BY_CC_ID = "select * from abstract_component A, context_contract B WHERE A.ac_id = B.ac_id AND cc_id = ?;";
	private final static String SELECT_COMPONENT_NAME = "select ac_name from abstract_component where ac_id=?;";
	private static final String SELECT_ALL_SLICES = "SELECT * FROM slice WHERE ac_id = ?;";
	private final static String UPDATE_ABSTRACT_COMPONENT = "update abstract_component set enabled=false where ac_name = ?;";
	private static final String INSERT_INNER_COMPONENT = "INSERT INTO inner_components (parent_id, ac_id) VALUES (?, ?) RETURNING ic_id;" ;

	/**
	 * This method gets the list of all abstract components in the library
	 * @return List of components
	 * @throws SQLException 
	 * @throws DBHandlerException
	 */

	public static List<AbstractComponentType> listAbstractComponents() throws DBHandlerException{
		try {
			Connection con = getConnection();
			int ac_id, supertype_id;
			String name;
			ArrayList<AbstractComponentType> list = new ArrayList<AbstractComponentType>();
			PreparedStatement prepared = con.prepareStatement(SELECT_ALL_ABSTRACTCOMPONENT);
			ResultSet resultSet = prepared.executeQuery();
			while (resultSet.next()) {
				name = resultSet.getString("ac_name");
				ac_id = resultSet.getInt("ac_id");
				supertype_id = resultSet.getInt("supertype_id"); 
				AbstractComponentType ac = new AbstractComponentType();
				ac.setIdAc(ac_id);
				ac.setName(name);
				ac.setSupertype(new AbstractComponentType());
				ac.getSupertype().setIdAc(supertype_id);
				try {
					ac.getSupertype().setName(AbstractComponentHandler.getAbstractComponentName(supertype_id));
				} catch (DBHandlerException e) {
					throw new DBHandlerException("Supertype not found", e);
				}
				list.add(ac);
			}
			return list;
		} catch (SQLException e1) {
			throw new DBHandlerException("A sql error occurred: ", e1);
		}

	}

	/**
	 * This method adds an Abstract Component into components library 
	 * @param ac object 
	 * @return Abstract Component added id 
	 * @throws ResolveException 
	 * @throws SQLException 
	 * @throws XMLException 
	 */
	
	public static void main(String [] a){
		AbstractComponentType ac = new AbstractComponentType();
		ac.setName("LSSapp");
		ac.setSupertype(new AbstractComponentType());
		ac.getSupertype().setIdAc(2);
		ac.getContextParameter().add(new ContextParameterType());
		ac.getContextParameter().get(0).setName("par-teste-1");
		ac.getContextParameter().get(0).setKind(1);
		ac.getContextParameter().get(0).setBound(new ContextContract());
		ac.getContextParameter().get(0).getBound().setCcId(21);
		ac.getInnerComponents().add(new AbstractComponentType());
		ac.getInnerComponents().get(0).setName("teste1");
		ac.getInnerComponents().get(0).setSupertype(new AbstractComponentType());
		ac.getInnerComponents().get(0).getSupertype().setIdAc(2);
		ac.getAbstractUnit().add(new AbstractUnitType());
		ac.getAbstractUnit().get(0).setAuName("teste");
		ac.getSlices().add(new SliceType());
		ac.getSlices().get(0).setInnerUnitName("teste");
		ac.getSlices().get(0).setInnerComponentName("teste1");
		
		
		
		try {
			XMLHandler.addAbstractComponentFromXML(XMLHandler.getAbstractComponent(ac));
		} catch (ResolveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Tudo bem");
	}
	
	//TODO: Concluir o cadastro de variáveis
	public static int addAbstractComponent(AbstractComponentType ac, Map<String, Integer> sharedVariables) throws DBHandlerException, ResolveException{

		try {
			Integer ac_id = null;
			
			Connection con = DBHandler.getConnection();
			con.setAutoCommit(false);
			PreparedStatement prepared = con.prepareStatement(INSERT_ABSTRACT_COMPONENT);
			prepared.setString(1, ac.getName()); 
			prepared.setInt(2, ac.getSupertype().getIdAc());
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				ac.setIdAc(result.getInt("ac_id"));
				ac_id = ac.getIdAc();
			}else{
				throw new DBHandlerException("Abstract component id not returned");
			}
			if(ac.getContextParameter().size() > 0){
				if(sharedVariables == null){
					sharedVariables = new HashMap<String, Integer>();
				}
				for(ContextParameterType cp:ac.getContextParameter()){
					//TODO: Corrigir a passagem de variáveis
					//					Se tem variavel compartilhada, adicionar no hashmap

					String boundName = null;
					if(cp.getBound().getCcName() != null){
						boundName = cp.getBound().getCcName();
					}else{
						boundName = ContextContractHandler.getContextContract(cp.getBound().getCcId()).getCcName();
						if(boundName == null){
						//Parameter without bound, must throw an exception
						throw new DBHandlerException("Parameter without bound");
						}
					}

					if(cp.getContextVariableProvided()!=null){
						sharedVariables.put(cp.getContextVariableProvided(), cp.getCpId());
					}
					Integer i = ContextParameterHandler.addContextParameter(cp.getName(), boundName, ac.getName(), null, cp.getBoundValue(), cp.getContextVariableRequired(), sharedVariables, cp.getKind());
					cp.setCpId(i);
					

				}
			}
			//Add each abstract unit
			for(AbstractUnitType aut: ac.getAbstractUnit()){
				aut.setAuId(AbstractUnitHandler.addAbstractUnit(ac.getName(), aut.getAuName()));;
			}
			//Register inner components if not registered yet
			for(AbstractComponentType inner:ac.getInnerComponents()){
				if(inner.getIdAc()==null){
					inner.setIdAc(addAbstractComponent(inner, sharedVariables));
					addInnerComponnet(ac.getIdAc(), inner.getIdAc());
				}
			}
			for(SliceType st:ac.getSlices()){
					SliceHandler.addSlice(st.getInnerComponentName(), st.getInnerUnitName(), ac.getName());
			}
				
			return ac.getIdAc();
			
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		}

	}
	
	public static int addInnerComponnet(Integer ac_id, Integer innerComponent_id) throws DBHandlerException, ResolveException{
		try {
			Connection con = DBHandler.getConnection();
			PreparedStatement prepared = con.prepareStatement(INSERT_INNER_COMPONENT);
			prepared.setInt(1, ac_id); 
			prepared.setInt(2, innerComponent_id);
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				return result.getInt("ic_id");
			}else{
				throw new DBHandlerException("Inner component from ac_id: "+ac_id+" wasn't added");
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		}

	}
	

	/**
	 * This method get an abstract component from components library
	 * @param compName
	 * @return Abstract component if found, else it returns null
	 * @throws DBHandlerException 
	 */
	public static AbstractComponentType getAbstractComponent(int ac_id) throws DBHandlerException{
		try {
			ResolutionNode.setup();
		} catch (ResolveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		AbstractComponentType ac;
		ac = getAbstractComponentPartial(ac_id);
		ac.getInnerComponents().addAll(ContextContractHandler.getInnerComponents(ac_id));
		ac.getContextParameter().addAll(ResolutionNode.resolutionTree.findNode(ac_id).getCps());
		ac.getQualityParameters().addAll(ResolutionNode.resolutionTree.findNode(ac_id).getQps());
		ac.getCostParameters().addAll(ResolutionNode.resolutionTree.findNode(ac_id).getCops());
		ac.getRankingParameters().addAll(ResolutionNode.resolutionTree.findNode(ac_id).getRps());
		ac.getAbstractUnit().addAll(AbstractUnitHandler.getAbstractUnits(ac_id));
		ac.getSlices().addAll(SliceHandler.getSlices(ac_id));
		return ac;
	}

	/**
	 * This method get an abstract component from components library
	 * @param compName
	 * @return Abstract component if found, else it returns null
	 * @throws SQLException 
	 * @throws DBHandlerException 
	 */
	//TODO: Refactor this method
	public static AbstractComponentType getAbstractComponentPartial(int ac_id) throws DBHandlerException{
		try {
			Connection con = getConnection();
			Integer supertype_id = null;
			String name = null;
			PreparedStatement prepared = con.prepareStatement(SELECT_ABSTRACT_COMPONENT_BY_ID); 
			prepared.setInt(1, ac_id);
			ResultSet resultSet = prepared.executeQuery(); 
			if (resultSet.next()) { 
				if(resultSet.getBoolean("enabled") == false){
					return null;
				}
				name=resultSet.getString("ac_name");
				supertype_id = resultSet.getInt("supertype_id"); 
				AbstractComponentType ac = new AbstractComponentType();
				ac.setIdAc(ac_id);
				ac.setName(name);
				if(supertype_id!=null){
					ac.setSupertype(new AbstractComponentType());
					ac.getSupertype().setIdAc(supertype_id);
					ac.getSupertype().setName(getAbstractComponentName(supertype_id));
				}
				return ac; 
			}else{
				throw new DBHandlerException("Abstract component not exists");
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 

	}

	/**
	 * This method gets an abstract component from a context contract id
	 * @param cc_id
	 * @return
	 * @throws SQLException 
	 * @throws DBHandlerException 
	 */

	public static AbstractComponentType getAbstractComponentFromContextContractID(int cc_id) throws DBHandlerException{
		try {
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(SELECT_ABSTRACT_COMPONENT_BY_CC_ID); 
			prepared.setInt(1, cc_id);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				if(resultSet.getBoolean("enabled") == false){
					throw new DBHandlerException("Abstract component disabled, can't be caught");
				}else{
					return getAbstractComponent(resultSet.getInt("ac_id"));	
				}
			}else{
				throw new DBHandlerException("Abstract component not found");
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}

	}

	/**
	 * This method returns an abstract component name given an id
	 * @param id
	 * @return abstract component name
	 * @throws SQLException 
	 * @throws DBHandlerException 
	 */
	public static String getAbstractComponentName(int id) throws DBHandlerException{
		try {
			Connection con = getConnection();
			PreparedStatement prepared;
			prepared = con.prepareStatement(SELECT_COMPONENT_NAME);
			prepared.setInt(1, id); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				return resultSet.getString("ac_name");
			}else{
				throw new DBHandlerException("Abstract component not found");
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 


	}

	/**
	 * This method returns an abstract component id given an name
	 * @param name
	 * @return abstract component id
	 * @throws SQLException 
	 * @throws DBHandlerException 
	 */
	public static Integer getAbstractComponentID(String name) throws DBHandlerException{

		try {
			Connection con = getConnection(); 
			int ac_id = 0;
			PreparedStatement prepared;
			prepared = con.prepareStatement(SELECT_COMPONENT_ID);
			prepared.setString(1, name); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				ac_id = resultSet.getInt("ac_id"); 
				return ac_id;
			}else{
				return null;
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}

	/**
	 * This method is responsible for set a component as removed, disabling it in the database
	 * @param name Abstract component name
	 * @return true if correctly set as removed
	 * @throws SQLException 
	 */
	public static void setObsolete(String name) throws DBHandlerException{

		try {
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(UPDATE_ABSTRACT_COMPONENT);
			prepared.setString(1, name); 
			prepared.execute();
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 

	}



}
