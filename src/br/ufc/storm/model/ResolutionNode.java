package br.ufc.storm.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.exception.ResolveException;
import br.ufc.storm.jaxb.AbstractComponentType;
import br.ufc.storm.jaxb.CalculatedParameterType;
import br.ufc.storm.jaxb.ContextContract;
import br.ufc.storm.jaxb.ContextParameterType;
import br.ufc.storm.sql.AbstractComponentHandler;
import br.ufc.storm.sql.AbstractUnitHandler;
import br.ufc.storm.sql.ContextContractHandler;
import br.ufc.storm.sql.ContextParameterHandler;
import br.ufc.storm.sql.SliceHandler;


public class ResolutionNode {
	public static ResolutionNode resolutionTree = null;
	public static Hashtable <Integer , ResolutionNode> treeTable = new Hashtable<Integer, ResolutionNode>();
	private AbstractComponentType ac = new AbstractComponentType();
	public AbstractComponentType getAc() {
		return ac;
	}

	public void setAc(AbstractComponentType ac) {
		this.ac = ac;
	}

	//	private int ac_id;
	private ResolutionNode supertype;
	//	private String name;
	private String acPath=null;
	private List<ResolutionNode> subtype;
	private List<Integer> implementationContracts;

	public static void main(String [] a){
		ResolutionNode.resolutionTree = null;
		try {
			setup();
		} catch (ResolveException e) {
			e.printStackTrace();
		}
//		System.out.println(ResolutionNode.resolutionTree.toString());
		
		
		
		
	}

	public ResolutionNode(){
		this.ac=new AbstractComponentType();
		this.subtype = new ArrayList<ResolutionNode>();
		this.setImplementationContracts(new ArrayList<Integer>());
	}

	public static void setup() throws ResolveException{
		
		if(ResolutionNode.resolutionTree == null){
			long start = System.currentTimeMillis();
			try {
				generateResolutionTree();
				getAllParameters();
				getAllInnerComponents();
				getAllSlices();
				getAllAbstractUnits();
			} catch (DBHandlerException e) {
				throw new ResolveException("Can not create resolution tree: ",e);
			}
			long elapsed = System.currentTimeMillis() - start;
			System.out.println("Resolution Tree Setup Time: "+(int)((elapsed/1000)/60)+" minutos e "+(elapsed/1000) % 60+" segundos. Time millis: "+elapsed+" ms");
		}
		
	}


	public static void generateResolutionTree() throws DBHandlerException{
		//Cria lista de todos componentes abstratos
		List<AbstractComponentType> acs = AbstractComponentHandler.getAllAbstractComponent();
		//Indexa os componentes abstratos por supertipos
		Hashtable<Integer, List<AbstractComponentType>> supertypeIndex = new Hashtable<Integer, List<AbstractComponentType>>();
		for(AbstractComponentType a: acs) {
			if(a.getIdAc()!=0) {
				//Percorrendo toda lista de acs
				//Cria uma lista l com o que já existir na hashtable
				List<AbstractComponentType> l = supertypeIndex.get(a.getSupertype().getIdAc());
				if(l!=null) {
					supertypeIndex.get(a.getSupertype().getIdAc()).add(a);
				}else {
					l= new LinkedList<AbstractComponentType>();
					l.add(a);
					supertypeIndex.put(a.getSupertype().getIdAc(), l);
				}
			}

		}
		Hashtable<Integer, List<ACCCPair>> ccAC = new Hashtable<Integer, List<ACCCPair>>();
		List<ACCCPair> l = ContextContractHandler.getAllContextContract();
		for(ACCCPair a: l) {
			if(a.getAc()!=0) {
				//Percorrendo toda lista de acs
				//Cria uma lista l com o que já existir na hashtable
				List<ACCCPair> list = ccAC.get(a.getAc());
				if(list==null) {
					list= new LinkedList<ACCCPair>();
				}
				list.add(a);
			}
		}
		//Percorre a lista de componentes pelo indice dos supertipos, gerando a árvore
		AbstractComponentType a = new AbstractComponentType();
		a.setIdAc(0);
		a.setName("root");
		a.setPath("");
		a.setSupertype(a);
		ResolutionNode r = new ResolutionNode();
		r.ac=a;
		r.supertype = null;
		r.acPath="*";
		addTable(0, r);
		

		for(AbstractComponentType ac:supertypeIndex.get(0)) {
			r.addSubtype(addChildren(ac, r, supertypeIndex, acs, ccAC));
		}
		
		resolutionTree = r;

		//		node.setImplementationContracts(ContextContractHandler.getContextContractByAcId(node.getAc_id()));
		//		node.setSupertype(tree);
		//		tree.addSubtype(node);
		//		ResolutionNode.addTable(node.getAc_id(), node);
		//		generateResolutionTree(node.getAc_id(), node, node.getCps(), node.getQps(), node.getCops(), node.getRps(), node.getCaps(), node.getPath()+"."+node.getName());



		//Cria lista de todos componentes abstratos a partir do banco de dados e salva em uma hashmap
		//Criar link de todos os supertipos dos componentes abstratos com os respectivos objetos
	}

