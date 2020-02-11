package br.ufc.storm.model;

import java.util.Hashtable;
import java.util.List;

import br.ufc.storm.jaxb.ContextContract;

public class ResolveContractTree {
	int cc_id;
	ContextContract cc;
	//Table of parameters and each paramenter has a list of abstract component IDs with value true
	//It will be used to find quickly the subtype relation
	Hashtable<Integer, Hashtable<Integer, Boolean>> tableOfParameters;

	public ResolveContractTree(int cc_id, ContextContract cc) {
		this.cc_id = cc_id;
		this.cc = cc;
	}
	
	public boolean addAbstractContractIDtoTableOfSubtypes(int cp_id, int ac_id) {
		if(tableOfParameters.get(cp_id)==null) {
			tableOfParameters.put(cp_id, new Hashtable<Integer, Boolean>());
		}
		tableOfParameters.get(cp_id).put(ac_id, true);
		return true;
	}
	
	public boolean addAbstractContractIDtoTableOfSubtypes(int cp_id, List<Integer> acList) {
		if(tableOfParameters.get(cp_id)==null) {
			tableOfParameters.put(cp_id, new Hashtable<Integer, Boolean>());
		}
		for(int i:acList) {
			tableOfParameters.get(cp_id).put(i, true);
		}
		return true;
	}
	
	public boolean isSubtype(int cp_id, int ac_id) {
		return tableOfParameters.get(cp_id).get(ac_id);
	}
	
	
}
