package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.text.TabExpander;

import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextArgumentValueType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.control.Resolution;
import br.ufc.storm.exception.DBHandlerException;

public class ContextArgumentHandler extends DBHandler {

	private static final String INSERT_CONTEXT_ARGUMENT = "INSERT INTO context_argument (cc_id, variable_cp_id) VALUES (?,?) RETURNING ca_id;";
	private final static String SELECT_CONTEXT_ARGUMENT_BY_ID = "select * from context_argument A, context_parameter B where cc_id = ? and A.variable_cp_id = B.cp_id ORDER BY variable_cp_id;";
	private static final String SELECT_VARIABLE_VALUE = "select * from context_argument A, closed_argument_values B WHERE A.variable_cp_id = ? AND A.cc_id = ? AND A.ca_id = B.ca_id;";
	private static final String SELECT_SHARED_VARIABLE_CP = "select refers_to_var from context_parameter A, shared_context_variables B  WHERE A.cp_id = B.cp_id AND A.cp_id = ?;";
	private static final String SELECT_CLOSED_ARGUMENT_CC = "select target_cc_id from context_argument A, closed_arguments_context_contract B WHERE A.variable_cp_id = ? AND A.cc_id = ? AND A.ca_id = B.ca_id;";
	private static final String SELECT_VARIABLE_SHARED = "select * from context_parameter A, context_variable_required B	WHERE A.cp_id = ? AND A.cp_id = B.cp_id;";
	private static final String INSERT_CONTEXT_ARGUMENT_VALUE = "INSERT INTO closed_argument_values (ca_id, data_type, value) VALUES (?,?,?);";
	private static final String INSERT_CONTEXT_ARGUMENT_REFERENCE = "INSERT INTO shared_context_variables (refers_to_var, cp_id)VALUES (?,?);";
	private static final String INSERT_CONTEXT_ARGUMENT_CONTEXT_CONTRACT = "INSERT INTO closed_arguments_context_contract (ca_id, target_cc_id)VALUES (?,?);";
	private static final String SELECT_CA_ID_CC_ID = "SELECT * FROM context_argument WHERE variable_cp_id = ? AND cc_id = ?;";

