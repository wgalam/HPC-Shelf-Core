package br.ufc.storm.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.CostArgumentType;
import br.ufc.storm.jaxb.CostFunctionTermType;
import br.ufc.storm.jaxb.CostFunctionType;
import br.ufc.storm.jaxb.CostParameterType;
import br.ufc.storm.jaxb.QualityArgumentType;
import br.ufc.storm.jaxb.QualityFunctionTermType;
import br.ufc.storm.jaxb.QualityFunctionType;
import br.ufc.storm.jaxb.QualityParameterType;
import br.ufc.storm.jaxb.RankingArgumentType;
import br.ufc.storm.jaxb.RankingFunctionTermType;
import br.ufc.storm.jaxb.RankingFunctionType;
import br.ufc.storm.jaxb.RankingParameterType;
import br.ufc.storm.model.ResolutionNode;
import br.ufc.storm.sql.CostHandler;
import br.ufc.storm.sql.DBHandler;
import br.ufc.storm.sql.QualityHandler;
import br.ufc.storm.sql.RankingHandler;

import com.udojava.evalex.Expression;

public class FunctionHandler {

	public FunctionHandler() {
		// TODO Auto-generated constructor stub
	}	
	public static void calulateContextContractQualityArguments(ContextContract cc, ResolutionNode tree){
		//Calculates arguments contracts parameters before it own quality arguments
		for(ContextArgumentType cat : cc.getContextArguments()){
			if(cat.getKind()== 1){
				FunctionHandler.calulateContextContractQualityArguments(cat.getContextContract(), tree);
			}
		}
		//Begin of calculus
		List<QualityParameterType> qps= tree.findNode(cc.getAbstractComponent().getIdAc()).getQps();
		for(QualityParameterType qpt:qps){//calcula cada parametro de qualidade
			QualityFunctionType function = QualityHandler.getQualityFunction(qpt.getQpId(), cc.getCcId());
			if(function != null){
				ArrayList<QualityFunctionTermType> terms = QualityHandler.getQualityFunctionParameter(function.getFunctionId());
				for(QualityFunctionTermType qftt: terms){//busca cada argumento de cada termo da função
					ContextArgumentType cat = Resolution.getArgumentRecursive(cc, qftt.getCpId());
					if(cat!=null){
						function.getFunctionArguments().add(cat);
					}
				}
				QualityArgumentType qat = new QualityArgumentType();
				qat.setValue(FunctionHandler.calculate(function));
				qpt.setQualityArgument(qat);
				cc.getQualityArguments().add(qat);
			}
		}
	}

	/**
	 * Given a quality function, this method evaluate the function
	 * @param qft
	 * @return result
	 */
	public static double calculate(QualityFunctionType qft){
		BigDecimal result = null;
		int numOfarguments = qft.getFunctionArguments().size();
		//				System.out.println(qft.getFunctionValue()+" ... "+qft.getFunctionArguments().get(0).getValue()+" ... "+qft.getFunctionArguments().get(1).getValue());
		Expression expression = new Expression(qft.getFunctionValue());
		for(int i = 0; i < numOfarguments; i++){
			expression.with("v"+i, qft.getFunctionArguments().get(i).getValue().getValue());
		}
		expression.setPrecision(2);
		result = expression.eval();
		return result.doubleValue();
	}	

	/**
	 * This method is only for testing purpose
	 * @param eq
	 * @param args
	 * @return
	 */
	public static double calculate(String eq, ArrayList<String> args){
		BigDecimal result = null;
		int numOfarguments = args.size();
		Expression expression = new Expression(eq);
		for(int i = 0; i < numOfarguments; i++){
			expression.with("v"+i, args.get(i));
		}
		expression.setPrecision(2);
		result = expression.eval();
		return result.doubleValue();
	}
	
