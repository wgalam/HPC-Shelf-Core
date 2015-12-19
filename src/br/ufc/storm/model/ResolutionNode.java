package br.ufc.storm.model;

import java.util.ArrayList;
import java.util.List;

import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.jaxb.CostParameterType;
import br.ufc.storm.jaxb.QualityParameterType;
import br.ufc.storm.jaxb.RankingParameterType;


public class ResolutionNode {
	private int ac_id;
	private ResolutionNode supertype;
	private String name;
	private String acPath=null;
	private List<ContextParameterType> cps;
	private List<QualityParameterType> qps;
	private List<RankingParameterType> Rps;
	private List<CostParameterType> cops;	
	private List<ResolutionNode> subtype;

	public ResolutionNode(){
		subtype = new ArrayList<ResolutionNode>();
		setCps(new ArrayList<ContextParameterType>());
		setQps(new ArrayList<QualityParameterType>());
		setRps(new ArrayList<RankingParameterType>());
	}		
	public int getAc_id() {
		return ac_id;
	}
	public void setAc_id(int ac_id) {
		this.ac_id = ac_id;
	}
	public List<ResolutionNode> getSubtype() {
		return subtype;
	}
	public void addSubtype(ResolutionNode sub){
		this.subtype.add(sub);
	}
	public ResolutionNode getSupertype() {
		return supertype;
	}
	public void setSupertype(ResolutionNode supertype) {
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
		for(ResolutionNode rn : subtype){
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
	public ResolutionNode findNode(Integer id){
		if(ac_id==id){
			return this;
		}else{
			for(ResolutionNode rn: this.getSubtype()){
				ResolutionNode x = rn.findNode(id);
				if(x != null){
					return x;
				}
			}
		}
		return null;
	}

	public ResolutionNode findNode(String nameToResolve){
		if(nameToResolve.equals(acPath.toString()+"."+name) ){
			return this;
		}else{
			for(ResolutionNode rn: this.getSubtype()){
				ResolutionNode x = rn.findNode(nameToResolve);
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
	public List<QualityParameterType> getQps() {
		return qps;
	}
	public void setQps(List<QualityParameterType> qps) {
		this.qps = qps;
	}
	public List<CostParameterType> getCops() {
		return cops;
	}
	public void setCops(List<CostParameterType> cops) {
		this.cops = cops;
	}
	public List<RankingParameterType> getRps() {
		return Rps;
	}
	public void setRps(List<RankingParameterType> rkps) {
		Rps = rkps;
	}
	public String getPath() {
		return acPath;
	}
	public void setPath(String path) {
		this.acPath = path;
	}

}

