package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.ContractList;

public class ContextContractHandler extends DBHandler{
	
	private final static String INSERT_CONTEXT_CONTRACT = "INSERT INTO context_contract (ac_id, cc_name) VALUES ((select ac_id from abstract_component where ac_name = ?), ?) RETURNING cc_id;";
	private final static String SELECT_CONTEXT_CONTRACT_ID = "select cc_id from context_contract where cc_name = ?;";
	private static final String SELECT_CONTEXT_CONTRACT_NAME = "select cc_name from context_contract where cc_id = ?;";
	private static final String SELECT_CONTEXT_CONTRACT = "select cc_id, ac_id, cc_name from context_contract where cc_id = ?;";
	private static final String SELECT_CONTEXT_CONTRACT_BY_AC_ID = "select cc_id from context_contract where ac_id = ?;";
	private final static String INSERT_INNER_COMPONENT = "INSERT INTO inner_components (parent_id, inner_component_name, component_id) VALUES ((select ac_id from abstract_component where ac_name = ?), ?, ?);";
	private final static String SELECT_INNER_COMPONENTS_IDs = "select A.ac_id from inner_components A, abstract_component B where A.parent_id = ? AND A.parent_id = B.ac_id;";
		
	
	
	public static boolean  addContextContract(ContextContract cc){

		Connection con = null;
		int cc_id = -1;
		try {
			con = DBHandler.getConnection();
			con.setAutoCommit(false);
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_CONTRACT);
			prepared.setString(1, cc.getAbstractComponent().getName());
			prepared.setString(2, cc.getCcName());
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				cc_id = result.getInt("cc_id");
			}

			if(cc_id != -1){
				//Add context arguments
				for(ContextArgumentType cat:cc.getContextArguments()){
					cat.setCcId(cc_id);
					ContextArgumentHandler.addContextArgument(cat, con);
				}
				//Add Platform
				addContextContract(cc.getPlatform());
				//Add inner components
				for(ContextContract inner : cc.getInnerComponents()){
					addContextContract(inner);
				}
				
			}



