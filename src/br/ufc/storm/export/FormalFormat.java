package br.ufc.storm.export;

import br.ufc.storm.control.Resolution;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.ResolveException;
import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.CalculatedArgumentType;
import br.ufc.storm.jaxb.CalculatedParameterType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.model.ResolutionNode;
import br.ufc.storm.sql.AbstractComponentHandler;
import br.ufc.storm.sql.ContextContractHandler;
import br.ufc.storm.xml.XMLHandler;

public class FormalFormat {

	public static void main(String[] args) {
		try {
			/*
			ContextContract cc = ContextContractHandler.getContextContract(21);
			System.out.println(FormalFormat.exportContextContract(cc, null));
			*/
			System.out.println(FormalFormat.exportComponentSignature(AbstractComponentHandler.getAbstractComponent(5), null));
					//System.out.println(XMLHandler.getAbstractComponent("Cluster"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String exportComponentSignature(AbstractComponentType ac, String space){
		try {
			ResolutionNode.setup();
		} catch (ResolveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String str = "";
		if(space == null){
			space="    ";
		}else{
			space+="    ";
		}
		str+=ac.getName()+"=[";
		for(ContextParameterType cp: ac.getContextParameter()){
			if(cp.getBound()!= null){
				try {
					if(cp.getBound().getCcName().equals("DATA")){
						str+="\n"+space+cp.getName()+" : "+cp.getBound().getCcName()+",";
					}else{
						str+="\n"+space+cp.getName()+" : "+cp.getBound().getCcName()+" ["+exportComponentSignature(AbstractComponentHandler.getAbstractComponentFromContextContractID(cp.getBound().getCcId()), space)+"],";
					}
				} catch (DBHandlerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				str+="\n"+space+cp.getName()+" : "+cp.getBoundValue()+",";
			}
		}
		for(CalculatedParameterType cp: ac.getQualityParameters()){
			str+="\n"+space+cp.getName()+" : DATA(Quality)";
		}
		for(CalculatedParameterType cp: ac.getCostParameters()){
			str+="\n"+space+cp.getName()+" : DATA(Cost)";
		}
		for(CalculatedParameterType cp: ac.getRankingParameters()){
			str+="\n"+space+cp.getName()+" : DATA(Ranking)";
		}
		str+="]";
		return str;
	}

	
	
	
	

	public static String exportContextContract(ContextContract cc, String space){
		if(space == null){
			try {
				ContextContractHandler.completeContextContract(cc);
			} catch (DBHandlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			space="    ";
		}else{
			space+="    ";
		}
		String str = "";
		AbstractComponentType ac = cc.getAbstractComponent();
		
		ResolutionNode r = ResolutionNode.resolutionTree.findNode(ac.getIdAc());
		ac.getQualityParameters().clear();
		ac.getQualityParameters().addAll(r.getQps());
		ac.getCostParameters().clear();
		ac.getCostParameters().addAll(r.getCops());
		ac.getRankingParameters().clear();
		ac.getRankingParameters().addAll(r.getRps());
		
		/*if(ac.getIdAc()==161){
			System.out.println(ac.getName()+" >> "+cc.getAbstractComponent().getQualityParameters().size());
		}*/
		
		
		
		str+=cc.getCcName()+"[";
		for(ContextParameterType cp: ac.getContextParameter()){
			for(ContextArgumentType ca : cc.getContextArguments()){
				if(cp.getCpId()==ca.getCpId()){
					if(ca.getContextContract()!=null){
						str+="\n"+space+cp.getName()+" = "+exportContextContract(ca.getContextContract(),"    ")+",";
					}else{
						if(ca.getValue()!= null){
							str+="\n"+space+cp.getName()+" = "+ca.getValue().getValue()+",";
						}else{
							if(ca.getCpId()!= null){
								//Definir comportamento
							}
						}
					}
				}
			}
		}
		for(CalculatedParameterType cp: ac.getQualityParameters()){
//			str+="\n"+space+cp.getName()+" = ";
			for(CalculatedArgumentType c : cc.getQualityArguments()){
				if(c.getCalcId()==cp.getCalcId()){
					str+="\n"+space+cp.getName()+" = "+c.getValue();//terminar
				}
			}
		}
		for(CalculatedParameterType cp: ac.getCostParameters()){
//			str+="\n"+space+cp.getName()+" = ";
			for(CalculatedArgumentType c : cc.getCostArguments()){
				if(c.getCalcId()==cp.getCalcId()){
					str+="\n"+space+cp.getName()+" = "+c.getValue();//terminar
				}
			}
		}
		for(CalculatedParameterType cp: ac.getRankingParameters()){
//			str+="\n"+space+cp.getName()+" = ";
			for(CalculatedArgumentType c : cc.getRankingArguments()){
				if(c.getCalcId()==cp.getCalcId()){
					str+="\n"+space+cp.getName()+" = "+c.getValue();//terminar
				}
			}
		}
		if(cc.getPlatform()!=null){
			str+="\n"+space+"PLATFORM: = "+exportContextContract(cc.getPlatform().getPlatformContract(),null)+",";
		}
		str+="]";
		return str;
	}
}
