package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.AbstractUnitType;
import br.ufc.storm.jaxb.CalculatedArgumentType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextArgumentValueType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.ContractList;
import br.ufc.storm.jaxb.PlatformProfileType;
import br.ufc.storm.xml.XMLHandler;

public class ContextContractHandler extends DBHandler{
	private final static String INSERT_CONTEXT_CONTRACT = "INSERT INTO context_contract (ac_id, cc_name, type_id, owner_id) VALUES ((select ac_id from abstract_component where ac_name = ?), ?, ?, ?) RETURNING cc_id;";
	private final static String SELECT_CONTEXT_CONTRACT_ID = "select cc_id from context_contract where cc_name = ?;";
	private static final String SELECT_CONTEXT_CONTRACT_NAME = "select cc_name from context_contract where cc_id = ?;";
	private static final String SELECT_CONTEXT_CONTRACT = "select * from context_contract where cc_id = ?;";
	private static final String SELECT_CONTEXT_CONTRACT_BY_AC_ID = "select cc_id from context_contract where ac_id = ?;";
	private static final String SELECT_CC_ID_NAME = "SELECT * FROM context_contract WHERE cc_id = ? OR cc_name = ?;";
	private static final String INSERT_PLATFORM_LINKAGE = "INSERT INTO component_platform  (cc_id, platform_cc_id) VALUES (?,?);";
	private static final String INSERT_INNER_COMPONENT = "INSERT INTO concrete_inner_contract  (parent_cc_id, inner_cc_id) VALUES (?,?) RETURNING id;";
	private static final String SELECT_INNER_COMPONENT = "SELECT * FROM concrete_inner_contract  WHERE parent_cc_id = ?;";

	private static Integer owner = null;

