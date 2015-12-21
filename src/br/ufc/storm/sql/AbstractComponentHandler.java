package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.SliceType;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.StormException;
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

	/**
	 * This method gets the list of all abstract components in the library
	 * @return List of components
	 * @throws SQLException 
	 * @throws DBHandlerException 
	 */

	//TODO: Refactor this method
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
				//kind = resultSet.getInt("kind_id");
				//				parent = resultSet.getInt("parent");
				AbstractComponentType ac = new AbstractComponentType();
				ac.setIdAc(ac_id);
				ac.setName(name);
				ac.setSupertype(new AbstractComponentType());
				ac.getSupertype().setIdAc(supertype_id);
				try {
					ac.getSupertype().setName(AbstractComponentHandler.getAbstractComponentName(supertype_id));
				} catch (DBHandlerException e) {
					throw new DBHandlerException("Supertype not found");
				}
				//ac.setParent(new AbstractComponentType());
				//ac.getParent().setIdAc(parent);
				//ac.setKind(kind);
				list.add(ac);
			}
			return list;
		} catch (SQLException e1) {
			throw new DBHandlerException("A sql error occurred: "+e1.getMessage());
		}

	}

	/**
	 * 
	 * This method adds an Abstract Component into components library
	 * @param ac object
	 * @return Abstract Component added id
	 * @throws SQLException 
	 * @throws XMLException 
	 * @throws StormException 
	 */
	//TODO: Refactor this method
	public static int addAbstractComponent(AbstractComponentType ac) throws DBHandlerException, StormException{
		try {
			Connection con = DBHandler.getConnection();
			con.setAutoCommit(false);
			int ac_id = 0;
			PreparedStatement prepared = con.prepareStatement(INSERT_ABSTRACT_COMPONENT);
			prepared.setString(1, ac.getName()); 
			prepared.setInt(2, ac.getSupertype().getIdAc());
			//Removido kind_id pois está definido no parâmetro de contexto
			//			prepared.setInt(3, ac.getKind());
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				ac_id = result.getInt("ac_id");
			}else{
				throw new DBHandlerException("Abstract component id not returned");
			}
			if(ac.getContextParameter()!=null){
				for(ContextParameterType cp:ac.getContextParameter()){
					//					TODO: Corrigir a passagem de variáveis
					String boundName = null;
					if(cp.getBound() != null){
						boundName = cp.getBound().getCcName();
					}
					boolean fcp = true;
					if(fcp){
						int ret = ContextParameterHandler.addContextParameter(cp.getName(),boundName, ac.getName(), null);
						if(ret < 0){
							fcp=false;
							throw new DBHandlerException("Storm XML Handler Exception: "+ret+" with parameter: "+cp.getName());
						}
					}
				}
			}
			con.commit();
			return ac_id;
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
		AbstractComponentType ac;
		ac = getAbstractComponentPartial(ac_id);
		ac.getInnerComponents().addAll(ContextContractHandler.getInnerComponents(ac_id));
		ac.getContextParameter().addAll(ResolutionHandler.generateResolutionTree().findNode(ac_id).getCps());
		ac.getQualityParameters().addAll(QualityHandler.getQualityParameters(ac_id));
		ac.getCostParameters().addAll(CostHandler.getCostParameters(ac_id));
		ac.getAbstractUnit().addAll(AbstractUnitHandler.getAbstractUnits(ac_id));
		ac.getSlices().addAll(getSlices(ac_id));
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
					closeConnnection(con);
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
				//			ac.setParent(new AbstractComponentType());
				//			ac.getParent().setIdAc(parent);
				//			List<ContextParameterType> t = DBHandler.getAllContextParameterFromAbstractComponent(ac_id);
				//			ac.getContextParameter().addAll(t);
				return ac; 
			}else{
				throw new DBHandlerException("Abstract component not exists");
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
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
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
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
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		} 


	}

	/**
	 * This method returns an abstract component id given an name
	 * @param name
	 * @return abstract component id
	 * @throws SQLException 
	 * @throws DBHandlerException 
	 */
	public static int getAbstractComponentID(String name) throws DBHandlerException{

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
				throw new DBHandlerException("Abstract component not found");
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		} 
	}

	/**
	 * 
	 * @param ac_id
	 * @return
	 * @throws SQLException 
	 */

	private static List<SliceType> getSlices(int ac_id) throws DBHandlerException {

		try {
			Connection con = getConnection();
			ArrayList<SliceType> slices = new ArrayList<SliceType>();
			PreparedStatement prepared = con.prepareStatement(SELECT_ALL_SLICES);
			prepared.setInt(1, ac_id);
			ResultSet resultSet = prepared.executeQuery();
			while (resultSet.next()) {				
				SliceType slc = new SliceType();
				slc.setSliceId(resultSet.getInt("slice_id"));
				slc.setSliceId(resultSet.getInt("inner_component_id"));
				slc.setSliceId(resultSet.getInt("inner_unit_id"));
				slices.add(slc);
			} 
			return slices; 
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
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
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		} 

	}



}
