package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.jaxb.CostFunctionTermType;
import br.ufc.storm.jaxb.CostFunctionType;
import br.ufc.storm.jaxb.CostParameterType;

public class CostHandler extends DBHandler {


	private static final String SELECT_COST_FUNCTION = "select * FROM cost_function A, context_parameter B WHERE B.cp_id = ? AND A.cc_id = ?;";
	private static final String SELECT_COST_FUNCTION_TERMS = "SELECT cp_id, term_order FROM cost_function_terms WHERE cof_id = ?;";
	private static final String INSERT_COST_FUNCTION = "INSERT INTO cost_function (cp_id, function_name, function_value, cc_id) VALUES (?,?,?,?) RETURNING cof_id;";
	private static final String INSERT_COST_PARAMETER = "INSERT INTO cost_function_terms (cof_id,term_order,cp_id) VALUES (?,?,?);";
	private static final String SELECT_COST_PARAMETER = "select * FROM context_parameter WHERE ac_id = ? AND parameter_type = 3;";

	//TODO:Testar
	public static int addCostFunction(CostFunctionType cft) throws DBHandlerException {
		int function_id = -1;
		try { 
			Connection con = getConnection(); 
			con.setAutoCommit(false);
			PreparedStatement prepared = con.prepareStatement(INSERT_COST_FUNCTION); 
			prepared.setInt(1, cft.getCcId());
			prepared.setString(2, cft.getFunctionName()); 
			prepared.setString(3, cft.getFunctionValue()); 
			prepared.setInt(4, cft.getCcId());
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				function_id = result.getInt("cof_id");
				if(function_id != -1){
					for(CostFunctionTermType cp: cft.getFunctionParameters()){
						prepared = con.prepareStatement(INSERT_COST_PARAMETER); 
						prepared.setInt(1, function_id); 
						prepared.setInt(2, cp.getOrder()); 
						prepared.setInt(3, cp.getCpId());
						prepared.execute();

					}
				}
				con.commit();
				return function_id;
			}else{
				throw new DBHandlerException("Can not add this cost function");
			}

		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 

	}

	public static CostFunctionType getCostFunction(Integer cp_id, Integer cc_id) throws DBHandlerException {

		try { 
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_COST_FUNCTION);
			prepared.setInt(1, cp_id);
			prepared.setInt(2, cc_id);
			ResultSet resultSet = prepared.executeQuery();
			if(resultSet.next()) {
				CostFunctionType cof = new CostFunctionType();
				cof.setFunctionId(resultSet.getInt("cof_id"));
				cof.setFunctionName(resultSet.getString("function_name"));
				cof.setFunctionValue(resultSet.getString("function_value"));
				return cof;	
			}else{
				throw new DBHandlerException("Cost function with cp_id = "+cp_id+" was not found");
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 		

	}

	@SuppressWarnings("unchecked")
	public static ArrayList<CostFunctionTermType> getCostFunctionParameter(Integer cof_id) throws DBHandlerException {
		try { 
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_COST_FUNCTION_TERMS); 
			prepared.setInt(1, cof_id);
			ResultSet resultSet = prepared.executeQuery(); 
			ArrayList<CostFunctionTermType> cps= new ArrayList<CostFunctionTermType>();
			while(resultSet.next()){
				CostFunctionTermType qftt = new CostFunctionTermType();
				qftt.setCpId(resultSet.getInt("cp_id"));
				qftt.setOrder(resultSet.getInt("term_order"));
				cps.add(qftt);
			}
			Collections.sort (cps, new ComparatorCostTerms(true));
			return cps;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		}	

	}

	public static ArrayList<CostParameterType> getCostParameters(int ac_id) throws DBHandlerException{
		try { 
			Connection con = getConnection(); 
			ArrayList<CostParameterType> cps= new ArrayList<CostParameterType>();
			PreparedStatement prepared = con.prepareStatement(SELECT_COST_PARAMETER); 
			prepared.setInt(1, ac_id);
			ResultSet resultSet = prepared.executeQuery(); 
			while(resultSet.next()){ 
				CostParameterType qp = new CostParameterType();
				qp.setCopId(resultSet.getInt("cp_id"));
				qp.setName(resultSet.getString("cp_name"));
				qp.setKindId(resultSet.getInt("kind_id"));
				cps.add(qp);
			}
			return cps;
		} catch (SQLException e) {
			throw new DBHandlerException("A sql error occurred: ", e);
		}
	}
}

@SuppressWarnings("rawtypes")
class ComparatorCostTerms implements Comparator {  
	boolean crescente = true;  

	public ComparatorCostTerms(boolean crescente) {  
		this.crescente = crescente;  
	}  

	public int compare(Object o1, Object o2) {  
		CostFunctionTermType p1 = (CostFunctionTermType) o1;  
		CostFunctionTermType p2 = (CostFunctionTermType) o2;  
		if (crescente) {  
			return p1.getOrder() < p2.getOrder() ? -1 : (p1.getOrder() > p2.getOrder() ? +1 : 0);  
		} else {  
			return p1.getOrder() < p2.getOrder() ? +1 : (p1.getOrder() > p2.getOrder() ? -1 : 0);  
		}  
	}  
}  