	public static void calulateContextContractCostArguments(ContextContract cc, ResolutionNode tree) {
		//Calculates arguments contracts parameters before it own quality arguments
				for(ContextArgumentType cat : cc.getContextArguments()){
					if(cat.getKind()== 2){
						FunctionHandler.calulateContextContractCostArguments(cat.getContextContract(), tree);
					}
				}
				//Begin of calculus
//				System.out.println("STORM=>FunctionHander:"+"Começou o cálculo de parâmetro de custo");
				List<CostParameterType> cps= tree.findNode(cc.getAbstractComponent().getIdAc()).getCops();
//				System.out.println("STORM=>FunctionHander:"+"Começou o cálculo de parâmetro de custo");
				for(CostParameterType qpt:cps){//calcula cada parametro de qualidade
					CostFunctionType function = CostHandler.getCostFunction(qpt.getCopId(), cc.getCcId());
					if(function != null){
//						System.out.println("STORM=>FunctionHander:"+"Carregou uma função");
						ArrayList<CostFunctionTermType> terms = CostHandler.getCostFunctionParameter(function.getFunctionId());
						for(CostFunctionTermType qftt: terms){//busca cada argumento de cada termo da função
							ContextArgumentType cat = Resolution.getArgumentRecursive(cc, qftt.getCpId());
							if(cat!=null){
								function.getFunctionArguments().add(cat);
							}
						}
//						System.out.println("STORM=>FunctionHander:"+"Carregou "+terms.size()+" termos");
						CostArgumentType qat = new CostArgumentType();
						qat.setValue(FunctionHandler.calculate(function));
//						System.out.println("STORM=>FunctionHander:"+"Valor calculado: " + qat.getValue());
						qpt.setCostArgument(qat);
						cc.getCostArguments().add(qat);
					}
				}
	}
	private static Double calculate(CostFunctionType function) {
		BigDecimal result = null;
		int numOfarguments = function.getFunctionArguments().size();
		//				System.out.println(qft.getFunctionValue()+" ... "+qft.getFunctionArguments().get(0).getValue()+" ... "+qft.getFunctionArguments().get(1).getValue());
		Expression expression = new Expression(function.getFunctionValue());
		for(int i = 0; i < numOfarguments; i++){
			expression.with("v"+i, function.getFunctionArguments().get(i).getValue().getValue());
		}
		expression.setPrecision(2);
		result = expression.eval();
		return result.doubleValue();
	}	

	public static void calulateContextContractRankingArguments(ContextContract cc, ResolutionNode tree) {
		//Calculates arguments contracts parameters before it own quality arguments
				for(ContextArgumentType cat : cc.getContextArguments()){
					if(cat.getKind()== 3){
						FunctionHandler.calulateContextContractRankingArguments(cat.getContextContract(), tree);
					}
				}
				//Begin of calculus
				List<RankingParameterType> cps= tree.findNode(cc.getAbstractComponent().getIdAc()).getRps();
				for(RankingParameterType qpt:cps){//calcula cada parametro de qualidade
					RankingFunctionType function = RankingHandler.getRankingFunction(qpt.getRankId(), cc.getCcId());
					if(function != null){
						ArrayList<RankingFunctionTermType> terms = RankingHandler.getRankingFunctionParameter(function.getFunctionId());
						for(RankingFunctionTermType qftt: terms){//busca cada argumento de cada termo da função
							ContextArgumentType cat = Resolution.getArgumentRecursive(cc, qftt.getCpId());
							if(cat!=null){
								function.getFunctionArguments().add(cat);
							}
						}
						RankingArgumentType qat = new RankingArgumentType();
						qat.setValue(FunctionHandler.calculate(function));
						qpt.setRankingArgument(qat);
						cc.getRankingArguments().add(qat);
					}
				}
	}
	
	private static Double calculate(RankingFunctionType function) {
		BigDecimal result = null;
		int numOfarguments = function.getFunctionArguments().size();
		//				System.out.println(qft.getFunctionValue()+" ... "+qft.getFunctionArguments().get(0).getValue()+" ... "+qft.getFunctionArguments().get(1).getValue());
		Expression expression = new Expression(function.getFunctionValue());
		for(int i = 0; i < numOfarguments; i++){
			expression.with("v"+i, function.getFunctionArguments().get(i).getValue().getValue());
		}
		expression.setPrecision(2);
		result = expression.eval();
		return result.doubleValue();
	}
	


}