	public static ResolutionNode addChildren(AbstractComponentType a, ResolutionNode parent, Hashtable<Integer, List<AbstractComponentType>> supertypeIndex, List<AbstractComponentType> acs, Hashtable<Integer,List<ACCCPair>> ccAC) throws DBHandlerException {
		if(parent==null || a==null) {	
			System.out.println("Error generating resolution tree, empty abstract component");
		}
		ResolutionNode node = new ResolutionNode();
		node.ac = a;
		node.setPath(parent.getPath());
		node.setSupertype(parent);
		node.acPath=parent.getFullName();
		//			TODO: Refactor this next line, to get best performance
		//			node.setImplementationContracts(ContextContractHandler.getContextContractByAcId(node.getAc_id()));

		List<ACCCPair> list = ccAC.get(a.getIdAc());
		
		if(list!=null) {
//			System.out.println("aaaaaaaaaaa");
			for(ACCCPair p:list) {
				node.getImplementationContracts().add(p.cc);
			}
		}
		
		
		List<AbstractComponentType> l = supertypeIndex.get(node.getAc_id());
		if(l!=null) {
			for(AbstractComponentType ac:l) {
				node.addSubtype(addChildren(ac, node, supertypeIndex, acs,ccAC));
			}
		}
		addTable(node.getAc_id(), node);
		return node;
	}

