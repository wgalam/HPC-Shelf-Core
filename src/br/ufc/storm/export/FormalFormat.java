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
	
public static final String PROVIDED = " :> ";
public static final String REQUIRED = " <: ";

	public static void main(String[] args) {
		try {
			int ac_id = 19;
			
			ContextContract cc = ContextContractHandler.getContextContract(318);
			System.out.println(FormalFormat.exportContextContract(cc, null));
//			System.out.println(FormalFormat.exportContextContractWithIDs(cc, null));
//			System.out.println(XMLHandler.getAbstractComponent("EnvironmentBindingReadData"));
//			System.out.println(FormalFormat.exportCandyComponentSignature(AbstractComponentHandler.getAbstractComponent(ac_id, false),""));
//			System.out.println("------------------------------------------");
//			System.out.println(FormalFormat.exportComponentSignatureLatex(AbstractComponentHandler.getAbstractComponent(ac_id,true), null, false));
//			System.out.println("------------------------------------------");
//			System.out.println(FormalFormat.exportContextContractWithIDs(ContextContractHandler.getContextContract(ac_id), null));
		
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try {
//			Resolution.main(args);
//		} catch (DBHandlerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public static String exportComponentSignature(AbstractComponentType ac, String space){
		boolean b;
		
		try {
			ResolutionNode.setup();
		} catch (ResolveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String str = "";
		if (space==null) {
			b = true;
			space="    ";
			str+="[name :> "+ac.getName()+",";
		}else{
			b = false;
			space+="    ";
			str+=ac.getName()+"[";
		}
		
//		str+="\n concern :> "+ac.getSupertype().getName()+",";
		for(ContextParameterType cp: ac.getContextParameter()){
			if(cp.getBound()!= null){
				try {
					if(cp.getBoundValue()==null){
						if(cp.getBound().getCcName().equals("DATA")){
							str+="\n"+space+cp.getName()+REQUIRED+cp.getBound().getCcName()+",";
						}else{
							if(AbstractComponentHandler.getAbstractComponentFromContextContractID(cp.getBound().getCcId()).getIdAc() == ac.getIdAc() ){
								str+="\n"+space+cp.getName()+REQUIRED+cp.getBound().getCcName()+" [],";
							}else{
								str+="\n"+space+cp.getName()+REQUIRED+cp.getBound().getCcName()+" ["+exportComponentSignature(AbstractComponentHandler.getAbstractComponentFromContextContractID(cp.getBound().getCcId()), ""+space)+"],";
							}
						}
					}else{
						str+="\n"+space+cp.getName()+REQUIRED+cp.getBoundValue()+",";
					}
					
				} catch (DBHandlerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				str+="\n"+space+cp.getName()+" : "+cp.getBoundValue()+",";
			}
		}
		if(b){
			for(CalculatedParameterType cp: ac.getQualityParameters()){
				str+="\n"+space+cp.getName()+" : DATA(Quality)";
			}
			for(CalculatedParameterType cp: ac.getCostParameters()){
				str+="\n"+space+cp.getName()+" : DATA(Cost)";
			}
			for(CalculatedParameterType cp: ac.getRankingParameters()){
				str+="\n"+space+cp.getName()+" : DATA(Ranking)";
			}
			
		}
		str+="]";
		return str;
	}
	//Recursive é quando está dentro de uma impressão completa
	public static String exportComponentSignatureLatex(AbstractComponentType ac, String space, boolean recursive){
		boolean b;
		String str;
		try {
			ResolutionNode.setup();
		} catch (ResolveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(recursive){
			str = "";
		}else{
			str = "\\begin{figure}\n\\caption{CAPTION ????????? }\\label{??????????}\n\\begin{small}\n\\begin{center}\n$\\left[\n\\begin{minipage}{\\textwidth}\n\\begin{tabbing}\n";
		}
		
		if (space==null) {
			b = true;
			space="    ";
			str+="\\=\\textbf{\\textit{name}} :> "+"\\textsc{"+ac.getName()+"}"+",\\\\";
		}else{
			b = false;
			space+="    ";
			str+="\\textsc{"+ac.getName()+"}"+"[";
		}
		
//		str+="\n concern :> "+ac.getSupertype().getName()+",";
		for(ContextParameterType cp: ac.getContextParameter()){
			if(cp.getBound()!= null){
				try {
					if(cp.getBoundValue()==null){
						if(cp.getBound().getCcName().equals("DATA")){
							str+="\n"+space+"\\>\\textbf{\\textit{"+cp.getName()+"}}"+REQUIRED+"\\textsc{"+cp.getBound().getCcName()+"},\\\\";
						}else{
							if(AbstractComponentHandler.getAbstractComponentFromContextContractID(cp.getBound().getCcId()).getIdAc() == ac.getIdAc() ){
								str+="\n"+space+"\\>\\textbf{\\textit{"+cp.getName()+"}}"+REQUIRED+"\\textsc{"+cp.getBound().getCcName()+"} [],\\\\";
							}else{
//								str+="\n"+space+"\\>\\textbf{\\textit{"+cp.getName()+"}}"+REQUIRED+"\\textsc{"+cp.getBound().getCcName()+"} ["+exportComponentSignatureLatex(AbstractComponentHandler.getAbstractComponentFromContextContractID(cp.getBound().getCcId()), ""+space, true)+"],";
							}
						}
					}else{
						str+="\n"+space+"\\>\\textbf{\\textit{"+cp.getName()+"}}"+REQUIRED+"\\textsc{"+cp.getBoundValue()+"},\\\\";
					}
					
				} catch (DBHandlerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				str+="\n"+"\\>\\textbf{\\textit{"+space+cp.getName()+"}}"+" : "+"\\textsc{"+cp.getBoundValue()+"}\\\\,";
			}
		}
		if(b){
			for(CalculatedParameterType cp: ac.getQualityParameters()){
				str+="\n"+space+"\\>\\textbf{\\textit{"+cp.getName()+"}}"+" : \\textsc{DATA(Quality)}\\\\";
			}
			for(CalculatedParameterType cp: ac.getCostParameters()){
				str+="\n"+space+"\\>\\textbf{\\textit{"+cp.getName()+"}}"+" : \\textsc{DATA(Cost)}\\\\";
			}
			for(CalculatedParameterType cp: ac.getRankingParameters()){
				str+="\n"+space+"\\>\\textbf{\\textit{"+cp.getName()+"}}"+" : \\textsc{DATA(Ranking)}\\\\";
			}
			
		}
		
		if(recursive){
			str+="]";
			
		}else{
			str +="\n\\end{tabbing}\n\\end{minipage}\n\\right]$\n\\end{center}\n\\end{small}\n\\begin{center}\n    Fonte: Próprio autor.\n\\end{center}\n\\end{figure}";
		}
		return str;
	}
	
	public static String exportCandyComponentSignature(AbstractComponentType ac, String space){
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
		str+=ac.getName()+"[";
//		str+="\n concern :> "+ac.getSupertype().getName()+",";
		for(ContextParameterType cp: ac.getContextParameter()){
			if(cp.getBound()!= null){
				try {
					if(cp.getBoundValue()==null){
						if(cp.getBound().getCcName().equals("DATA")){
							str+="\n"+space+cp.getName()+REQUIRED+cp.getBound().getCcName()+",";
						}else{
							if(AbstractComponentHandler.getAbstractComponentFromContextContractID(cp.getBound().getCcId()).getIdAc() == ac.getIdAc() ){
								str+="\n"+space+cp.getName()+REQUIRED+cp.getBound().getCcName()+" [],";
							}else{
								str+="\n"+space+cp.getName()+REQUIRED+cp.getBound().getCcName()+" ["+exportCandyComponentSignature(AbstractComponentHandler.getAbstractComponentFromContextContractID(cp.getBound().getCcId()), ""+space)+"],";
							}
						}
					}else{
						str+="\n"+space+cp.getName()+REQUIRED+cp.getBoundValue()+",";
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

	public static String exportComponentSignatureWithIDs(AbstractComponentType ac, String space){
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
						str+="\n"+space+cp.getName()+"{"+cp.getCpId()+"} : "+cp.getBound().getCcName()+",";
					}else{
						if(AbstractComponentHandler.getAbstractComponentFromContextContractID(cp.getBound().getCcId()).getIdAc() == ac.getIdAc() ){
							str+="\n"+space+cp.getName()+"{"+cp.getCpId()+"} : "+cp.getBound().getCcName()+" [],";
						}else{
							str+="\n"+space+cp.getName()+"{"+cp.getCpId()+"} : "+cp.getBound().getCcName()+" ["+exportComponentSignatureWithIDs(AbstractComponentHandler.getAbstractComponentFromContextContractID(cp.getBound().getCcId()), ""+space)+"],";
						}
					}
				} catch (DBHandlerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				str+="\n"+space+cp.getName()+"{"+cp.getCpId()+"} : "+cp.getBoundValue()+",";
			}
		}
		for(CalculatedParameterType cp: ac.getQualityParameters()){
			str+="\n"+space+cp.getName()+"{"+cp.getCalcId()+"} : DATA(Quality)";
		}
		for(CalculatedParameterType cp: ac.getCostParameters()){
			str+="\n"+space+cp.getName()+"{"+cp.getCalcId()+"} : DATA(Cost)";
		}
		for(CalculatedParameterType cp: ac.getRankingParameters()){
			str+="\n"+space+cp.getName()+"{"+cp.getCalcId()+"} : DATA(Ranking)";
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
		str+=cc.getCcName()+"[";
		for(ContextParameterType cp: ac.getContextParameter()){
			for(ContextArgumentType ca : cc.getContextArguments()){
				if(cp.getCpId().equals(ca.getCpId())){
					if(ca.getContextContract()!=null){
						str+="\n"+space+cp.getName()+" = "+exportContextContract(ca.getContextContract(),space+"")+",";
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
		if(cc.getPlatform()!=null){
			str+="\n"+space+"PLATFORM: = "+exportContextContract(cc.getPlatform().getPlatformContract(),space+"")+",";
		}
		for(CalculatedParameterType cp: ac.getQualityParameters()){
//			str+="\n"+space+cp.getName()+" = ";
			for(CalculatedArgumentType c : cc.getQualityArguments()){
				if(c.getCpId()==cp.getCalcId()){
					str+="\n"+space+cp.getName()+" = "+c.getValue();//terminar
				}
			}
		}
		for(CalculatedParameterType cp: ac.getCostParameters()){
//			str+="\n"+space+cp.getName()+" = ";
			for(CalculatedArgumentType c : cc.getCostArguments()){
				if(c.getCpId()==cp.getCalcId()){
					str+="\n"+space+cp.getName()+" = "+c.getValue();//terminar
				}
			}
		}
		for(CalculatedParameterType cp: ac.getRankingParameters()){
//			str+="\n"+space+cp.getName()+" = ";
			for(CalculatedArgumentType c : cc.getRankingArguments()){
				if(c.getCpId()==cp.getCalcId()){
					str+="\n"+space+cp.getName()+" = "+c.getValue();//terminar
				}
			}
		}
		
		str+="]";
		return str;
	}
	
	
	public static String exportContextContractWithIDs(ContextContract cc, String space){
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
		str+=cc.getCcName()+"{"+cc.getCcId()+"}"+"[";
		for(ContextParameterType cp: ac.getContextParameter()){
			for(ContextArgumentType ca : cc.getContextArguments()){
				if(cp.getCpId().equals(ca.getCpId())){
					if(ca.getContextContract()!=null){
						str+="\n"+space+cp.getName()+"{"+cp.getCpId()+"}"+" = "+exportContextContractWithIDs(ca.getContextContract(),space+"")+",";
					}else{
						if(ca.getValue()!= null){
							
							str+="\n"+space+cp.getName()+"{"+cp.getCpId()+"}"+" = "+ca.getValue().getValue()+",";
						}else{
							if(ca.getCpId()!= null){
								//Definir comportamento
								System.out.println("aaaaaaaaaaaaaaaaa");
							}
						}
					}
				}
			}
		}
		if(cc.getPlatform()!=null){
			str+="\n"+space+"PLATFORM: = "+exportContextContractWithIDs(cc.getPlatform().getPlatformContract(),space+"")+",";
		}
		for(CalculatedParameterType cp: ac.getQualityParameters()){
//			str+="\n"+space+cp.getName()+" = ";
			for(CalculatedArgumentType c : cc.getQualityArguments()){
				if(c.getCpId()==cp.getCalcId()){
					str+="\n"+space+cp.getName()+"{"+cp.getCalcId()+"}"+" = "+c.getValue();//terminar
				}
			}
		}
		for(CalculatedParameterType cp: ac.getCostParameters()){
//			str+="\n"+space+cp.getName()+" = ";
			for(CalculatedArgumentType c : cc.getCostArguments()){
				if(c.getCpId()==cp.getCalcId()){
					str+="\n"+space+cp.getName()+"{"+cp.getCalcId()+"}"+" = "+c.getValue();//terminar
				}
			}
		}
		for(CalculatedParameterType cp: ac.getRankingParameters()){
//			str+="\n"+space+cp.getName()+" = ";
			for(CalculatedArgumentType c : cc.getRankingArguments()){
				if(c.getCpId()==cp.getCalcId()){
					str+="\n"+space+cp.getName()+"{"+cp.getCalcId()+"}"+" = "+c.getValue();//terminar
				}
			}
		}
		
		str+="]";
		return str;
	}
	
}
