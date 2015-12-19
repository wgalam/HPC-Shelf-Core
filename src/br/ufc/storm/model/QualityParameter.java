package br.ufc.storm.model;
//package br.ufc.storm.Model;
//import java.util.ArrayList;
//import java.util.List;
//
//import br.ufc.storm.Control.DBHandler;
//import br.ufc.storm.Control.FunctionHandler;
//import br.ufc.storm.jaxb.*;
//
//public class QualityParameter {
//	private String function;
//	private List<ContextParameterType> cps; 
//	private ContextContract cc;
//	
//	public QualityParameter(String function, ContextContract cc, List<ContextParameterType> cps){
//		this.function = function;
//		this.cc = cc;
//		this.cps = cps;
//		
//	}
//	/**
//	 * This method calculates a quality parameter from a contract and a set of context parameters
//	 */
//	public double evaluateQualityParameter(){
//		ArrayList<String> args = new ArrayList<String>();
//		for(int i = 0; i < cps.size(); i++){
//			args.add(""+DBHandler.getContextArgumentValue(cc.getCcId(), cps.get(i).getCpId()));
//			function = function.replaceFirst("\\?", "v"+i);
//		}
//		return FunctionHandler.calculate(function,args);
//	}
//	
//	/**
//	 * @return the function
//	 */
//	public String getFunction() {
//		return function;
//	}
//	/**
//	 * @param function the function to set
//	 */
//	public void setFunction(String function) {
//		this.function = function;
//	}
//	/**
//	 * @return the cc
//	 */
//	public ContextContract getCc() {
//		return cc;
//	}
//	/**	
//	 * 
//	 * @param cc the cc to set
//	 */
//	public void setCc(ContextContract cc) {
//		this.cc = cc;
//	}
//	/**
//	 * @return the cps
//	 */
//	public List<ContextParameterType> getCps() {
//		return cps;
//	}
//	/**
//	 * @param cps the cps to set
//	 */
//	public void setCps(List<ContextParameterType> cps) {
//		this.cps = cps;
//	}
//	public double getValue() {
//		return evaluateQualityParameter();
//	}
//}
