package br.ufc.storm.sql;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.udojava.evalex.Expression;

import br.ufc.storm.control.Resolution;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.FunctionException;
import br.ufc.storm.io.LogHandler;
import br.ufc.storm.jaxb.CalculatedArgumentType;
import br.ufc.storm.jaxb.CalculatedFunctionTermType;
import br.ufc.storm.jaxb.CalculatedFunctionType;
import br.ufc.storm.jaxb.CalculatedParameterType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.model.ArgumentTable;
import br.ufc.storm.model.ResolutionNode;

public class CalculatedArgumentHandler extends DBHandler{
	private static final String INSERT_CALCULATED_FUNCTION = "INSERT INTO calculated_function (cc_id, function_value, cp_id, CF_TYPE) VALUES (?,?,?,?) RETURNING cf_id;";
	private static final String INSERT_CALCULATED_PARAMETER = "INSERT INTO calculated_function_terms(cf_id,term_order,cp_id) VALUES (?,?,?);";
	private static final String SELECT_CALCULATED_FUNCTION = "select * FROM calculated_function A, context_parameter B WHERE B.cp_id = ? AND A.cc_id = ? AND B.parameter_type = ? AND A.cp_id = B.cp_id;";
	private static final String SELECT_CALCULATED_FUNCTION_GENERIC = "select * FROM calculated_function A, context_parameter B WHERE B.cp_id = ? AND A.cc_id IS NULL AND B.parameter_type = ? AND A.cp_id = B.cp_id;";
	private static final String SELECT_CALCULATED_FUNCTION_TERMS = "SELECT cp_id, term_order FROM calculated_function_terms WHERE cf_id = ?;";
	private static final String SELECT_CALCULATED_PARAMETER = "select * FROM context_parameter WHERE ac_id = ? AND parameter_type = ?;"; 

