package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.AbstractUnitType;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.SliceType;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.XMLException;

public class AbstractComponentHandler extends DBHandler{
	private final static String SELECT_ALL_ABSTRACTCOMPONENT = "select ac_name,ac_id,supertype_id from abstract_component;";
	private final static String INSERT_ABSTRACT_COMPONENT ="INSERT INTO abstract_component (ac_name, supertype_id) VALUES (?, ?) RETURNING ac_id;" ;
	private final static String SELECT_COMPONENT_ID = "select ac_id from abstract_component where ac_name=?;";
	private final static String SELECT_ABSTRACT_COMPONENT_BY_ID = "select * from abstract_component WHERE ac_id = ?;";
	private final static String SELECT_ABSTRACT_COMPONENT_BY_CC_ID = "select * from abstract_component A, context_contract B WHERE A.ac_id = B.ac_id AND cc_id = ?;";
	private final static String SELECT_COMPONENT_NAME = "select ac_name from abstract_component where ac_id=?;";
	private static final String SELECT_ALL_ABTRACT_UNIT = "SELECT * FROM abstract_unit WHERE ac_id = ?;";
	private static final String SELECT_ALL_SLICES = "SELECT * FROM slice WHERE ac_id = ?;";
	private final static String UPDATE_ABSTRACT_COMPONENT = "update abstract_component set enabled=false where ac_name = ?;";
	
	/**
	 * This method gets the list of all abstract components in the library
	 * @return List of components
	 */

	public static List<AbstractComponentType> listAbstractComponents(){
		Connection con = null; 
		int ac_id, supertype_id;
		String name;
		ArrayList<AbstractComponentType> list = new ArrayList<AbstractComponentType>(); 
		try {
			con = getConnection();
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
				ac.getSupertype().setName(AbstractComponentHandler.getAbstractComponentName(supertype_id));
				//				ac.setParent(new AbstractComponentType());
				//				ac.getParent().setIdAc(parent);
				//ac.setKind(kind);
				list.add(ac);
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
			closeConnnection(con);
			return null;
		} finally { 
			closeConnnection(con); 
		} 
		return list;
	}
	
	/**
	 * 
	 * This method adds an Abstract Component into components library
	 * @param ac object
	 * @return Abstract Component added id
	 */
	public static int addAbstractComponent(AbstractComponentType ac) throws DBHandlerException{
		Connection con = null;
		int ac_id = -1;
		try {
			con = DBHandler.getConnection();
			con.setAutoCommit(false);
			PreparedStatement prepared = con.prepareStatement(INSERT_ABSTRACT_COMPONENT);
			prepared.setString(1, ac.getName()); 
			prepared.setInt(2, ac.getSupertype().getIdAc());
			//Removido kind_id pois está definido no parâmetro de contexto
			//			prepared.setInt(3, ac.getKind());
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				ac_id = result.getInt("ac_id");
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
						int ret = ContextParameterHandler.addContextParameter(cp.getName(),boundName, ac.getName(), null, con);
						if(ret < 0){
							fcp=false;
							try {
								throw new XMLException("Storm XML Handler Exception: "+ret+" with parameter: "+cp.getName());
							} catch (XMLException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}


			con.commit();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally { 
			DBHandler.closeConnnection(con); 
		}
		return ac_id;
	}

	/**
	 * This method get an abstract component from components library
	 * @param compName
	 * @return Abstract component if found, else it returns null
	 */
	public static AbstractComponentType getAbstractComponent(int ac_id){
		AbstractComponentType ac = getAbstractComponentPartial(ac_id);
		ac.getInnerComponents().addAll(ContextContractHandler.getInnerComponents(ac_id));
		ac.getContextParameter().addAll(ResolutionHandler.generateResolutionTree().findNode(ac_id).getCps());
		ac.getQualityParameters().addAll(QualityHandler.getQualityParameters(ac_id, null));
		ac.getCostParameters().addAll(CostHandler.getCostParameters(ac_id, null));
		ac.getAbstractUnit().addAll(getAbstractUnits(ac_id));
		ac.getSlices().addAll(getSlices(ac_id));
		return ac;
	}
	
	/**
	 * This method get an abstract component from components library
	 * @param compName
	 * @return Abstract component if found, else it returns null
	 */
	public static AbstractComponentType getAbstractComponentPartial(int ac_id){
		Connection con = null; 
		Integer supertype_id = null;
		String name = null;

		try { 
			con = getConnection(); 
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
			} 
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


			closeConnnection(con);
			return ac; 
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return null;
	}

	/**
	 * This method gets an abstract component from a context contract id
	 * @param cc_id
	 * @return
	 */

	public static AbstractComponentType getAbstractComponentFromContextContractID(int cc_id){
		Connection con = null; 
		int ac_id = 0;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_ABSTRACT_COMPONENT_BY_CC_ID); 
			prepared.setInt(1, cc_id);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				if(resultSet.getBoolean("enabled") == false){
					closeConnnection(con);
					return null;
				}
				ac_id = resultSet.getInt("ac_id");
			} 

			return getAbstractComponent(ac_id); 
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return null;
	}


	/**
	 * This method returns an abstract component name given an id
	 * @param id
	 * @return abstract component name
	 */
	public static String getAbstractComponentName(int id){
		Connection con = null;
		String ac_name = null;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_COMPONENT_NAME); 
			prepared.setInt(1, id); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				ac_name = resultSet.getString("ac_name"); 
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return ac_name;
	}

	/**
	 * This method returns an abstract component id given an name
	 * @param name
	 * @return abstract component id
	 */
	public static int getAbstractComponentID(String name){
		Connection con = null; 
		int ac_id = -2;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_COMPONENT_ID); 
			prepared.setString(1, name); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				ac_id = resultSet.getInt("ac_id"); 
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
			closeConnnection(con);
			return -1;
		} finally { 
			closeConnnection(con); 
		} 
		return ac_id;
	}



	private static List<SliceType> getSlices(int ac_id) {
		Connection con = null;
		ArrayList<SliceType> slices = new ArrayList<SliceType>();
		try { 
			con = getConnection(); 
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
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return slices; 
	}

	private static List<AbstractUnitType> getAbstractUnits(int ac_id) {
		Connection con = null;
		ArrayList<AbstractUnitType> auts = new ArrayList<AbstractUnitType>();
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_ALL_ABTRACT_UNIT);
			prepared.setInt(1, ac_id);
			ResultSet resultSet = prepared.executeQuery();
			while (resultSet.next()) {
				AbstractUnitType aut = new AbstractUnitType();
				aut.setAuName(resultSet.getString("au_name"));
				aut.setAuId(resultSet.getInt("abstract_unit_id"));
				auts.add(aut);
			} 
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return auts; 
	}


	/**
	 * This method is responsible for set a component as removed, disabling it in the database
	 * @param name Abstract component name
	 * @return true if correctly set as removed
	 */
	public static boolean setObsolete(String name){
		Connection con = null; 
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(UPDATE_ABSTRACT_COMPONENT); 
			prepared.setString(1, name); 
			prepared.execute();
			closeConnnection(con);
			return true;
		} catch (SQLException e) { e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return false;


	}



}
