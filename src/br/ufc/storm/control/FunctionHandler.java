package br.ufc.storm.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

import com.udojava.evalex.Expression;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.FunctionException;
import br.ufc.storm.jaxb.CalculatedArgumentType;
import br.ufc.storm.jaxb.CalculatedFunctionTermType;
import br.ufc.storm.jaxb.CalculatedFunctionType;
import br.ufc.storm.jaxb.CalculatedParameterType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextArgumentValueType;
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
import br.ufc.storm.model.ArgumentTable;
import br.ufc.storm.model.ResolutionNode;
import br.ufc.storm.sql.CalculatedArgumentHandler;
import br.ufc.storm.sql.CostHandler;
import br.ufc.storm.sql.QualityHandler;
import br.ufc.storm.sql.RankingHandler;

public class FunctionHandler {

	public static void main(String [] a){
		try {
			Resolution.main(a);
		} catch (DBHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FunctionHandler() {
		// TODO Auto-generated constructor stub
	}

	public static int calulateContextContractArguments(ContextContract cc, ResolutionNode tree, ArgumentTable argTable,int type) throws FunctionException{
		//Types: 1 - Quality | 2 - Cost | 3 - Ranking
		//Calculates arguments contracts parameters before it own quality arguments
		int count = 0;
		for(ContextArgumentType cat : cc.getContextArguments()){
			if(cat.getKind()== 1 && cat.getContextContract()!=null){
				FunctionHandler.calulateContextContractArguments(cat.getContextContract(), tree, argTable, type);
			}
		}
		List<CalculatedParameterType> calcps = null;
		switch (type) {
		case 1:
			calcps= tree.findNode(cc.getAbstractComponent().getIdAc()).getQps();
			break;
		case 2:
			calcps= tree.findNode(cc.getAbstractComponent().getIdAc()).getCops();
			break;
		case 3:
			calcps= tree.findNode(cc.getAbstractComponent().getIdAc()).getRps();
			break;
		}
		//Begin of calculus
		for(CalculatedParameterType calcpt:calcps){//calcula cada parametro de qualidade
			CalculatedFunctionType function;
			try {
				function = CalculatedArgumentHandler.getCalculatedFunction(calcpt.getCalcId(), cc.getCcId(), type);
				//System.out.println("calcula cada parametro. Função: "+function.getFunctionValue());
			} catch (DBHandlerException e) {
				//				System.out.println("Error while trying calculate function with id "+qpt.getQpId());
				throw new FunctionException("Can not get quality function: ",e);
			}

			if(function != null){
				//System.out.println("Carregou função");
				ArrayList<CalculatedFunctionTermType> terms;
				try {
					terms = CalculatedArgumentHandler.getCalculatedFunctionParameter(function.getFunctionId());
				} catch (DBHandlerException e) {
					//					System.out.println("Error while trying calculate function with id "+qpt.getQpId());
					throw new FunctionException("Can not get calculated function: ",e);
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
				qat.setValue(FunctionHandler.calculate(function));
				argTable.addNewArgument(calcpt.getCalcId(), ""+qat.getValue());
				calcpt.setCalculatedArgument(qat);
				switch (type) {
				case 1:
					cc.getQualityArguments().add(qat);
					count++;
					break;
				case 2:
					cc.getCostArguments().add(qat);
					count++;
					break;
				case 3:
					cc.getRankingArguments().add(qat);
					count++;
					break;
				}


			}
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
			//System.out.println(qft.getFunctionValue()+" ... "+qft.getFunctionArguments().get(0).getValue().getValue()+" ... "+qft.getFunctionArguments().get(1).getValue().getValue());
		}
		expression.setPrecision(2);
		result = expression.eval();
		//System.out.println("Calculated: "+result.doubleValue());
		return result.doubleValue();
	}

	/*
	public static void calulateContextContractQualityArguments(ContextContract cc, ResolutionNode tree, ArgumentTable args) throws FunctionException{
		//Calculates arguments contracts parameters before it own quality arguments
		for(ContextArgumentType cat : cc.getContextArguments()){
			if(cat.getKind()== 1){
				FunctionHandler.calulateContextContractQualityArguments(cat.getContextContract(), tree, args);
			}
		}
		//Begin of calculus
		List<QualityParameterType> qps= tree.findNode(cc.getAbstractComponent().getIdAc()).getQps();
		for(QualityParameterType qpt:qps){//calcula cada parametro de qualidade
			QualityFunctionType function;
			try {
				function = QualityHandler.getQualityFunction(qpt.getQpId(), cc.getCcId());
			} catch (DBHandlerException e) {
				//				System.out.println("Error while trying calculate function with id "+qpt.getQpId());
				throw new FunctionException("Can not get quality function: ",e);
			}
			if(function != null){
				ArrayList<QualityFunctionTermType> terms;
				try {
					terms = QualityHandler.getQualityFunctionParameter(function.getFunctionId());
				} catch (DBHandlerException e) {
					//					System.out.println("Error while trying calculate function with id "+qpt.getQpId());
					throw new FunctionException("Can not get quality function: ",e);
				}
				for(QualityFunctionTermType qftt: terms){//busca cada argumento de cada termo da função
					//ContextArgumentType cat = Resolution.getArgumentRecursive(cc, qftt.getCpId());
					ContextArgumentType cat = args.getArgument(qftt.getCpId());
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
	}*/



	/**
	 * Given a quality function, this method evaluate the function
	 * @param qft
	 * @return result
	 */
	/*public static double calculate(QualityFunctionType qft){
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
	}	*/

	/**
	 * This method is only for testing purpose
	 * @param eq
	 * @param args
	 * @return
	 */
	/*public static double calculate(String eq, ArrayList<String> args){
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
	 */
	/*	public static void calulateContextContractCostArguments(ContextContract cc, ResolutionNode tree) throws FunctionException {
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
			CostFunctionType function;
			try {
				function = CostHandler.getCostFunction(qpt.getCopId(), cc.getCcId());
			} catch (DBHandlerException e) {
				throw new FunctionException("Can not get cost function: ",e);
			}
			if(function != null){
				//						System.out.println("STORM=>FunctionHander:"+"Carregou uma função");
				ArrayList<CostFunctionTermType> terms;
				try {
					terms = CostHandler.getCostFunctionParameter(function.getFunctionId());
				} catch (DBHandlerException e) {
					throw new FunctionException("Can not get cost function: ",e);
				}
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
	 */
	/*
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
	 */
	/*
	public static void calulateContextContractRankingArguments(ContextContract cc, ResolutionNode tree) throws FunctionException {
		//Calculates arguments contracts parameters before it own quality arguments
		for(ContextArgumentType cat : cc.getContextArguments()){
			if(cat.getKind()== 3){
				FunctionHandler.calulateContextContractRankingArguments(cat.getContextContract(), tree);
			}
		}
		//Begin of calculus
		List<RankingParameterType> cps= tree.findNode(cc.getAbstractComponent().getIdAc()).getRps();
		for(RankingParameterType qpt:cps){//calcula cada parametro de qualidade
			RankingFunctionType function;
			try {
				function = RankingHandler.getRankingFunction(qpt.getRankId(), cc.getCcId());
			} catch (DBHandlerException e) {
				throw new FunctionException("Can not get ranking function: ",e);
			}
			if(function != null){
				ArrayList<RankingFunctionTermType> terms;
				try {
					terms = RankingHandler.getRankingFunctionParameter(function.getFunctionId());
				} catch (DBHandlerException e) {
					throw new FunctionException("Can not get ranking function term: ",e);
				}
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

	 */

}