	public static void main(String[] args) {
		//try {
			//			QualityFunctionType cft = getQualityFunction(1, 123);
			//			System.out.println(cft.getFunctionValue());
			
			/*CalculatedFunctionType q = new CalculatedFunctionType();
			q.setCpId(2);
			q.setCcId(123);
			q.setFunctionValue("V0-v1");


			CalculatedFunctionTermType qft = new CalculatedFunctionTermType();
			qft.setCpId(27);
			qft.setOrder(1);
			q.getFunctionParameters().add(qft);
			CalculatedFunctionTermType qft2 = new CalculatedFunctionTermType();
			qft2.setCpId(27);
			qft2.setOrder(2);
			q.getFunctionParameters().add(qft2);
			addCalculatedFunction(q, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
			try {
				Resolution.main(args);
			} catch (DBHandlerException e) {
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
			prepared.setString(2, qf.getFunctionValue()); 
			prepared.setInt(3, qf.getCpId());
			prepared.setInt(4, type);
			ResultSet resultSet = prepared.executeQuery();
			if(resultSet.next()){
				function_id = resultSet.getInt("cf_id");
				for(CalculatedFunctionTermType cp: qf.getFunctionParameters()){
					prepared = con.prepareStatement(INSERT_CALCULATED_PARAMETER); 
					prepared.setInt(1, function_id); 
					prepared.setInt(2, cp.getOrder()); 
					prepared.setInt(3, cp.getCpId());
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

	public static ArrayList<CalculatedParameterType> getCalculatedParameters(int ac_id, int type) throws DBHandlerException{

		try{	
			ArrayList<CalculatedParameterType> cps= new ArrayList<CalculatedParameterType>();
			Connection con = getConnection();
			PreparedStatement prepared = con.prepareStatement(SELECT_CALCULATED_PARAMETER);
			prepared.setInt(1, ac_id);
			prepared.setInt(2, type);
			ResultSet resultSet = prepared.executeQuery(); 
			while(resultSet.next()){ 
//				if(resultSet.getInt("cp_id") == 112){
//				System.out.println(resultSet.getString("cp_name"));
//				}
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
			if(cp_id == 112){
			System.out.println("XXXXXXXXXXXXXX");
		}
			Connection con = getConnection(); 
			PreparedStatement prepared = con.prepareStatement(SELECT_CALCULATED_FUNCTION);
			prepared.setInt(1, cp_id);
			prepared.setInt(2, cc_id);
			prepared.setInt(3, type);
			ResultSet resultSet = prepared.executeQuery();
			CalculatedFunctionType qf = null;
			if(resultSet.next()) {
				qf = new CalculatedFunctionType();
				qf.setFunctionId(resultSet.getInt("cf_id"));
				qf.setFunctionValue(resultSet.getString("function_value"));
				return qf;
			}else{
				prepared = con.prepareStatement(SELECT_CALCULATED_FUNCTION_GENERIC);
				prepared.setInt(1, cp_id);
				prepared.setInt(2, type);
				resultSet = prepared.executeQuery();
				if(resultSet.next()) {
					qf = new CalculatedFunctionType();
					qf.setFunctionId(resultSet.getInt("cf_id"));
					qf.setFunctionValue(resultSet.getString("function_value"));
					return qf;
				}
			}
			return qf;
		} catch (SQLException e) { 
			e.printStackTrace();
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
			if(cat.getCpId()==cp_id){
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
	
	public static int calulateContextContractArguments(ContextContract cc, ResolutionNode tree, ArgumentTable argTable,int type) throws FunctionException{
		//Calculates arguments contracts parameters before it own quality arguments
		
		int count = 0;
		for(ContextArgumentType cat : cc.getContextArguments()){
			if(cat.getKind()== 1 && cat.getContextContract()!=null){
				CalculatedArgumentHandler.calulateContextContractArguments(cat.getContextContract(), tree, argTable, type);
			}
		}
		List<CalculatedParameterType> calcps = null;
		switch (type) {
		case ContextParameterHandler.QUALITY:
			calcps= tree.findNode(cc.getAbstractComponent().getIdAc()).getQps();
			break;
		case ContextParameterHandler.COST:
			calcps= tree.findNode(cc.getAbstractComponent().getIdAc()).getCops();
			break;
		case ContextParameterHandler.RANKING:
			calcps= tree.findNode(cc.getAbstractComponent().getIdAc()).getRps();
			break;
		}
		//Begin of calculus
		for(CalculatedParameterType calcpt:calcps){//calcula cada parametro
			
			CalculatedFunctionType function = null;
			try {
				function = CalculatedArgumentHandler.getCalculatedFunction(calcpt.getCalcId(), cc.getCcId(), type);
			} catch (DBHandlerException e) {
				LogHandler.getLogger().info("Error while getting function. "+e.getCause());
			}

			if(function != null){
				ArrayList<CalculatedFunctionTermType> terms = null;
				try {
					terms = CalculatedArgumentHandler.getCalculatedFunctionParameter(function.getFunctionId());
				} catch (DBHandlerException e) {
					LogHandler.getLogger().info("Error while getting function. "+e.getCause());
					//throw new FunctionException("Can not get calculated function: ",e);
				}
				for(CalculatedFunctionTermType qftt: terms){//busca cada argumento de cada termo da função
					//ContextArgumentType cat = Resolution.getArgumentRecursive(cc, qftt.getCpId());
					ContextArgumentType cat = argTable.getArgument(qftt.getCpId());
					if(cat!=null){
						function.getFunctionArguments().add(cat);
					}
				}
				CalculatedArgumentType qat = new CalculatedArgumentType();
				qat.setCalcId(calcpt.getCalcId());
				qat.setValue(CalculatedArgumentHandler.calculate(function));
				argTable.addNewArgument(calcpt.getCalcId(), ""+qat.getValue());
				calcpt.setCalculatedArgument(qat);
				
				switch (type) {
				case ContextParameterHandler.QUALITY:
					cc.getQualityArguments().add(qat);
					count++;
					break;
				case ContextParameterHandler.COST:
					cc.getCostArguments().add(qat);
					count++;
					break;
				case ContextParameterHandler.RANKING:
					cc.getRankingArguments().add(qat);
					count++;
					break;
				}


			}
		}
		if(cc.getPlatform()!=null){
			count+=calulateContextContractArguments(cc.getPlatform().getPlatformContract(), tree, argTable, type);
		}
		return count;
	}


	/**
	 * Given a calculated function, this method evaluate the function
	 * @param qft
	 * @return result
	 */
	public static double calculate(CalculatedFunctionType qft){
		BigDecimal result = null;
		int numOfarguments = qft.getFunctionArguments().size();

		Expression expression = new Expression(qft.getFunctionValue());
		for(int i = 0; i < numOfarguments; i++){
			expression.with("v"+i, (qft.getFunctionArguments().get(i).getValue().getValue()));
		}
		
		expression.setPrecision(2);
		result = expression.eval();
		return result.doubleValue();
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