package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.jaxb.CalculatedFunctionTermType;
import br.ufc.storm.jaxb.CalculatedFunctionType;
import br.ufc.storm.jaxb.CalculatedParameterType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.QualityFunctionTermType;
import br.ufc.storm.jaxb.QualityFunctionType;
import br.ufc.storm.jaxb.QualityParameterType;

public class CalculatedArgumentHandler extends DBHandler{
	private static final String INSERT_CALCULATED_FUNCTION = "INSERT INTO calculated_function (cc_id, function_name, function_value, cp_id, CF_TYPE) VALUES (?,?,?,?,?) RETURNING cf_id;";
	private static final String INSERT_CALCULATED_PARAMETER = "INSERT INTO calculated_function_terms(cf_id,term_order,cp_id) VALUES (?,?,?);";
	private static final String SELECT_CALCULATED_FUNCTION = "select * FROM calculated_function A, context_parameter B WHERE B.cp_id = ? AND A.cc_id = ? AND A.cf_type = ?;";
	private static final String SELECT_CALCULATED_FUNCTION_TERMS = "SELECT cp_id, term_order FROM calculated_function_terms WHERE cf_id = ?;";
	private static final String SELECT_CALCULATED_PARAMETER = "select * FROM context_parameter WHERE ac_id = ? AND parameter_type = 2;"; 

	public static void main(String[] args) {
		try {
			//			QualityFunctionType cft = getQualityFunction(1, 123);
			//			System.out.println(cft.getFunctionValue());
			CalculatedFunctionType q = new CalculatedFunctionType();
			q.setCpId(2);
			q.setCcId(123);
			q.setFunctionName("wagner");
			q.setFunctionValue("V0-v1");


			CalculatedFunctionTermType qft = new CalculatedFunctionTermType();
			qft.setCpId(27);
			qft.setOrder(1);
			q.getFunctionParameters().add(qft);
			CalculatedFunctionTermType qft2 = new CalculatedFunctionTermType();
			qft2.setCpId(27);
			qft2.setOrder(2);
			q.getFunctionParameters().add(qft2);
			System.out.println(addCalculatedFunction(q, 1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 * @throws DBHandlerException 
	 */
	public static int addCalculatedFunction(CalculatedFunctionType qf, int type) throws DBHandlerException{
		int function_id = 0;
		try {
			Connection con = getConnection(); 
			con.setAutoCommit(false);
			PreparedStatement prepared = con.prepareStatement(INSERT_CALCULATED_FUNCTION); 
			prepared.setInt(1, qf.getCcId());
			prepared.setString(2, qf.getFunctionName()); 
			prepared.setString(3, qf.getFunctionValue()); 
			prepared.setInt(4, qf.getCpId());
			prepared.setInt(5, type);
			System.out.println(prepared);
			ResultSet resultSet = prepared.executeQuery();
			if(resultSet.next()){
				function_id = resultSet.getInt("cf_id");
				for(CalculatedFunctionTermType cp: qf.getFunctionParameters()){
					prepared = con.prepareStatement(INSERT_CALCULATED_PARAMETER); 
					prepared.setInt(1, function_id); 
					prepared.setInt(2, cp.getOrder()); 
					prepared.setInt(3, cp.getCpId());
					System.out.println(prepared);
					prepared.execute();
				}
				con.commit();
				return function_id;	
			}else{
				throw new DBHandlerException("Quality function can not be created, error trying retrieve qf id");
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 

	}


	/**
	 * 
	 * @param ac_id
	 * @param connection
	 * @return
	 * @throws DBHandlerException
	 */

	public static ArrayList<CalculatedParameterType> getCalculatedParameters(int ac_id) throws DBHandlerException{
		try{	
			ArrayList<CalculatedParameterType> cps= new ArrayList<CalculatedParameterType>();
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(SELECT_CALCULATED_PARAMETER); 
			prepared.setInt(1, ac_id);
			ResultSet resultSet = prepared.executeQuery(); 
			while(resultSet.next()){ 
				CalculatedParameterType calcp = new CalculatedParameterType();
				calcp.setCalcId(resultSet.getInt("cp_id"));
				calcp.setName(resultSet.getString("cp_name"));
				calcp.setKindId(resultSet.getInt("kind_id"));
				cps.add(calcp);
			}
			return cps;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 	
	}

	/**
	 * 
	 * @return
	 * @throws DBHandlerException 
	 */
	public static CalculatedFunctionType getCalculatedFunction(int cp_id, int cc_id, int type) throws DBHandlerException{
		try { 
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CALCULATED_FUNCTION);
			prepared.setInt(1, cp_id);
			prepared.setInt(2, cc_id);
			prepared.setInt(3, type);
			ResultSet resultSet = prepared.executeQuery();
			if(resultSet.next()) {
				CalculatedFunctionType qf = new CalculatedFunctionType();
				qf.setFunctionId(resultSet.getInt("cf_id"));
				qf.setFunctionName(resultSet.getString("function_name"));
				qf.setFunctionValue(resultSet.getString("function_value"));
				return qf;
			}else{
				return null;
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		}		

	}
	/**
	 * 
	 * @param cf_id
	 * @return
	 * @throws DBHandlerException
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<CalculatedFunctionTermType> getCalculatedFunctionParameter(int cf_id) throws DBHandlerException{
		ArrayList<CalculatedFunctionTermType> cps= new ArrayList<CalculatedFunctionTermType>();
		try { 
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CALCULATED_FUNCTION_TERMS); 
			prepared.setInt(1, cf_id);
			ResultSet resultSet = prepared.executeQuery(); 
			while(resultSet.next()){ 
				CalculatedFunctionTermType cftt = new CalculatedFunctionTermType();
				cftt.setCpId(resultSet.getInt("cp_id"));
				cftt.setOrder(resultSet.getInt("term_order"));
				cps.add(cftt);
			}
			Collections.sort (cps, new ComparatorCalculatedTerms(true));
			return cps;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		}	
	}

	public static Object getContractArgument(int cp_id, ContextContract cc){
		for(ContextArgumentType cat : cc.getContextArguments()){
			if(cat.getVariableCpId()==cp_id){
				return cat.getValue();
			}else{
				if(cat.getContextContract()!=null){
					return getContractArgument(cp_id, cat.getContextContract());
				}else{
					//variavel compartilhada
				}
			}
		}
		return null;
	}
}



@SuppressWarnings("rawtypes")
class ComparatorCalculatedTerms implements Comparator {  
	boolean crescente = true;  

	public ComparatorCalculatedTerms(boolean crescente) {  
		this.crescente = crescente;  
	}  

	public int compare(Object o1, Object o2) {  
		CalculatedFunctionTermType p1 = (CalculatedFunctionTermType) o1;  
		CalculatedFunctionTermType p2 = (CalculatedFunctionTermType) o2;  
		if (crescente) {  
			return p1.getOrder() < p2.getOrder() ? -1 : (p1.getOrder() > p2.getOrder() ? +1 : 0);  
		} else {  
			return p1.getOrder() < p2.getOrder() ? +1 : (p1.getOrder() > p2.getOrder() ? -1 : 0);  
		}  
	}  
}  