	public static void main(String[] args) {
		for(int i = 0; i < 0; i++){
			//BEGIN PROCESSOR
			ContextContract processor = new ContextContract();
			processor.setCcName("E5-2650v3");
			processor.setAbstractComponent(new AbstractComponentType());
			processor.getAbstractComponent().setName("XEON");
			processor.getContextArguments().add(new ContextArgumentType());
			processor.getContextArguments().get(0).setCpId(54);
			processor.getContextArguments().get(0).setValue(new ContextArgumentValueType());
			processor.getContextArguments().get(0).getValue().setValue("20");
			processor.getContextArguments().get(0).getValue().setDataType("Integer");
			processor.getContextArguments().add(new ContextArgumentType());
			processor.getContextArguments().get(1).setCpId(33);
			processor.getContextArguments().get(1).setValue(new ContextArgumentValueType());
			processor.getContextArguments().get(1).getValue().setValue("10");
			processor.getContextArguments().get(1).getValue().setDataType("Integer");
			processor.getContextArguments().add(new ContextArgumentType());
			processor.getContextArguments().get(2).setCpId(42);
			processor.getContextArguments().get(2).setValue(new ContextArgumentValueType());
			processor.getContextArguments().get(2).getValue().setValue("2300");
			processor.getContextArguments().get(2).getValue().setDataType("Integer");
			processor.getContextArguments().add(new ContextArgumentType());
			processor.getContextArguments().get(3).setCpId(43);
			processor.getContextArguments().get(3).setValue(new ContextArgumentValueType());
			processor.getContextArguments().get(3).getValue().setValue("640");
			processor.getContextArguments().get(3).getValue().setDataType("Integer");
			processor.getContextArguments().add(new ContextArgumentType());
			processor.getContextArguments().get(4).setCpId(44);
			processor.getContextArguments().get(4).setValue(new ContextArgumentValueType());
			processor.getContextArguments().get(4).getValue().setValue("2560");
			processor.getContextArguments().get(4).getValue().setDataType("Integer");
			processor.getContextArguments().add(new ContextArgumentType());
			processor.getContextArguments().get(5).setCpId(45);
			processor.getContextArguments().get(5).setValue(new ContextArgumentValueType());
			processor.getContextArguments().get(5).getValue().setValue("25600");
			processor.getContextArguments().get(5).getValue().setDataType("Integer");
			processor.getContextArguments().add(new ContextArgumentType());
			processor.getContextArguments().get(6).setCpId(28);
			processor.getContextArguments().get(6).setContextContract(new ContextContract());
			processor.getContextArguments().get(6).getContextContract().setCcName("Intel");
			//END PROCESSOR

			//BEGIN NODE
			ContextContract node = new ContextContract();
			node.setCcName("MDCC-CPU-Node");
			node.setAbstractComponent(new AbstractComponentType());
			node.getAbstractComponent().setName("Node");
			node.getContextArguments().add(new ContextArgumentType());
			node.getContextArguments().get(0).setCpId(28);
			node.getContextArguments().get(0).setContextContract(processor);
			node.getContextArguments().add(new ContextArgumentType());
			node.getContextArguments().get(1).setCpId(49);
			node.getContextArguments().get(1).setContextContract(new ContextContract());
			node.getContextArguments().get(1).getContextContract().setCcId(234);
			node.getContextArguments().add(new ContextArgumentType());
			node.getContextArguments().get(2).setCpId(32);
			node.getContextArguments().get(2).setValue(new ContextArgumentValueType());
			node.getContextArguments().get(2).getValue().setValue("2");
			node.getContextArguments().get(2).getValue().setDataType("Integer");
			node.getContextArguments().add(new ContextArgumentType());
			node.getContextArguments().get(3).setCpId(38);
			node.getContextArguments().get(3).setValue(new ContextArgumentValueType());
			node.getContextArguments().get(3).getValue().setValue("2048");
			node.getContextArguments().get(3).getValue().setDataType("Integer");
			node.getContextArguments().add(new ContextArgumentType());
			node.getContextArguments().get(4).setCpId(36);
			node.getContextArguments().get(4).setValue(new ContextArgumentValueType());
			node.getContextArguments().get(4).getValue().setValue("64");
			node.getContextArguments().get(4).getValue().setDataType("Integer");
			//					node.getContextArguments().add(new ContextArgumentType());
			//					node.getContextArguments().get(5).setCpId(101);
			//					node.getContextArguments().get(5).setContextContract(new ContextContract());
			//					node.getContextArguments().get(5).getContextContract().setCcId(185);
			//END NODE


			//BEGIN INTERCONNECT
			ContextContract interconnect = new ContextContract();
			interconnect.setCcName("MDCC-Interconnect");
			interconnect.setAbstractComponent(new AbstractComponentType());
			interconnect.getAbstractComponent().setName("GigabitEthernet");
			interconnect.getContextArguments().add(new ContextArgumentType());
			interconnect.getContextArguments().get(0).setCpId(98);
			interconnect.getContextArguments().get(0).setValue(new ContextArgumentValueType());
			interconnect.getContextArguments().get(0).getValue().setValue("5");
			interconnect.getContextArguments().get(0).getValue().setDataType("Integer");
			interconnect.getContextArguments().add(new ContextArgumentType());
			interconnect.getContextArguments().get(1).setCpId(99);
			interconnect.getContextArguments().get(1).setValue(new ContextArgumentValueType());
			interconnect.getContextArguments().get(1).getValue().setValue("6.6");
			interconnect.getContextArguments().get(1).getValue().setDataType("Float");
			interconnect.getContextArguments().add(new ContextArgumentType());
			interconnect.getContextArguments().get(2).setCpId(97);
			interconnect.getContextArguments().get(2).setContextContract(new ContextContract());
			interconnect.getContextArguments().get(2).getContextContract().setCcName("Star");
			interconnect.getContextArguments().get(2).getContextContract().setAbstractComponent(new AbstractComponentType());
			interconnect.getContextArguments().get(2).getContextContract().getAbstractComponent().setName("Star");
			//END INTERCONNECT

			//BEGIN CLUSTER
			ContextContract cc = new ContextContract();
			cc.setCcName("MDCC-CPU-Large");
			cc.setOwnerId(4);
			cc.setAbstractComponent(new AbstractComponentType());
			cc.getAbstractComponent().setName("Cluster");
			cc.getContextArguments().add(new ContextArgumentType());
			cc.getContextArguments().get(0).setCpId(23);
			cc.getContextArguments().get(0).setContextContract(node);
			cc.getContextArguments().add(new ContextArgumentType());
			cc.getContextArguments().get(1).setCpId(24);
			cc.getContextArguments().get(1).setContextContract(interconnect);
			cc.getContextArguments().add(new ContextArgumentType());
			cc.getContextArguments().get(2).setCpId(26);
			cc.getContextArguments().get(2).setContextContract(new ContextContract());
			cc.getContextArguments().get(2).getContextContract().setCcId(144);
			cc.getContextArguments().add(new ContextArgumentType());
			cc.getContextArguments().get(3).setCpId(27);
			cc.getContextArguments().get(3).setValue(new ContextArgumentValueType());
			cc.getContextArguments().get(3).getValue().setValue("8");
			cc.getContextArguments().get(3).getValue().setDataType("Double");
			cc.getContextArguments().add(new ContextArgumentType());
			cc.getContextArguments().get(4).setCpId(35);
			cc.getContextArguments().get(4).setContextContract(new ContextContract());
			cc.getContextArguments().get(4).getContextContract().setCcId(172);


			try {
				DBHandler.getConnection().setAutoCommit(false);
				addPlatformContextContract(cc);
				DBHandler.getConnection().commit();
				DBHandler.getConnection().setAutoCommit(true);

				System.out.println(XMLHandler.getContextContract(cc));;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//		ContextContract cc = new ContextContract();
		//		cc.setCcName("MatrixMultiplicationMIC");
		//		cc.setOwnerId(1);
		//		cc.setAbstractComponent(new AbstractComponentType());
		//		cc.getAbstractComponent().setName("MatrixMultiplication");
		//		cc.getContextArguments().add(new ContextArgumentType());
		//		cc.getContextArguments().get(0).setCpId(102);
		//		cc.getContextArguments().get(0).setContextContract(new ContextContract());
		//		cc.getContextArguments().get(0).getContextContract().setCcName("SparseMatrix");
		//		cc.getContextArguments().get(0).getContextContract().setAbstractComponent(new AbstractComponentType());
		//		cc.getContextArguments().get(0).getContextContract().getAbstractComponent().setName("SparseMatrix");;
		//		cc.getContextArguments().get(0).getContextContract().setOwnerId(1);
		//		cc.setPlatform(new PlatformProfileType());
		//		cc.getPlatform().setPlatformContract(new ContextContract());
		//		cc.getPlatform().getPlatformContract().setCcName("MICPlatformRequired");
		//		cc.getPlatform().getPlatformContract().setAbstractComponent(new AbstractComponentType());
		//		cc.getPlatform().getPlatformContract().getAbstractComponent().setName("Cluster");
		//		cc.getPlatform().getPlatformContract().getContextArguments().add(new ContextArgumentType());
		//		cc.getPlatform().getPlatformContract().getContextArguments().get(0).setCpId(23);
		//		cc.getPlatform().getPlatformContract().getContextArguments().get(0).setContextContract(new ContextContract());
		//		cc.getPlatform().getPlatformContract().getContextArguments().get(0).getContextContract().setCcName("MICPlatformNode");
		//		cc.getPlatform().getPlatformContract().getContextArguments().get(0).getContextContract().setAbstractComponent(new AbstractComponentType());
		//		cc.getPlatform().getPlatformContract().getContextArguments().get(0).getContextContract().getAbstractComponent().setName("Node");
		//		cc.getPlatform().getPlatformContract().getContextArguments().get(0).getContextContract().getContextArguments().add(new ContextArgumentType());
		//		cc.getPlatform().getPlatformContract().getContextArguments().get(0).getContextContract().getContextArguments().get(0).setCpId(101);
		//		cc.getPlatform().getPlatformContract().getContextArguments().get(0).getContextContract().getContextArguments().get(0).setContextContract(new ContextContract());
		//		cc.getPlatform().getPlatformContract().getContextArguments().get(0).getContextContract().getContextArguments().get(0).getContextContract().setAbstractComponent(new AbstractComponentType());
		//		cc.getPlatform().getPlatformContract().getContextArguments().get(0).getContextContract().getContextArguments().get(0).getContextContract().getAbstractComponent().setName("MIC");
		//		cc.getPlatform().getPlatformContract().getContextArguments().get(0).getContextContract().getContextArguments().get(0).getContextContract().setCcId(232);
		//		try {
		//			DBHandler.getConnection().setAutoCommit(false);
		//			addContextContract(cc);
		//			DBHandler.getConnection().commit();
		//			DBHandler.getConnection().setAutoCommit(true);
		//		} catch (Exception e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}



		System.out.println("Finished");
	}

	private static Integer insertComponentContextContract(ContextContract cc) throws DBHandlerException{
		if(cc.getCcId()!=null){
			throw new DBHandlerException("Context Contract id must be auto generated, can not be informed");
		}
		try {
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_CONTRACT);
			prepared.setString(1, cc.getAbstractComponent().getName());
			prepared.setString(2, cc.getCcName());
			prepared.setInt(3, 0);
			prepared.setInt(4, cc.getOwnerId());
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				cc.setCcId(result.getInt("cc_id"));
			}
			return cc.getCcId();
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}
	}




