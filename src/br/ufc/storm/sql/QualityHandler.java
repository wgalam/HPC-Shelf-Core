package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.jaxb.QualityFunctionTermType;
import br.ufc.storm.jaxb.QualityFunctionType;
import br.ufc.storm.jaxb.QualityParameterType;

public class QualityHandler extends DBHandler{
	private static final String INSERT_QUALITY_FUNCTION = "INSERT INTO quality_function (cp_id, function_name, function_value, cc_id) VALUES (?,?,?,?);";
	private static final String INSERT_QUALITY_PARAMETER = "INSERT INTO quality_function_terms(qf_id,term_order,cp_id) VALUES (?,?,?);";
	private static final String SELECT_QUALITY_FUNCTION = "select * FROM quality_function A, context_parameter B WHERE B.cp_id = ? AND A.cc_id = ?;";
	private static final String SELECT_QUALITY_FUNCTION_ID = "select qf_id FROM quality_function WHERE function_name = ?;";
	private static final String SELECT_QUALITY_FUNCTION_TERMS = "SELECT cp_id, term_order FROM quality_function_terms WHERE qf_id = ?;";
	private static final String SELECT_QUALITY_PARAMETER = "select * FROM context_parameter WHERE ac_id = ? AND parameter_type = 2;"; 
	/**
	 * 
	 * @return
	 * @throws DBHandlerException 
	 */
	public static int addQualityFunction(QualityFunctionType qf) throws DBHandlerException{
		int function_id = 0;
		try { 
			Connection con = getConnection(); 
			con.setAutoCommit(false);
			PreparedStatement prepared = con.prepareStatement(INSERT_QUALITY_FUNCTION); 
			prepared.setInt(1, qf.getCcId());
			prepared.setString(2, qf.getFunctionName()); 
			prepared.setString(3, qf.getFunctionValue()); 
			prepared.setInt(4, qf.getCcId());
			prepared.execute();
			prepared = con.prepareStatement(SELECT_QUALITY_FUNCTION_ID);
			prepared.setString(1, qf.getFunctionName());
			ResultSet resultSet = prepared.executeQuery(); 
			if(resultSet.next()){
				function_id = resultSet.getInt("qf_id");
				//				if(function_id != -1){
				for(QualityFunctionTermType cp: qf.getFunctionParameters()){
					prepared = con.prepareStatement(INSERT_QUALITY_PARAMETER); 
					prepared.setInt(1, function_id); 
					prepared.setInt(2, cp.getOrder()); 
					prepared.setInt(3, cp.getCpId());
					prepared.execute();
				}
				//				}
				con.commit();
				return function_id;	
			}else{
				throw new DBHandlerException("Quality function can not be created, error trying retrieve qf id");
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		} 

	}


	/**
	 * 
	 * @param ac_id
	 * @param connection
	 * @return
	 * @throws DBHandlerException
	 */

	public static ArrayList<QualityParameterType> getQualityParameters(int ac_id) throws DBHandlerException{
		try{	
			ArrayList<QualityParameterType> cps= new ArrayList<QualityParameterType>();
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(SELECT_QUALITY_PARAMETER); 
			prepared.setInt(1, ac_id);
			ResultSet resultSet = prepared.executeQuery(); 
			while(resultSet.next()){ 
				QualityParameterType qp = new QualityParameterType();
				qp.setQpId(resultSet.getInt("cp_id"));
				qp.setName(resultSet.getString("cp_name"));
				qp.setKindId(resultSet.getInt("kind_id"));
				cps.add(qp);
			}
			return cps;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		} 	
	}

	/**
	 * 
	 * @return
	 * @throws DBHandlerException 
	 */
	public static QualityFunctionType getQualityFunction(int cp_id, int cc_id) throws DBHandlerException{
		try { 
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_QUALITY_FUNCTION);
			prepared.setInt(1, cp_id);
			prepared.setInt(2, cc_id);
			ResultSet resultSet = prepared.executeQuery();
			if(resultSet.next()) {
				QualityFunctionType qf = new QualityFunctionType();
				qf.setFunctionId(resultSet.getInt("qf_id"));
				qf.setFunctionName(resultSet.getString("function_name"));
				qf.setFunctionValue(resultSet.getString("function_value"));
				return qf;
			}else{
				throw new DBHandlerException("Quality function not found with cp_id = "+cp_id);
			}

		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		}		

	}
	/**
	 * 
	 * @param qf_id
	 * @return
	 * @throws DBHandlerException
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<QualityFunctionTermType> getQualityFunctionParameter(int qf_id) throws DBHandlerException{
		ArrayList<QualityFunctionTermType> cps= new ArrayList<QualityFunctionTermType>();
		try { 
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_QUALITY_FUNCTION_TERMS); 
			prepared.setInt(1, qf_id);
			ResultSet resultSet = prepared.executeQuery(); 
			while(resultSet.next()){ 
				QualityFunctionTermType qftt = new QualityFunctionTermType();
				qftt.setCpId(resultSet.getInt("cp_id"));
				qftt.setOrder(resultSet.getInt("term_order"));
				cps.add(qftt);
			}
			Collections.sort (cps, new ComparatorQualityTerms(true));
			return cps;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: "+e.getMessage());
		}	
	}
}

@SuppressWarnings("rawtypes")
class ComparatorQualityTerms implements Comparator {  
	boolean crescente = true;  

	public ComparatorQualityTerms(boolean crescente) {  
		this.crescente = crescente;  
	}  

	public int compare(Object o1, Object o2) {  
		QualityFunctionTermType p1 = (QualityFunctionTermType) o1;  
		QualityFunctionTermType p2 = (QualityFunctionTermType) o2;  
		if (crescente) {  
			return p1.getOrder() < p2.getOrder() ? -1 : (p1.getOrder() > p2.getOrder() ? +1 : 0);  
		} else {  
			return p1.getOrder() < p2.getOrder() ? +1 : (p1.getOrder() > p2.getOrder() ? -1 : 0);  
		}  
	}  
}  