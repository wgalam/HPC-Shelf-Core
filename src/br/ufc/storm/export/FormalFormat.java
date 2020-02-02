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
			int ac_id = 3;
			int cc_id = 123;
			//			ContextContract cc = ContextContractHandler.getContextContract(318);
			//			System.out.println(FormalFormat.exportContextContract(cc, null));
			//			System.out.println(FormalFormat.exportContextContractWithIDs(cc, null));
			//			System.out.println(XMLHandler.getAbstractComponent("EnvironmentBindingReadData"));
			//			System.out.println(FormalFormat.exportCandyComponentSignature(AbstractComponentHandler.getAbstractComponent(ac_id, false),""));
			//			System.out.println("------------------------------------------");
						System.out.println(FormalFormat.exportComponentSignature(AbstractComponentHandler.getAbstractComponent(ac_id,true), ""));

			//			System.out.println(FormalFormat.exportComponentSignatureLatex(AbstractComponentHandler.getAbstractComponent(ac_id,true), null, false));
						System.out.println("------------------------------------------");
			System.out.println(FormalFormat.exportContextContract(ContextContractHandler.getContextContract(cc_id), null, 1, false));

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

	
	public static String getComment(){
		return "          # ";
	}
	
	public static String exportComponentSignature(AbstractComponentType ac, String space){
		boolean b;

		try {
			ResolutionNode.setup();
		} catch (ResolveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//		System.out.println(">>>>>"+XMLHandler.getAbstractComponent(ac));
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
						if(cp.getCpUnit()!=null){
							str+=getComment()+cp.getCpUnit();  
						}
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
		int var = 0;
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
			str = "\\begin{figure}\n\\caption{CAPTION ????????? }\\label{??????????}\n\\begin{small}\n\\begin{center}\n$\\left[\n\\begin{minipage}{\\textwidth}\n\\begin{tabbing}\n\\=tabs \\= tabs \\= tabs \\= tabs \\= tabs \\= tabs \\= tabs \\= tabs \\kill\n";
		}

		if (space==null) {
			b = true;
			space="    ";
			str+="\\=\\textbf{\\textit{name}} :> "+"\\textsc{"+ac.getName()+"}"+",\\\\";
		}else{
			b = false;
			space+="    ";
			str+="\\textsc{"+ac.getName()+"}"+"[";
			if(!ac.getContextParameter().isEmpty()){
				str+="\\+\\\\";
			}
		}

		for(int i=0; i < ac.getContextParameter().size(); i++){
			ContextParameterType cp = ac.getContextParameter().get(i);
			if(cp.getBound()!= null){
				try {
					if(cp.getBoundValue()==null){
						if(cp.getBound().getCcName().equals("DATA")){
							str+="\n"+space+"\\>\\textbf{\\textit{"+replaceUnderline(cp.getName())+"}}"+REQUIRED+"\\textsc{"+cp.getBound().getCcName()+"[]}";
						}else{
							if(AbstractComponentHandler.getAbstractComponentFromContextContractID(cp.getBound().getCcId()).getIdAc() == ac.getIdAc() ){
								str+="\n"+space+"\\>\\textbf{\\textit{"+replaceUnderline(cp.getName())+"}}"+REQUIRED+"\\textsc{"+cp.getBound().getCcName()+"} []";
							}else{
								str+="\n"+space+"\\>\\textbf{\\textit{"+replaceUnderline(cp.getName())+"}}"+REQUIRED+exportComponentSignatureLatex(AbstractComponentHandler.getAbstractComponentFromContextContractID(cp.getBound().getCcId()), ""+space, true);
							}
						}
					}else{
						str+="\n"+space+"\\>\\textbf{\\textit{"+replaceUnderline(cp.getName())+"}}"+REQUIRED+"\\textsc{"+cp.getBoundValue()+"}";
						if(cp.getCpUnit()!=null){
							str+=getComment()+cp.getCpUnit();  
						}
					}
					if(i!=ac.getContextParameter().size()-1){//
						str+=",\\\\";
					}
				} catch (DBHandlerException e) {
					e.printStackTrace();
				}
			}else{
				str+="\n"+"\\>\\textbf{\\textit{"+space+replaceUnderline(cp.getName())+"}}"+" : "+"\\textsc{"+cp.getBoundValue()+"}\\\\,";
			}
		}
		if(b){
			str+="\\\\";
			for(CalculatedParameterType cp: ac.getQualityParameters()){
				str+="\n"+space+"\\>\\textbf{\\textit{"+replaceUnderline(cp.getName())+"}}"+" : \\textsc{DATA[],}\\\\";
			}
			for(CalculatedParameterType cp: ac.getCostParameters()){
				str+="\n"+space+"\\>\\textbf{\\textit{"+replaceUnderline(cp.getName())+"}}"+" : \\textsc{DATA[],}\\\\";
			}
			for(CalculatedParameterType cp: ac.getRankingParameters()){
				str+="\n"+space+"\\>\\textbf{\\textit{"+replaceUnderline(cp.getName())+"}}"+" : \\textsc{DATA[],}\\\\";
			}

		}

		if(recursive){
			if(ac.getContextParameter().isEmpty()){
				str+="]";
			}else{
				str+="]\\-";
			}


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
							if(cp.getCpUnit()!=null){
								str+=getComment()+cp.getCpUnit();  
							}
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
					if(cp.getCpUnit()!=null){
						str+=getComment()+cp.getCpUnit();  
					}
				}
			}
		}
		for(CalculatedParameterType cp: ac.getCostParameters()){
			//			str+="\n"+space+cp.getName()+" = ";
			for(CalculatedArgumentType c : cc.getCostArguments()){
				if(c.getCpId()==cp.getCalcId()){
					str+="\n"+space+cp.getName()+" = "+c.getValue();//terminar
					if(cp.getCpUnit()!=null){
						str+=getComment()+cp.getCpUnit();  
					}
				}
			}
		}
		for(CalculatedParameterType cp: ac.getRankingParameters()){
			//			str+="\n"+space+cp.getName()+" = ";
			for(CalculatedArgumentType c : cc.getRankingArguments()){
				if(c.getCpId()==cp.getCalcId()){
					str+="\n"+space+cp.getName()+" = "+c.getValue();//terminar
					if(cp.getCpUnit()!=null){
						str+=getComment()+cp.getCpUnit();  
					}
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


	/**
	 * Exports context contract as text output, in 3 different syntaxes
	 * @param cc Context contract
	 * @param space Number of spaces used for indentation on recursive calls 
	 * @param type Type of output: 1 - formal format text, 2 - formal format text with ids and 3 - for latex output
	 * @return
	 */
	
	//TODO: Concluir impressão formato formal
	public static String exportContextContract(ContextContract cc, String space, int type, boolean recursive){
		boolean b;
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

		if(recursive){
			str = "";
		}else{
			if(type>2){
				str = "\\begin{figure}\n\\caption{CAPTION ????????? }\\label{??????????}\n\\begin{small}\n\\begin{center}\n$\\left[\n\\begin{minipage}{\\textwidth}\n\\begin{tabbing}\n\\=tabs \\= tabs \\= tabs \\= tabs \\= tabs \\= tabs \\= tabs \\= tabs \\kill\n";
			}else{
				str+=cc.getCcName()+"[";
			}
			
		}



		AbstractComponentType ac = cc.getAbstractComponent();

		ResolutionNode r = ResolutionNode.resolutionTree.findNode(ac.getIdAc());
		ac.getQualityParameters().clear();
		ac.getQualityParameters().addAll(r.getQps());
		ac.getCostParameters().clear();
		ac.getCostParameters().addAll(r.getCops());
		ac.getRankingParameters().clear();
		ac.getRankingParameters().addAll(r.getRps());



		if (space.equals("    ")) {
			b = true;
			space="    ";
			str+="\\=\\textbf{\\textit{name}} = "+"\\textsc{"+cc.getCcName()+"}"+",\\\\";
		}else{
			b = false;
			space+="    ";
			str+="\\textsc{"+ac.getName()+"}"+"[";
			if(!ac.getContextParameter().isEmpty()){
				str+="\\+\\\\";
			}
		}
		for(int i= 0; i < ac.getContextParameter().size(); i++){
			ContextParameterType cp = ac.getContextParameter().get(i);
			for(ContextArgumentType ca : cc.getContextArguments()){
				if(cp.getCpId().equals(ca.getCpId())){
					if(ca.getContextContract()!=null){
						//						str+="\n"+space+cp.getName()+" = "+exportContextContract(ca.getContextContract(),space+"",3,true)+",";
						if(type>2){
							str+="\n"+space+"\\>\\textbf{\\textit{"+replaceUnderline(cp.getName())+"}}"+" = "+exportContextContract(ca.getContextContract(),space+"",type,true);
						}else{
							str+="\n"+space+cp.getName()+" = "+exportContextContract(ca.getContextContract(),space+"", type, true)+",";
						}
						

					}else{
						if(ca.getValue()!= null){
							if(type>2){
								str+="\n"+space+"\\>\\textbf{\\textit{"+replaceUnderline(cp.getName())+"}}"+" = "+"\\textsc{"+ca.getValue().getValue()+"}";
							}else{
								str+="\n"+space+cp.getName()+" = "+ca.getValue().getValue()+",";
							}
							
						}else{
							if(ca.getCpId()!= null){
								//Definir comportamento
							}
						}
					}
					if(i!=ac.getContextParameter().size()-1){
						str+=",\\\\";
					}
				}
			}

		}

		if(cc.getPlatform()!=null){
			str+="\n"+space+"PLATFORM: = "+exportContextContract(cc.getPlatform().getPlatformContract(),space+"", 3, true)+",";
		}
		for(CalculatedParameterType cp: ac.getQualityParameters()){
			//			str+="\n"+space+cp.getName()+" = ";
			for(CalculatedArgumentType c : cc.getQualityArguments()){
				if(c.getCpId()==cp.getCalcId()){
					if(type>2){
						str+="\n"+space+"\\>\\textbf{\\textit{"+replaceUnderline(cp.getName())+"}}"+" = \\textsc{"+ c.getValue()+",}\\\\";
					}else{
						if(type==1){
							str+="\n"+space+cp.getName()+" = "+c.getValue();
						}else{
							str+="\n"+space+cp.getName()+"{"+cp.getCalculatedArgument().getCpId()+"}"+         " = "+c.getValue();
						}
						
					}
				}
			}
		}
		for(CalculatedParameterType cp: ac.getCostParameters()){
			//			str+="\n"+space+cp.getName()+" = ";
			for(CalculatedArgumentType c : cc.getCostArguments()){
				if(c.getCpId()==cp.getCalcId()){
					if(c.getCpId()==cp.getCalcId()){
						if(type>2){
							str+="\n"+space+"\\>\\textbf{\\textit{"+replaceUnderline(cp.getName())+"}}"+" = \\textsc{"+ c.getValue()+",}\\\\";
						}else{
							if(type==1){
								str+="\n"+space+cp.getName()+" = "+c.getValue();
							}else{
								str+="\n"+space+cp.getName()+"{"+cp.getCalculatedArgument().getCpId()+"}"+         " = "+c.getValue();
							}
							
						}
					}
				}
			}
		}
		for(CalculatedParameterType cp: ac.getRankingParameters()){
			//			str+="\n"+space+cp.getName()+" = ";
			for(CalculatedArgumentType c : cc.getRankingArguments()){
				if(c.getCpId()==cp.getCalcId()){
					if(c.getCpId()==cp.getCalcId()){
						if(type>2){
							str+="\n"+space+"\\>\\textbf{\\textit{"+replaceUnderline(cp.getName())+"}}"+" = \\textsc{"+ c.getValue()+",}\\\\";
						}else{
							if(type==1){
								str+="\n"+space+cp.getName()+" = "+c.getValue();
							}else{
								str+="\n"+space+cp.getName()+"{"+cp.getCalculatedArgument().getCpId()+"}"+         " = "+c.getValue();
							}
							
						}
					}
				}
			}
		}
		if(type>2){
			if(recursive){
				if(ac.getContextParameter().isEmpty()){
					str+="]";
				}else{
					str+="]\\-";
				}
			}else{
				str +="\n\\end{tabbing}\n\\end{minipage}\n\\right]$\n\\end{center}\n\\end{small}\n\\begin{center}\n    Fonte: Próprio autor.\n\\end{center}\n\\end{figure}";
			}
		}else{
			str+="]";
		}
		return str;
	}

	public static String replaceUnderline(String str){
		return str.replace("_", "\\_");
	}

	public static String variableGenerator(int aux){
		String str="";
		while(aux>25){
			str+="A";
			aux-=26; 
		}
		str+=(char)(65+aux);
		return str;
	}


}