	private static Integer insertPlatformContextContract(ContextContract cc) throws DBHandlerException{

		if(cc.getCcId()!=null){
			throw new DBHandlerException("Context Contract id must be auto generated, can not be informed");
		}
		try {
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_CONTRACT);
			prepared.setString(1, cc.getAbstractComponent().getName());
			prepared.setString(2, cc.getCcName());
			prepared.setInt(3, 1);
			prepared.setInt(4, cc.getOwnerId());
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				cc.setCcId(result.getInt("cc_id"));
			}
			return cc.getCcId();
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}
	}




	private static boolean exists(ContextContract cc) throws DBHandlerException{
		try {
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(SELECT_CC_ID_NAME);

			if(cc.getCcId()==null){
				prepared.setNull(1, java.sql.Types.INTEGER);
			}else{
				prepared.setInt(1, cc.getCcId());
			}
			prepared.setString(2, cc.getCcName());
			ResultSet result = prepared.executeQuery();
			int cont = 0;
			int id=0;
			while(result.next()){
				id = result.getInt("cc_id");
				cont++;
			}
			if(cont==0){
				return false;
			}else{
				if(cont == 1){
					cc.setCcId(id);
					return true;
				}else{
					throw new DBHandlerException("Multiple compoenent contracts where found: ");
				}
			}
		} catch (Exception e) {
			throw new DBHandlerException("Error retrieving context contracts: ", e);
		}
	}

	private static boolean linkPlatformRequired(Integer ccId, Integer ccId2) throws SQLException {
		Connection con = getConnection();
		PreparedStatement prepared = con.prepareStatement(INSERT_PLATFORM_LINKAGE);
		prepared.setInt(1, ccId);
		prepared.setInt(2, ccId2);
		if(prepared.execute()){
			return true;
		}else{
			return false;
		}
	}

	public static Integer addContextContract(ContextContract cc) throws DBHandlerException{
		if(owner==null){
			owner = cc.getOwnerId();
		}
		try {
			if(cc.getAbstractComponent()!=null){
				AbstractComponentHandler.addAbstractComponent(cc.getAbstractComponent(), null);
			}			

			//Verificar se o contrato já existe
			if(ContextContractHandler.exists(cc)){
				//Se existe, adicionar e/ou atualizar argumentos
				System.out.println("Já existe contrato");
			}else{
				insertComponentContextContract(cc);
				//Se não existe, adicionar argumentos
				System.out.println("Não existe contrato");
			}
			//Verificar se existem componentes aninhados
			for(ContextContract inner : cc.getInnerComponents()){
				//Se existe, chamada recursiva para cada um
				System.out.println("Existem componentes aninhados");
				if(inner.getOwnerId()==null){
					inner.setOwnerId(owner);
				}
				addContextContract(inner);
				addInnerCompponent(cc.getCcId(), inner.getCcId());
			}

			//Add context arguments only if it doesnt exists (at this moment not updating the value of an existent argument)
			for(ContextArgumentType cat:cc.getContextArguments()){
				if(cat.getContextContract()!=null){
					if(cat.getContextContract().getOwnerId()==null){
						cat.getContextContract().setOwnerId(owner);
					}
					if(cat.getCcId()==null){
						addContextContract(cat.getContextContract());
					}
				}
				cat.setCcId(cc.getCcId());
				ContextArgumentHandler.addContextArgument(cat);
			}



			//			TODO: Create a table in db to link the contract with it inner component at concrete sphere


			//Add inner components
			//			for(ContextContract inner : cc.getInnerComponents()){
			//				addContextContract(inner);
			//				addInnerComponent(null,, cc.getAbstractComponent().getName());
			////				TODO: set as an inner component
			//			}
			//add quality functions
			for(CalculatedArgumentType qa : cc.getQualityArguments()){
				CalculatedArgumentHandler.addCalculatedFunction(qa.getFunction(), 1);
			}
			//add cost functions
			for(CalculatedArgumentType ca : cc.getCostArguments()){
				CalculatedArgumentHandler.addCalculatedFunction(ca.getFunction(), 2);
				//CostHandler.addCostFunction(ca.getFunction());
			}

			if(cc.getPlatform()!=null){
				cc.getPlatform().getPlatformContract().setOwnerId(cc.getOwnerId());
				addContextContract(cc.getPlatform().getPlatformContract());
				linkPlatformRequired(cc.getCcId(), cc.getPlatform().getPlatformContract().getCcId());
			}
			return cc.getCcId();
		} catch (Exception e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}
	}

	public static Integer addPlatformContextContract(ContextContract cc) throws DBHandlerException{
		try {
			AbstractComponentHandler.addAbstractComponent(cc.getAbstractComponent(), null);
			//Verificar se o contrato já existe
			if(ContextContractHandler.exists(cc)){
				//Se existe, adicionar e/ou atualizar argumentos
				System.out.println("Já existe contrato");
			}else{
				insertPlatformContextContract(cc);
				//Se não existe, adicionar argumentos
				System.out.println("Não existe contrato");
			}
			PlatformHandler.addPlatformOwner(cc);
			//Add context arguments only if it doesn't exists (at this moment not updating the value of an existent argument)
			for(ContextArgumentType cat:cc.getContextArguments()){
				//				TODO: Fazer merge tabelas usuários

				if(cat.getCcId()==null && cat.getContextContract()!=null){
					cat.getContextContract().setOwnerId(1);
					if(cat.getContextContract()!=null){
						if(cat.getContextContract().getOwnerId()==null){
							cat.getContextContract().setOwnerId(owner);
						}
					}
					cat.getContextContract().setOwnerId(1);
					addContextContract(cat.getContextContract());
				}
				cat.setCcId(cc.getCcId());
				ContextArgumentHandler.addContextArgument(cat);
			}
			//add quality functions
			for(CalculatedArgumentType qa : cc.getQualityArguments()){
				CalculatedArgumentHandler.addCalculatedFunction(qa.getFunction(), 1);
			}
			//add cost functions
			for(CalculatedArgumentType ca : cc.getCostArguments()){
				CalculatedArgumentHandler.addCalculatedFunction(ca.getFunction(), 2);
			}

			System.out.println(cc.getCcId());
			return cc.getCcId();
		} catch (Exception e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}
	}

	/**
	 * This method adds an instantiation type 
	 * @param name Instantiation type name
	 * @param ac_name Abstract component name 
	 * @return instantiation type id
	 * @throws SQLException 
	 * @throws DBHandlerException 
	 */

	public static int addContextContract(String name, String ac_name) throws DBHandlerException{
		try {
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_CONTRACT); 
			prepared.setString(1, ac_name); 
			prepared.setString(2,  name); 
			prepared.executeQuery(); 	
			return getContextContractID(name);
		} catch (SQLException e) {
			throw new DBHandlerException("An error occured while trying to add a context contract with name: "+name+" and abstract component name "+ac_name+". Error: ", e);
		} 

	}


	/**
	 * This method returns a context contract
	 * @param cc_id his id
	 * @return
	 * @throws SQLException 
	 * @throws DBHandlerException 
	 */
	public static ContextContract getContextContract(Integer cc_id) throws DBHandlerException {
		try {
			Connection con = getConnection();
			int ac_id = 0;
			String owner = null;
			String name = null;
			ContextContract cc = null;
			PreparedStatement prepared = con.prepareStatement(SELECT_CONTEXT_CONTRACT); 
			prepared.setInt(1, cc_id);
			ResultSet resultSet = prepared.executeQuery(); 
			if (resultSet.next()){
				ac_id = resultSet.getInt("ac_id");
				name=resultSet.getString("cc_name");
				owner = resultSet.getString("owner_id");
				cc = new ContextContract();
				cc.setCcId(cc_id);
				cc.setCcName(name);
				cc.setOwnerId(Integer.parseInt(owner));
				cc.setAbstractComponent(AbstractComponentHandler.getAbstractComponent(ac_id));
				cc.getContextArguments().addAll(ContextArgumentHandler.getContextArguments(cc_id));
				cc.getInnerComponents().addAll(getInnerCompponents(cc_id));
				for(ContextArgumentType cat:cc.getContextArguments()){
					//Creating a pointer into context parameter to context argument
					for(ContextParameterType cpt : cc.getAbstractComponent().getContextParameter()){
						if(cpt.getCpId()==cat.getCpId()){
							cpt.setContextArgument(cat);
						}
					}
					//				end of pointer creation
					if(cat.getContextContract()!=null){
						completeContextContract(cat.getContextContract());
					}
				}
				if(cc.getCcId()!=null){
					cc.getConcreteUnits().addAll(ConcreteUnitHandler.getConcreteUnits(cc.getCcId()));
				}
				return cc;
			}else{
				throw new DBHandlerException("Context contract with id "+cc_id+" was not found");
			}
		} catch (SQLException e) {
			throw new DBHandlerException("GetContextContract sql error: ", e);
		} 
	}

	public static ContextContract getContextContractIncomplete(Integer cc_id) throws DBHandlerException {
		ContextContract cc = null;
		try { 
			Integer ac_id = null;
			String name = null;
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONTEXT_CONTRACT);
			prepared.setInt(1, cc_id);
			ResultSet resultSet = prepared.executeQuery();
			if (resultSet.next()){
				ac_id = resultSet.getInt("ac_id");
				name=resultSet.getString("cc_name");
				cc = new ContextContract();
				cc.setCcId(cc_id);
				cc.setCcName(name);
			}
			if(ac_id != null){
				cc.setAbstractComponent(AbstractComponentHandler.getAbstractComponentPartial(ac_id));
			}
		} catch (SQLException e) {
			throw new DBHandlerException("Context contract not found with cc_id "+cc_id, e);
		} 
		return cc;
	}

	/**
	 * This method gets an instantiation type name given its bound
	 * @param cc_id Instantiation type id
	 * @return Instantiation type name
	 * @throws SQLException 
	 * @throws DBHandlerException 
	 */

	public static String getContextContractName(int cc_id) throws DBHandlerException {
		try {
			Connection con = getConnection();  
			PreparedStatement prepared = con.prepareStatement(SELECT_CONTEXT_CONTRACT_NAME);
			prepared.setInt(1, cc_id);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				return resultSet.getString("cc_name"); 
			}else{
				throw new DBHandlerException("Context contract with id "+cc_id+" was not found");
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 

	}

	/**
	 * This method gets an instantiation type id
	 * @param name Instantiation type name
	 * @return Instantiation type id
	 * @throws SQLException 
	 * @throws DBHandlerException 
	 */

	public static int getContextContractID(String name) throws DBHandlerException{

		try {
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONTEXT_CONTRACT_ID); 
			prepared.setString(1, name);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				return resultSet.getInt("cc_id"); 
			}else{
				throw new DBHandlerException("Context contract with name = "+name+" was not fount");
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 

	}


	/**
	 * This method gets an instantiation type id
	 * @param name Instantiation type name
	 * @return Instantiation type id
	 * @throws SQLException 
	 */

	public static List<Integer> getContextContractByAcId(int ac_id) throws DBHandlerException{

		try {
			Connection con = getConnection(); 
			List<Integer> list = new ArrayList<Integer>();
			PreparedStatement prepared = con.prepareStatement(SELECT_CONTEXT_CONTRACT_BY_AC_ID); 
			prepared.setInt(1, ac_id);
			ResultSet resultSet = prepared.executeQuery(); 
			while(resultSet.next()) { 
				list.add(resultSet.getInt("cc_id"));
			}
			return list;
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}


	private static Integer addInnerCompponent(int parent_id, int inner_id) throws DBHandlerException{
		try {
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(INSERT_INNER_COMPONENT);
			prepared.setInt(1, parent_id);
			prepared.setInt(2, inner_id);
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				return result.getInt("id");
			}else{
				return null;
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}
	}

	private static List<ContextContract> getInnerCompponents(int cc_id) throws DBHandlerException{
		try {
			List<ContextContract> list = new ArrayList<>();
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(SELECT_INNER_COMPONENT);
			prepared.setInt(1, cc_id);
			ResultSet result = prepared.executeQuery();
			while(result.next()){
				list.add(ContextContractHandler.getContextContract(result.getInt("inner_cc_id")));
			}
			return list;
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}
	}



	public static ContractList listContract(int ac_id) throws DBHandlerException{
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
	 * @throws DBHandlerException 
	 */
	public static void completeContextContract(ContextContract application) throws DBHandlerException{
		application.setAbstractComponent(AbstractComponentHandler.getAbstractComponent(application.getAbstractComponent().getIdAc()));
		for(ContextArgumentType cat:application.getContextArguments()){
			if(cat.getContextContract()!=null){
				cat.getContextContract().setCcName(ContextContractHandler.getContextContractName(cat.getContextContract().getCcId()));
				AbstractComponentType ac = AbstractComponentHandler.getAbstractComponentFromContextContractID(cat.getContextContract().getCcId());
				cat.getContextContract().setAbstractComponent(ac);
				completeContextContract(cat.getContextContract());
				application.getAbstractComponent().getAbstractUnit().addAll(AbstractUnitHandler.getAbstractUnits(application.getAbstractComponent().getIdAc()));
			}
		}
		if(application.getPlatform()!=null){
			completeContextContract(application.getPlatform().getPlatformContract());
		}

	}
}