	/**
	 * Firstly the method read all context parameters from database and save in a LinkedList
	 * After, the method walk in the linked list and add each parameter to its respective abstract component
	 * Later destroy the linked list
	 */
	public static void getAllParameters() {
		try {
			List<ACCPPair> cpl = ContextParameterHandler.getAllContextParameters();
			for(ACCPPair i:cpl) {
				ResolutionNode.treeTable.get(i.ac).addParameter(i.cp, i.parameter_type);
			}
			//Now this method must walk into the tree and perform a parameters inheritance of parameters
			applyParameterInheritance(null, resolutionTree);

		} catch (DBHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void getAllAbstractUnits() {
		try {
			List<ACAUNIT> auts = AbstractUnitHandler.getAllAbstractUnits();
			for(ACAUNIT i:auts) {
				ResolutionNode.treeTable.get(i.ac).getAc().getAbstractUnit().add(i.getAUT());
			}
			//TODO:Include the abstract units to inheritors

		} catch (DBHandlerException e) {
			e.printStackTrace();
		}

	}
	
	public static void getAllSlices() {
		try {
			List<ACSLICE> asl = SliceHandler.getAllSlices();
			for(ACSLICE i:asl) {
				ResolutionNode.treeTable.get(i.ac).getAc().getSlices().add(i.getSlice());
			}
			//TODO:Include the slices to inheritors components
		} catch (DBHandlerException e) {
			e.printStackTrace();
		}

	}
	
	public static void getAllInnerComponents(){
		try {
			List<IntIntPair> auts = AbstractComponentHandler.getAllInnerComponents();
			for(IntIntPair i:auts) {
				ResolutionNode.treeTable.get(i.first).getAc().getInnerComponents().add(treeTable.get(i.getSecond()).ac);
			}
			//TODO:Include the abstract units to inheritors

		} catch (DBHandlerException e) {
			e.printStackTrace();
		}
	}

	public static void applyParameterInheritance(ResolutionNode parent, ResolutionNode child) {
		if(parent!=null) {
			iheritParametersFromFather(parent, child);
		}
		for(ResolutionNode r:child.getSubtype()) {
			applyParameterInheritance(child, r);
		}
	}

	public static void iheritParametersFromFather(ResolutionNode parent, ResolutionNode child) {
		child.ac.getContextParameter().addAll(parent.ac.getContextParameter());
		child.ac.getQualityParameters().addAll(parent.ac.getQualityParameters());
		child.ac.getCostParameters().addAll(parent.ac.getCostParameters());
		child.ac.getRankingParameters().addAll(parent.ac.getRankingParameters());
	}

	public static void addTable(int ac_id, ResolutionNode r){
		treeTable.put(ac_id, r);
	}

	public int getAc_id() {
		return ac.getIdAc();
	}
	public void setAc_id(int ac_id) {
		this.ac.setIdAc(ac_id);
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
		return ac.getName();
	}

	public String getFullName(){
		return acPath+"."+ac.getName();
	}

	public void setName(String name) {
		this.ac.setName(name);
	}

	public String toString(){
		String str = "";
		if(this.getSupertype()== null) {
			str = "\n"+this.getFullName()+" AC ID: "+this.ac.getIdAc()+" \nSupertype Name: root"+" \nQuantidade de Filhos: "+this.subtype.size() + " \nE possui "+this.getCps().size()+" parâmetros de contexto #"+this.writeParameters()+"\nPossui "+this.getQps().size()+" parâmetros de qualidade e "+this.getRps().size()+" Parâmetros de Ranqueamento. Implementações: "+this.implementationContracts.size();
		}else {
			str = "\n"+this.getFullName()+" AC ID: "+this.ac.getIdAc()+" \nSupertype Name: "+ this.getSupertype().getName() +" \nQuantidade de Filhos: "+this.subtype.size() + " \nE possui "+this.getCps().size()+" parâmetros de contexto #"+this.writeParameters()+"\nPossui "+this.getQps().size()+" parâmetros de qualidade e "+this.getRps().size()+" Parâmetros de Ranqueamento. Implementações: "+this.implementationContracts.size();
		}
		str+="\n---------------------------------------------------------";
		for(ResolutionNode rn : subtype){
			str += rn.toString();
		}
		return str;
	}

	private String writeParameters() {
		String str = "";
		for(ContextParameterType cp: ac.getContextParameter()){
			str += "CP Name = "+cp.getName()+", CP ID = "+cp.getCpId()+".\n";
		}
		return str;
	}

	/**
	 * Control null return 
	 * @param id
	 * @return
	 */
	public ResolutionNode findNode(Integer id){
		return treeTable.get(id);
		//		if(ac.getIdAc()==id){
		//			return this;
		//		}else{
		//			for(ResolutionNode rn: this.getSubtype()){
		//				ResolutionNode x = rn.findNode(id);
		//				if(x != null){
		//					return x;
		//				}
		//			}
		//		}
		//		return null;
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
		return ac.getContextParameter();
	}
	//	public void setCps(List<ContextParameterType> cps) {
	//		this.cps = cps;
	//	}

	public void addParameter(Object cp, int type){
		switch (type) {
		case 1:
			this.ac.getContextParameter().add((ContextParameterType) cp);
			break;
		case 2:
			this.ac.getQualityParameters().add((CalculatedParameterType) cp);
			break;
		case 3:
			this.ac.getCostParameters().add((CalculatedParameterType) cp);
			break;
		case 4:
			this.ac.getRankingParameters().add((CalculatedParameterType) cp);
			break;
			//		case 5:
			//			this.ac.caps.add((CalculatedParameterType) cp);
			//			break;
		default:
			System.out.println("Unrecognized Parameter Type");
			break;
		}
	}

	public List<CalculatedParameterType> getQps() {
		return ac.getQualityParameters();
	}
	//	public void setQps(List<CalculatedParameterType> qps) {
	//		this.qps = qps;
	//	}
	public List<CalculatedParameterType> getCops() {
		return ac.getCostParameters();
	}
	//	public void setCops(List<CalculatedParameterType> cops) {
	//		this.cops = cops;
	//	}
	public List<CalculatedParameterType> getRps() {
		return ac.getRankingParameters();
	}
	//	public void setRps(List<CalculatedParameterType> rkps) {
	//		rps = rkps;
	//	}
	public String getPath() {
		return acPath;
	}
	public void setPath(String path) {
		this.acPath = path;
	}
	//	public List<CalculatedParameterType> getCaps() {
	//		return ac.getcaps;
	//	}
	//	public void setCaps(List<CalculatedParameterType> caps) {
	//		this.caps = caps;
	//	}

	public List<Integer> getImplementationContracts() {
		return implementationContracts;
	}

	public void setImplementationContracts(List<Integer> implementationContracts) {
		this.implementationContracts = implementationContracts;
	}

	public List<CalculatedParameterType> getCaps() {

		// TODO Auto-generated method stub
		return ac.getCalculatedParameters();
	}
}