	public static void main(String[] args) {
		try {
			Resolution.main(args);
		} catch (DBHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method adds a context argument
	 * @param cc_name Instantiation type name 
	 * @param cp_name Context parameter name
	 * @param ca_value Context argument value
	 * @return True if well successful
	 * @throws SQLException 
	 */

	public static boolean addContextArgument(ContextArgumentType cat) throws DBHandlerException{
		
		if(exists(cat)){
			//in a far future it must update the argument
			return false;
		}
		try {
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_ARGUMENT); 
			prepared.setInt(1, cat.getCcId());
			prepared.setInt(2, cat.getContextParameter().getCpId()); 
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()){
				cat.setCaId(resultSet.getInt("ca_id"));
			}
			if(cat.getValue()!=null){
				addContextArgumentValue(cat.getCaId(),cat.getValue());
			}else{
				if(cat.getContextContract()!=null){
					addContextArgumentContextContract(cat.getCaId(),cat.getContextContract());
				}
			}
			return true;
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}
	
	public static boolean exists(ContextArgumentType ca) throws DBHandlerException{
		if(ca==null){
			throw new DBHandlerException("Context argument null");
		}else{
			if(ca.getContextParameter().getCpId()==null){
				throw new DBHandlerException("Context argument incomplete");
			}
		}
		try {
			Connection con = getConnection(); 
			PreparedStatement prepared;
			prepared = con.prepareStatement(SELECT_CA_ID_CC_ID);
			prepared.setInt(1, ca.getContextParameter().getCpId());
			prepared.setInt(2, ca.getCcId());
			ResultSet resultSet = prepared.executeQuery();
			int cont = 0;
			int id = 0;
			while(resultSet.next()){
				id = resultSet.getInt("ca_id");
				cont++;
			}
			if(cont==0){
				return false;
			}else{
				if(cont == 1){
					ca.setCaId(id);
					return true;
				}else{
					throw new DBHandlerException("Multiple context arguments where found: ");
				}
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}

	/**
	 * 
	 * @param var_id
	 * @param cpId
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	//TODO: Testar
	private static void addContextArgumentVariableReference(Integer var_id, Integer cpId) throws DBHandlerException {

		try {
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_ARGUMENT_REFERENCE); 
			prepared.setInt(1, var_id);
			prepared.setInt(2, cpId);
			prepared.executeQuery();
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}

	}

	/**
	 * 
	 * @param ca_id
	 * @param contextContract
	 * @param con
	 * @throws SQLException 
	 */
	
	//	TODO: Testar
	private static void addContextArgumentContextContract(int ca_id, ContextContract contextContract) throws DBHandlerException {
		try {
			PreparedStatement prepared = getConnection().prepareStatement(INSERT_CONTEXT_ARGUMENT_CONTEXT_CONTRACT); 
			prepared.setInt(1, ca_id);
			prepared.setInt(2, contextContract.getCcId());
			prepared.execute();
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}
	}

	/**
	 * 
	 * @param cat_id
	 * @param cavt
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	private static void addContextArgumentValue(int cat_id, ContextArgumentValueType cavt) throws DBHandlerException {

		try {
			PreparedStatement prepared = getConnection().prepareStatement(INSERT_CONTEXT_ARGUMENT_VALUE); 
			prepared.setInt(1, cat_id);
			prepared.setString(2, cavt.getDataType());
			prepared.setString(3, cavt.getValue());
			prepared.execute();
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}

	}

	/**
	 * 
	 * @param cc 
	 * @param cc_id
	 * @return
	 * @throws SQLException 
	 * @throws DBHandlerException 
	 */
	public static ArrayList<ContextArgumentType> getContextArguments(ContextContract cc,  int cc_id) throws DBHandlerException{
		if(cc.getAbstractComponent().getContextParameter()==null) {
			throw new DBHandlerException("getContextArguments: no context parameter");
		}
		Hashtable<Integer, ContextParameterType> cpTable = new Hashtable<Integer, ContextParameterType>();
		for(ContextParameterType cp:cc.getAbstractComponent().getContextParameter()) {
			cpTable.put(cp.getCpId(), cp);
			System.out.println("Added: cc= "+cc.getCcName()+cc.getCcId()+" cp_id:"+cp.getCpId());
		}
		//gerar tabela de cp
		//o add cp id será feito a partir da busca na tabela
		try {
			System.out.println("Buscando Contrato: "+cc.getCcName());
			Connection con = getConnection();
			ArrayList<ContextArgumentType> cpl = new ArrayList<ContextArgumentType>(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONTEXT_ARGUMENT_BY_ID);
			prepared.setInt(1, cc_id);
			System.out.println(prepared);
			ResultSet resultSet = prepared.executeQuery(); 
			
			if(true) {
				while (resultSet.next()) {
					ContextArgumentType ca = new ContextArgumentType();
					ca.setCcId(cc_id);
					System.out.println(prepared);
//					ca.setCpId();
//					ca.setContextParameter(cpTable.get(resultSet.getInt("variable_cp_id")));
					ca.setContextParameter(new ContextParameterType());
					ca.getContextParameter().setCpId(resultSet.getInt("variable_cp_id"));
					
					ca.setCaId(resultSet.getInt("ca_id"));
					ca.setKind(resultSet.getInt("variance_id"));
					Object o;
//					System.out.println("..."+cpTable.get(resultSet.getInt("variable_cp_id")));
					
//					if() {
//						
//					}
					
					//O problema está em buscar um parâmetro que é do ac 3
					System.out.println("Finding for: "+ca.getContextParameter().getCpId()+" caid "+ca.getCaId()+" ac "+cc.getAbstractComponent().getName());
					o = getContextArgumentValue(ca.getContextParameter().getCpId(), ca.getCcId());
					if(o instanceof ContextContract){
						ca.setContextContract((ContextContract) o);
					}else if(o instanceof String){
						ContextArgumentValueType value = new ContextArgumentValueType();
						value.setDataType("String");
						value.setValue((String)o);
						ca.setValue(value);
					}else if(o instanceof Double){
						ContextArgumentValueType value = new ContextArgumentValueType();
						value.setDataType("Double");
						value.setValue((String)o);
						ca.setValue(value);
					}else if(o instanceof Integer){
						ContextArgumentValueType value = new ContextArgumentValueType();
						value.setDataType("Integer");
						value.setValue((String)o);
						ca.setValue(value);
					}else if(o instanceof Float){
						ContextArgumentValueType value = new ContextArgumentValueType();
						value.setDataType("Float");
						value.setValue((String)o);
						ca.setValue(value);
					}else{
						throw new DBHandlerException("Context argument data type not recognized"); 
					}
					cpl.add(ca);
				} 
			}
			return cpl;
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}

	}

	/**
	 * 
	 * @param cp_id
	 * @param cc_id
	 * @return
	 * @throws SQLException 
	 */
	//TODO: Grade chance de estar errado o último teste, revisar e testar
	@SuppressWarnings("resource")
	public static Object getContextArgumentValue(int cp_id, int cc_id) throws DBHandlerException{
		try {
//			boolean hasValue = false;
			boolean shared = false;
			int ca_id = 0;
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(SELECT_VARIABLE_SHARED); 
			prepared.setInt(1, cp_id);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				prepared = con.prepareStatement(SELECT_SHARED_VARIABLE_CP); 
				prepared.setInt(1, cp_id);
				resultSet = prepared.executeQuery();
				if(resultSet.next()){
					return getContextArgumentValue(resultSet.getInt("refers_to_var"),cc_id);
				}else{
					throw new DBHandlerException("Shared variable not found with cpId equals to "+cp_id+"and context contract id equals to "+cc_id);
				}
			}else{
				prepared = con.prepareStatement(SELECT_VARIABLE_VALUE);
				prepared.setInt(1, cp_id);
				prepared.setInt(2, cc_id);
				resultSet = prepared.executeQuery();
				if(resultSet.next()){
					ca_id = resultSet.getInt("ca_id");
						Object o;
						String type = resultSet.getString("data_type");
						String s = resultSet.getString("value");
						switch (type) {
						case "Integer":
							o = Integer.parseInt(s);
							break;
						case "String":
							o = s;
							break;
						case "Float":
							o = Float.parseFloat(s);
							break;
						case "Double":
							o = Double.parseDouble(s);
							break;
						default:
							throw new DBHandlerException("Context argument value not recognized");
						}
						return s;
//					}
				}else{
					prepared = con.prepareStatement(SELECT_CLOSED_ARGUMENT_CC); 
					prepared.setInt(1, cp_id);
					prepared.setInt(2, cc_id);
					resultSet = prepared.executeQuery();
					if(resultSet.next()){
						return ContextContractHandler.getContextContract(resultSet.getInt("target_cc_id"));
					}else{
						throw new DBHandlerException("Can not get closed argument with cpId equals to "+cp_id+"and context contract id equals to "+cc_id);
					}
				}
			}
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}

	}
}