			con.commit();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally { 
			DBHandler.closeConnnection(con); 
		}
		return true;
	}

	/**
	 * This method adds an instantiation type 
	 * @param name Instantiation type name
	 * @param ac_name Abstract component name 
	 * @return instantiation type id
	 */

	public static int addContextContract(String name, String ac_name){
		Connection con = null; 
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_CONTRACT); 
			prepared.setString(1, ac_name); 
			prepared.setString(2,  name); 
			prepared.executeQuery(); 	
		} catch (SQLException e) { 
			e.printStackTrace(); 
			closeConnnection(con);
			return -1;
		} finally { 
			closeConnnection(con); 
		} 
		return getContextContractID(name);
	}
	

	/**
	 * This method returns a context contract
	 * @param cc_id his id
	 * @return
	 */
	public static ContextContract getContextContract(Integer cc_id) {
		Connection con = null; 
		int ac_id = 0;
		String name = null;
		ContextContract cc = null;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONTEXT_CONTRACT); 
			prepared.setInt(1, cc_id);
			ResultSet resultSet = prepared.executeQuery(); 
			while (resultSet.next()){
				ac_id = resultSet.getInt("ac_id");
				name=resultSet.getString("cc_name");
			} 
			cc = new ContextContract();
			cc.setCcId(cc_id);
			cc.setCcName(name);
			cc.setAbstractComponent(AbstractComponentHandler.getAbstractComponent(ac_id));
			cc.getContextArguments().addAll(ContextArgumentHandler.getContextArguments(cc_id));
			//Inicio mudança
			for(ContextArgumentType cat:cc.getContextArguments()){
				//Creating a pointer into context parameter to context argument
				//				System.out.println("Antes de adicionar");
				for(ContextParameterType cpt : cc.getAbstractComponent().getContextParameter()){
					if(cpt.getCpId()==cat.getVariableCpId()){
						//						System.out.println(">> "+cpt.getCpId()+", "+cat.getVariableCpId());
						cpt.setContextArgument(cat);
					}
				}
				//				end of pointer creation
				if(cat.getContextContract()!=null){
					completeContextContract(cat.getContextContract());
				}
			}
		} catch (SQLException e) {
			closeConnnection(con);
			return null;
			//			e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return cc;
	}


	public static ContextContract getContextContractIncomplete(Integer bound_id, Connection con2) {
		//Connection con = null; 
		int ac_id = 0;
		String name = null;
		ContextContract cc = null;
		try { 
			//con = getConnection(); 
			PreparedStatement prepared = con2.prepareStatement(SELECT_CONTEXT_CONTRACT); 
			prepared.setInt(1, bound_id);
			ResultSet resultSet = prepared.executeQuery(); 
			while (resultSet.next()){
				ac_id = resultSet.getInt("ac_id");
				name=resultSet.getString("cc_name");
			} 
			cc = new ContextContract();
			cc.setCcId(bound_id);
			cc.setCcName(name);
			AbstractComponentType ac = new AbstractComponentType();
			ac.setIdAc(ac_id);
			//			cc.setAbstractComponent(DBHandler.getAbstractComponentIncomplete(ac_id));
			//			cc.getContextArguments().addAll(DBHandler.getContextArguments(cc_id));
		} catch (SQLException e) {
			//closeConnnection(con);
			return null;
			//			e.printStackTrace(); 
		} finally { 
			//closeConnnection(con); 
		} 
		return cc;
	}

	/**
	 * This method gets an instantiation type name given its bound
	 * @param cc_id Instantiation type id
	 * @return Instantiation type name
	 */

	public static String getContextContractName(int cc_id) {
		Connection con = null; 
		String name = null;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONTEXT_CONTRACT_NAME); 
			prepared.setInt(1, cc_id); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				name = resultSet.getString("cc_name"); 
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return name;
	}


	/**
	 * This method gets an instantiation type id
	 * @param name Instantiation type name
	 * @return Instantiation type id
	 */

	public static int getContextContractID(String name){
		Connection con = null; 
		int cc_id = -1;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONTEXT_CONTRACT_ID); 
			prepared.setString(1, name); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				cc_id = resultSet.getInt("cc_id"); 
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
			closeConnnection(con);
			return -1;
		} finally { 
			closeConnnection(con); 
		} 
		return cc_id;
	}


	/**
	 * This method gets an instantiation type id
	 * @param name Instantiation type name
	 * @return Instantiation type id
	 */

	public static List<Integer> getContextContractByAcId(int id){
		Connection con = null;
		List<Integer> list = new ArrayList<Integer>();
		int cc_id = -1;
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONTEXT_CONTRACT_BY_AC_ID); 
			prepared.setInt(1, id); 
			ResultSet resultSet = prepared.executeQuery(); 
			while(resultSet.next()) { 
				cc_id = resultSet.getInt("cc_id"); 
				list.add(cc_id);
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

	public static ContractList listContract(int ac_id){
		ContractList list = new ContractList();
		for(Integer i : getContextContractByAcId(ac_id)){
			list.getContract().add(getContextContract(i));
		}
		return list;
	}
	
	
	/**
	 * This method is responsible for populate the context contract from application, it is necessary to reduce the amount of
	 * database queries
	 * @param cc
	 * @return
	 */
	public static void completeContextContract(ContextContract application){
		application.setAbstractComponent(AbstractComponentHandler.getAbstractComponent(application.getAbstractComponent().getIdAc()));
		for(ContextArgumentType cat:application.getContextArguments()){
			if(cat.getContextContract()!=null){
				cat.getContextContract().setCcName(ContextContractHandler.getContextContractName(cat.getContextContract().getCcId()));
				//	cat.setKind(DBHandler.getComponentKind(cat.getContextContract().getAbstractComponent().getIdAc()));
				AbstractComponentType ac = AbstractComponentHandler.getAbstractComponentFromContextContractID(cat.getContextContract().getCcId());
				cat.getContextContract().setAbstractComponent(ac);
				completeContextContract(cat.getContextContract());
				//TODO: Adicionar argumentos de qualidade, custo e ranking, bem como os componenetes aninhados
			}

		}
	}


	/**
	 * This method adds an inner component 
	 * @param name Inner component name 
	 * @param parent_name Parent abstract component name
	 * @return True if well successfully
	 */

	public static boolean addInnerComponent(String ic_name, String name, String parent_name) {
		Connection con = null; 
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(INSERT_INNER_COMPONENT); 
			prepared.setString(1, parent_name); 
			prepared.setString(2, name);  
			prepared.setString(3, ic_name);
			prepared.executeQuery(); 
		} catch (SQLException e) { 
			e.printStackTrace(); 
			closeConnnection(con);
			return false;
		} finally { 
			closeConnnection(con); 
		} 
		return true;
	}

	/**
	 * This method gets an inner component ac_id
	 * @param name Inner component name
	 * @return Inner component id
	 */
	public static ArrayList<AbstractComponentType> getInnerComponents(int parent) {
		Connection con = null; 
		ArrayList<AbstractComponentType> list = new ArrayList<AbstractComponentType>();
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_INNER_COMPONENTS_IDs); 
			prepared.setInt(1, parent); 
			ResultSet resultSet = prepared.executeQuery(); 
			while(resultSet.next()) { 
				list.add(AbstractComponentHandler.getAbstractComponent(resultSet.getInt("ac_id"))); 
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

	
}
