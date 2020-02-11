package br.ufc.storm.model;

import java.util.Enumeration;
import java.util.Hashtable;

import br.ufc.storm.control.Resolution;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.FunctionException;
import br.ufc.storm.export.FormalFormat;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextArgumentValueType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.sql.AbstractComponentHandler;
import br.ufc.storm.sql.ContextContractHandler;
import br.ufc.storm.sql.ContextParameterHandler;
import br.ufc.storm.sql.ContextParameterTreeHandler;

public class ArgumentTable {

	Hashtable <Integer , ContextArgumentType> argumentTable = new Hashtable<Integer, ContextArgumentType>();
	//The key is cp_id

	
	public static void main(String[] args) {
		ContextContract cc;
		try {
			
			cc = ContextContractHandler.getContextContract(124);
			ArgumentTable argumentT = new ArgumentTable(cc);
			System.out.println(argumentT.getArgument(234).getValue().getValue());
//			System.out.println(FormalFormat.exportContextContractWithIDs(cc, null));
//			System.out.println(argumentT.toString());
			
		} catch (DBHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
		private void fill(ContextContract cc) {
			//Adds all numerical arguments to hash table for performance purpose
			if(cc.getPlatform()!=null){
				fill(cc.getPlatform().getPlatformContract());
			}
			for(ContextArgumentType cat:cc.getContextArguments()){
				if(cat.getValue()!=null){
					for(ContextParameterType cpt:cc.getAbstractComponent().getContextParameter()){
						if(cpt.getCpId().equals(cat.getContextParameter().getCpId())){
							addNewArgument(cat.getContextParameter().getCpId(), ""+cat.getValue().getValue(), cpt.getKind());
						}
					}
	
				}else{
					if(cat.getContextContract()!=null){
						fill(cat.getContextContract());
					}
				}
			}
			
		}

	public ArgumentTable(ContextContract cc){
		fill(cc);
	}
	
//	public static ContextArgumentType getContextArgument(ContextContract cc, int cp_id){
//		ContextArgumentType x = null;
//		for(ContextArgumentType cat:cc.getContextArguments()){
//			if(cat.getCpId()==cp_id){
//				return cat;
//			}else{
//				if(cat.get){
//					
//				}
//			}
//		}
//	}

	public void addArgument(int cp_id, ContextArgumentType arg){
		argumentTable.put(cp_id, arg);
	}

	public void addNewArgument(int cp_id, String value, int kind){
		ContextArgumentType cat = new ContextArgumentType();
		ContextArgumentValueType x = new ContextArgumentValueType();
		x.setDataType("Double");
		x.setValue(value);
		cat.setValue(x);
		cat.setKind(kind);
		cat.setContextParameter(new ContextParameterType());
		cat.getContextParameter().setCpId(cp_id);
		argumentTable.put(cp_id, cat);
	}

	public Double getArgumentValue(int cp_id){
		String value = argumentTable.get(cp_id).getValue().getValue();
		return Double.parseDouble(value);
		//		Method method;
		//		try {
		//			method = value.getClass().getMethod("doubleValue", null);
		//			argumentTable.put(cp_id, (Double) method.invoke(arg, null));
		//		} catch (NoSuchMethodException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (SecurityException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (IllegalAccessException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (IllegalArgumentException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (InvocationTargetException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}

	}

	public ContextArgumentType getArgument(int cp_id){
		ContextArgumentType cat = argumentTable.get(cp_id);
		ContextArgumentType copy = new ContextArgumentType();
		ContextArgumentValueType cavt = new ContextArgumentValueType();
		
		if(cat!=null){
			//System.out.println(cat.getValue().getValue());
			cavt.setValue(""+Double.parseDouble(cat.getValue().getValue()));
			cavt.setDataType(cat.getValue().getDataType()+"");
			copy.setValue(cavt);
			copy.setKind(cat.getKind().intValue());
		}else{
			try {
				//TODO: Fazer a busca em memória, e não em disco
				cavt.setValue(ContextParameterHandler.getBoundValue(cp_id));
			} catch (DBHandlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(cavt.getValue()==null){

				cavt.setValue(null); //Neutral element of multiplication

			}
			cavt.setDataType("Double"); //Default data type
			copy.setValue(cavt); 
			copy.setContextParameter(new ContextParameterType());
			copy.getContextParameter().setCpId(cp_id);
		}

		return copy;
	}


	public String toString(){
		String str = "";
		Enumeration<Integer> ids = argumentTable.keys();
		while(ids.hasMoreElements()){
			int x = ids.nextElement();
			str+="cp_id: "+ x +" value: "+argumentTable.get(x).getValue().getValue()+"\n";
		}
		return str;
	}
}
