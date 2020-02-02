package br.ufc.storm.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.postgresql.core.SetupQueryRunner;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.ResolveException;
import br.ufc.storm.jaxb.CalculatedParameterType;
import br.ufc.storm.jaxb.ContextArgumentType;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.sql.ResolutionHandler;


public class ResolutionNodeOld {
	public static ResolutionNodeOld resolutionTree = null;
	private static Hashtable <Integer , ResolutionNodeOld> treeTable = new Hashtable<Integer, ResolutionNodeOld>();
	private int ac_id;
	private ResolutionNodeOld supertype;
	private String name;
	private String acPath=null;
	private List<ContextParameterType> cps;
	private List<CalculatedParameterType> caps;
	private List<CalculatedParameterType> qps;
	private List<CalculatedParameterType> rps;
	private List<CalculatedParameterType> cops;	
	private List<ResolutionNodeOld> subtype;

	public static void main(String [] a){
		ResolutionNodeOld r = null;
		try {
			setup();
		} catch (ResolveException e) {
			e.printStackTrace();
		}
		System.out.println(r.toString());
	}
	
	public ResolutionNodeOld(){
		subtype = new ArrayList<ResolutionNodeOld>();
		setCps(new ArrayList<ContextParameterType>());
		setQps(new ArrayList<CalculatedParameterType>());
		setCops(new ArrayList<CalculatedParameterType>());
		setRps(new ArrayList<CalculatedParameterType>());
		setCaps(new ArrayList<CalculatedParameterType>());
	}
	
	public static void setup() throws ResolveException{
		
		if(ResolutionNodeOld.resolutionTree == null){
			try {
				ResolutionNodeOld tree = new ResolutionNodeOld();
				tree.setAc_id(0);
				tree.setName("root");
				tree.setPath("");
				tree.setSupertype(tree);
				addTable(0, tree);
				//Descomentar, comentario somente para não dar problema de duplicação
				//ResolutionNodeOld.resolutionTree = ResolutionHandler.generateResolutionTree(0, tree, new ArrayList<ContextParameterType>(), new ArrayList<CalculatedParameterType>(), new ArrayList<CalculatedParameterType>(), new ArrayList<CalculatedParameterType>(), new ArrayList<CalculatedParameterType>(), "root");
			} catch (Exception e) {
				throw new ResolveException("Can not create resolution tree: ",e);
			}
		}
	}
	
	public static void addTable(int ac_id, ResolutionNodeOld r){
		treeTable.put(ac_id, r);
	}
	
	public int getAc_id() {
		return ac_id;
	}
	public void setAc_id(int ac_id) {
		this.ac_id = ac_id;
	}
	public List<ResolutionNodeOld> getSubtype() {
		return subtype;
	}
	public void addSubtype(ResolutionNodeOld sub){
		this.subtype.add(sub);
	}
	public ResolutionNodeOld getSupertype() {
		return supertype;
	}
	public void setSupertype(ResolutionNodeOld supertype) {
		this.supertype = supertype;
	}

	public String getName() {
		return name;
	}

	public String getFullName(){
		return acPath+"."+name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString(){
		String str = "";
		str = "\nNode: "+getFullName()+" AC ID: "+ac_id+" Supertype ID: "+ this.getSupertype().getName() +" Quantidade de Filhos: "+subtype.size() + " E possui "+this.getCps().size()+" parâmetros de contexto #"+this.writeParameters()+"E possui "+this.getQps().size()+" parâmetros de qualidade #";
		for(ResolutionNodeOld rn : subtype){
			str += rn.toString();
		}
		return str;
	}

	private String writeParameters() {
		String str = "";
		for(ContextParameterType cp: cps){
			str += "CP Name = "+cp.getName()+", CP ID = "+cp.getCpId()+".";
		}
		return str;
	}
	
	/**
	 * Control null return 
	 * @param id
	 * @return
	 */
	public ResolutionNodeOld findNode(Integer id){
		
		if(ac_id==id){
			return this;
		}else{
			for(ResolutionNodeOld rn: this.getSubtype()){
				ResolutionNodeOld x = rn.findNode(id);
				if(x != null){
					return x;
				}
			}
		}
		return null;
	}

	/**
	 * Control null return 
	 * @param id
	 * @return
	 */
	//	public ResolutionNode isSubtypeContravariant(Integer id, int ac_id){
	//		if(ac_id==id){
	//			return this;
	//		}else{
	//			for(ResolutionNode rn: this.getSubtype()){
	//				ResolutionNode x = rn.isSubtypeContravariant(id);
	//				if(x != null){
	//					return x;
	//				}
	//			}
	//		}
	//		return null;
	//	}


	public List<ContextParameterType> getCps() {
		return cps;
	}
	public void setCps(List<ContextParameterType> cps) {
		this.cps = cps;
	}

	public void addParameter(ContextParameterType cp){
		this.cps.add(cp);
	}
	
	public List<CalculatedParameterType> getQps() {
		return qps;
	}
	public void setQps(List<CalculatedParameterType> qps) {
		this.qps = qps;
	}
	public List<CalculatedParameterType> getCops() {
		return cops;
	}
	public void setCops(List<CalculatedParameterType> cops) {
		this.cops = cops;
	}
	public List<CalculatedParameterType> getRps() {
		return rps;
	}
	public void setRps(List<CalculatedParameterType> rkps) {
		rps = rkps;
	}
	public String getPath() {
		return acPath;
	}
	public void setPath(String path) {
		this.acPath = path;
	}
	public List<CalculatedParameterType> getCaps() {
		return caps;
	}
	public void setCaps(List<CalculatedParameterType> caps) {
		this.caps = caps;
	}
}

