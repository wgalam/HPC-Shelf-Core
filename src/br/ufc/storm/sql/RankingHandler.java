package br.ufc.storm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.jaxb.RankingFunctionTermType;
import br.ufc.storm.jaxb.RankingFunctionType;
import br.ufc.storm.jaxb.RankingParameterType;

public class RankingHandler extends DBHandler{

	private static final String SELECT_RANK_FUNCTION = "select * FROM ranking_function A, context_parameter B WHERE B.cp_id = ? AND A.cc_id = ?;";
	private static final String SELECT_RANK_FUNCTION_TERMS = "SELECT cp_id, term_order FROM ranking_function_terms WHERE rf_id = ?;";
	private static final String INSERT_RANK_FUNCTION = "INSERT INTO ranking_function (cp_id, function_name, function_value, cc_id) VALUES (?,?,?,?) RETURNING rf_id;";
	private static final String INSERT_RANK_PARAMETER = "INSERT INTO rank_function_terms (rf_id,term_order,cp_id) VALUES (?,?,?);";
	private static final String SELECT_RANK_PARAMETER = "select * FROM context_parameter WHERE ac_id = ? AND parameter_type = 4;";

	/**
	 * 
	 * @param rft
	 * @return
	 * @throws DBHandlerException 
	 */
	//TODO:Testar
	public static int addRankFunction(RankingFunctionType rft) throws DBHandlerException {
		int function_id = -1;
		try { 
			Connection con = DBHandler.getConnection(); 
			con.setAutoCommit(false);
			PreparedStatement prepared = con.prepareStatement(INSERT_RANK_FUNCTION); 
			prepared.setInt(1, rft.getCcId());
			prepared.setString(2, rft.getFunctionName()); 
			prepared.setString(3, rft.getFunctionValue()); 
			prepared.setInt(4, rft.getCcId());
			ResultSet result = prepared.executeQuery();
			if(result.next()){
				function_id = result.getInt("rf_id");
				//				if(function_id != -1){
				for(RankingFunctionTermType cp: rft.getFunctionParameters()){
					prepared = con.prepareStatement(INSERT_RANK_PARAMETER); 
					prepared.setInt(1, function_id); 
					prepared.setInt(2, cp.getOrder()); 
					prepared.setInt(3, cp.getCpId());
					prepared.execute();
				}
				con.commit();
				return function_id;
				//				}
			}else{
				throw new DBHandlerException("Can not insert rank function");
			}

		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e); 
		}

	}

	public static RankingFunctionType getRankingFunction(Integer cp_id, Integer cc_id) throws DBHandlerException {
		try { 
			Connection con = DBHandler.getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_RANK_FUNCTION);
			prepared.setInt(1, cp_id);
			prepared.setInt(2, cc_id);
			ResultSet resultSet = prepared.executeQuery();
			if(resultSet.next()) {
				RankingFunctionType rf = new RankingFunctionType();
				rf.setFunctionId(resultSet.getInt("rf_id"));
				rf.setFunctionName(resultSet.getString("function_name"));
				rf.setFunctionValue(resultSet.getString("function_value"));
				return rf;	
			}else{
//				throw new DBHandlerException("");
				return null;
			}
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		}	

	}

	public static ArrayList<RankingParameterType> getRankingParameters(int ac_id, Connection connection) throws DBHandlerException{

		try { 
			Connection con = DBHandler.getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_RANK_PARAMETER); 
			prepared.setInt(1, ac_id);
			ResultSet resultSet = prepared.executeQuery(); 
			ArrayList<RankingParameterType> rps= new ArrayList<RankingParameterType>();
			while(resultSet.next()){
				RankingParameterType qp = new RankingParameterType();
				qp.setRankId(resultSet.getInt("cp_id"));
				qp.setName(resultSet.getString("cp_name"));
				qp.setKindId(resultSet.getInt("kind_id"));
				rps.add(qp);
			}
			return rps;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<RankingFunctionTermType> getRankingFunctionParameter(Integer rf_id) throws DBHandlerException {
		try { 
			Connection con = DBHandler.getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_RANK_FUNCTION_TERMS); 
			prepared.setInt(1, rf_id);
			ResultSet resultSet = prepared.executeQuery();
			ArrayList<RankingFunctionTermType> rps= new ArrayList<RankingFunctionTermType>();
			while(resultSet.next()){ 
				RankingFunctionTermType qftt = new RankingFunctionTermType();
				qftt.setCpId(resultSet.getInt("cp_id"));
				qftt.setOrder(resultSet.getInt("term_order"));
				rps.add(qftt);
			}
			Collections.sort (rps, new ComparatorRankingTerms(true));
			return rps;
		} catch (SQLException e) { 
			throw new DBHandlerException("A sql error occurred: ", e);
		} 

	}
}

@SuppressWarnings("rawtypes")
class ComparatorRankingTerms implements Comparator {  
	boolean crescente = true;  

	public ComparatorRankingTerms(boolean crescente) {  
		this.crescente = crescente;  
	}  

	public int compare(Object o1, Object o2) {  
		RankingFunctionTermType p1 = (RankingFunctionTermType) o1;  
		RankingFunctionTermType p2 = (RankingFunctionTermType) o2;  
		if (crescente) {  
			return p1.getOrder() < p2.getOrder() ? -1 : (p1.getOrder() > p2.getOrder() ? +1 : 0);  
		} else {  
			return p1.getOrder() < p2.getOrder() ? +1 : (p1.getOrder() > p2.getOrder() ? -1 : 0);  
		}  
	}  
}  