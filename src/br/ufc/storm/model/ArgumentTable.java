package br.ufc.storm.model;

import java.util.Enumeration;
import java.util.Hashtable;

import br.ufc.storm.control.Resolution;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextArgumentValueType;
import br.ufc.storm.jaxb.ContextContract;

public class ArgumentTable {

	Hashtable <Integer , ContextArgumentType> argumentTable = new Hashtable<Integer, ContextArgumentType>();
	//The key is cp_id
	
	public static void main(String [] a){
		try {
			Resolution.main(a);
		} catch (DBHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArgumentTable(ContextContract cc){
		fill(cc);
	}
	
	private void fill(ContextContract cc) {
		//Adds all numerical arguments to hash table for performance purpose
		for(ContextArgumentType cat:cc.getContextArgumentsProvided()){
			if(cat.getValue()!=null){
				this.addNewArgument(cat.getCpId(), ""+cat.getValue().getValue());
			}else{
				if(cat.getContextContract()!=null){
					fill(cat.getContextContract());
				}
			}
		}
		if(cc.getPlatform()!=null){
			fill(cc.getPlatform().getPlatformContract());
		}
	}

	public void addArgument(int cp_id, ContextArgumentType arg){
		argumentTable.put(cp_id, arg);
	}
	
	public void addNewArgument(int cp_id, String value){
		ContextArgumentType cat = new ContextArgumentType();
		ContextArgumentValueType x = new ContextArgumentValueType();
		x.setDataType("Double");
		x.setValue(value);
		cat.setValue(x);
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
		return argumentTable.get(cp_id);
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
