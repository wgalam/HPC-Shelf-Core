package export;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.CostParameterType;
import br.ufc.storm.jaxb.QualityParameterType;
import br.ufc.storm.sql.AbstractComponentHandler;
import br.ufc.storm.sql.ContextArgumentHandler;
import br.ufc.storm.sql.ContextContractHandler;
import br.ufc.storm.xml.XMLHandler;

public class FormalFormat {

	public static void main(String[] args) {
		try {
			//			System.out.println(XMLHandler.getAbstractComponent("Cluster"));
			//			System.out.println(XMLHandler.getContextContract(126));
//			ContextContract cc = ContextContractHandler.getContextContract(126);
//			System.out.println(FormalFormat.exportContextContract(cc, null));

						System.out.println(FormalFormat.exportComponentSignature(AbstractComponentHandler.getAbstractComponent(35)));
			//			System.out.println(XMLHandler.getAbstractComponent("MatrixMultiplication"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String exportComponentSignature(AbstractComponentType ac){
		String str = "";
		str+=ac.getName()+"=[\n";
		for(ContextParameterType cp: ac.getContextParameter()){
			if(cp.getBound()!= null){
				str+=cp.getName()+" :"+cp.getBound().getCcName()+", \n";
			}else{
				str+=cp.getName()+" :"+cp.getBoundValue()+", \n";
			}
		}
		for(QualityParameterType cp: ac.getQualityParameters()){
			str+=cp.getName()+" = ,\n";
		}
		for(CostParameterType cp: ac.getCostParameters()){
			str+=cp.getName()+" = ,\n";
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
		str+=cc.getCcName()+"[";
		for(ContextParameterType cp: ac.getContextParameter()){
			for(ContextArgumentType ca : cc.getContextArguments()){
				if(cp.getCpId()==ca.getVariableCpId()){
					if(ca.getContextContract()!=null){
						str+="\n"+space+cp.getName()+" = "+exportContextContract(ca.getContextContract(),"    ")+",";
					}else{
						if(ca.getValue()!= null){
							str+="\n"+space+cp.getName()+" = "+ca.getValue().getValue()+",";
						}else{
							if(ca.getVariableCpId()!= null){
								//Definir comportamento
							}
						}
					}
				}
			}
		}
		for(QualityParameterType cp: ac.getQualityParameters()){
			str+="\n"+space+cp.getName()+" = ";//terminar
		}
		for(CostParameterType cp: ac.getCostParameters()){
			str+="\n"+space+cp.getName()+" = ";//terminar
		}
		str+="]";
		return str;
	}
}
