package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextArgumentValueType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.exception.DBHandlerException;

public class ContextArgumentHandler extends DBHandler {

	private static final String INSERT_CONTEXT_ARGUMENT = "INSERT INTO context_argument (cc_id, cp_id) VALUES ((select cc_id from context_contract where cc_name=?),(select cp_id from context_parameter where cp_name=?));";
	private final static String SELECT_CONTEXT_ARGUMENT_BY_ID = "select * from context_argument A, context_parameter B where cc_id = ? and A.variable_cp_id = B.cp_id;";
	private static final String SELECT_VARIABLE_HAS_VALUE = "select * from context_argument WHERE variable_cp_id = ? AND cc_id = ?;";
	private static final String SELECT_VARIABLE_VALUE = "SELECT * from closed_argument_values WHERE ca_id = ?;";
	private static final String SELECT_SHARED_VARIABLE_CP = "select refers_to_var from context_parameter A, shared_context_variables B  WHERE A.cp_id = B.cp_id AND A.cp_id = ?;";
	private static final String SELECT_CLOSED_ARGUMENT_CC = "select cc_id from closed_arguments_context_contract WHERE ca_id = ?;";
	private static final String SELECT_VARIABLE_SHARED = "select * from context_parameter WHERE cp_id =?;";
	private static final String INSERT_CONTEXT_ARGUMENT_VALUE = "INSERT INTO closed_argument_values (ca_id, data_type, value) VALUES (?,?,?);";
	private static final String INSERT_CONTEXT_ARGUMENT_REFERENCE = "INSERT INTO shared_context_variables (refers_to_var, cp_id)VALUES (?,?);";
	private static final String INSERT_CONTEXT_ARGUMENT_CONTEXT_CONTRACT = "INSERT INTO closed_arguments_context_contract (ca_id, cc_id)VALUES (?,?);";
	

	/**
	 * 
	 * @param cp_id
	 * @param cc_id
	 * @return
	 */
	public static boolean hasContextArgumentAValue(int cp_id, int cc_id){
		boolean hasValue = false;
		Connection con = null; 
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_VARIABLE_HAS_VALUE); 
			prepared.setInt(1, cp_id);
			prepared.setInt(2, cc_id);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				hasValue = resultSet.getBoolean("hasValue"); 
			}
		} catch (SQLException e) { 
			e.printStackTrace();
			closeConnnection(con);
			return false;
		} finally { 
			closeConnnection(con); 
		} 
		return hasValue;
	}


	/**
	 * This method adds a context argument
	 * @param cc_name Instantiation type name 
	 * @param cp_name Context parameter name
	 * @param ca_value Context argument value
	 * @return True if well successful
	 */

	public static boolean addContextArgument(ContextArgumentType cat, Connection con){		
		try { 
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_ARGUMENT); 
			prepared.setInt(1, cat.getCcId()); 
			prepared.setInt(2, cat.getVariableCpId()); 
			if(cat.getValue()!=null){
				prepared.setBoolean(3, true);
			}else{
				prepared.setBoolean(3, false);
			}
			prepared.executeQuery(); 
			if(cat.getValue()!=null){
				addContextArgumentValue(cat.getCaId(),cat.getValue(), con);
				//				Adicionar valor na respectiva tabela
			}else{
				if(cat.getContextContract()!=null){
					//					TODO: Falta testar
					addContextArgumentContextContract(cat.getCaId(),cat.getContextContract(), con);
					//					Adicionar contrato na respectiva tabela
				}else{
					//					TODO: Falta testar
					addContextArgumentVariableReference(cat.getVariableCpId(),cat.getCpId(), con);
					//					Adicionar referência à variável
				}
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
		}
		return true;
	}

	//TODO: Testar
	private static boolean addContextArgumentVariableReference(Integer var_id, Integer cpId, Connection con) {
		try {
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_ARGUMENT_REFERENCE); 
			prepared.setInt(1, var_id);
			prepared.setInt(2, cpId);
			prepared.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	//	TODO: Testar
	private static void addContextArgumentContextContract(int ca_id, ContextContract contextContract, Connection con) {
		try {
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_ARGUMENT_CONTEXT_CONTRACT); 
			prepared.setInt(1, ca_id);
			prepared.setInt(2, contextContract.getCcId());
			prepared.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private static boolean addContextArgumentValue(int cat_id, ContextArgumentValueType cavt, Connection con) {
		try { 
			PreparedStatement prepared = con.prepareStatement(INSERT_CONTEXT_ARGUMENT_VALUE); 
			prepared.setInt(1, cat_id);
			prepared.setString(2, cavt.getDataType());
			prepared.setString(3, cavt.getValue());
			prepared.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;


		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @param cc_id
	 * @return
	 */
	public static ArrayList<ContextArgumentType> getContextArguments(int cc_id){
		Connection con = null; 
		ArrayList<ContextArgumentType> cpl = new ArrayList<ContextArgumentType>();
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CONTEXT_ARGUMENT_BY_ID); 
			prepared.setInt(1, cc_id);
			ResultSet resultSet = prepared.executeQuery(); 
			while (resultSet.next()) {
				ContextArgumentType ca = new ContextArgumentType();
				ca.setCcId(cc_id);
				ca.setVariableCpId(resultSet.getInt("variable_cp_id"));
				ca.setBoundCcId(resultSet.getInt("bound_id"));
				ca.setCaId(resultSet.getInt("ca_id"));
				ca.setKind(resultSet.getInt("kind_id"));
				Object o;
				try {
					o = getContextArgumentValue(ca.getVariableCpId(), ca.getCcId());

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
					}
				} catch (DBHandlerException e) {
					e.printStackTrace();
				}
				cpl.add(ca);
			} 
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return cpl; 
	}




	/**
	 * 
	 * @param cp_id
	 * @param cc_id
	 * @return
	 */

	public static Object getContextArgumentValue(int cp_id, int cc_id) throws DBHandlerException{
		boolean hasValue = false, shared = false;
		int ca_id = 0;
		Connection con = null; 
		try { 
			con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_VARIABLE_SHARED); 
			prepared.setInt(1, cp_id);
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()) { 
				shared = resultSet.getBoolean("isShared");
			}
			if(shared){
				prepared = con.prepareStatement(SELECT_SHARED_VARIABLE_CP); 
				prepared.setInt(1, cp_id);
				resultSet = prepared.executeQuery();
				if(resultSet.next()){
					Object o = getContextArgumentValue(resultSet.getInt("refers_to_var"),cc_id);
					closeConnnection(con);
					return o;
				}
			}
			prepared = con.prepareStatement(SELECT_VARIABLE_HAS_VALUE);
			prepared.setInt(1, cp_id);
			prepared.setInt(2, cc_id);
			resultSet = prepared.executeQuery();
			if(resultSet.next()){
				hasValue = resultSet.getBoolean("hasValue");
				ca_id = resultSet.getInt("ca_id");
			}
			if(hasValue){
				prepared = con.prepareStatement(SELECT_VARIABLE_VALUE); 
				prepared.setInt(1, ca_id);
				resultSet = prepared.executeQuery();
				if(resultSet.next()){
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
					closeConnnection(con);
					return s;
				}
			}else{
				prepared = con.prepareStatement(SELECT_CLOSED_ARGUMENT_CC); 
				prepared.setInt(1, ca_id);
				resultSet = prepared.executeQuery();
				if(resultSet.next()){
					return ContextContractHandler.getContextContract(resultSet.getInt("cc_id"));
				}
			}
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} finally { 
			closeConnnection(con); 
		} 
		return null;
	}


}
