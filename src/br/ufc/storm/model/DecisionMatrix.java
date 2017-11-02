package br.ufc.storm.model;

import java.util.ArrayList;

public class DecisionMatrix {
	int sequence;
	private String alternative_name;
	private ArrayList<Criterion> criterionList = new ArrayList<>();
	private double list [][];
	
	
	public static void main(String [] a){
		DecisionMatrix d = new DecisionMatrix("TesteA");
		d.list = new double [3][3];
		d.criterionList.add(new Criterion(0, (float) 0.3, 150, "Criterio1"));
		d.criterionList.add(new Criterion(0, (float) 0.3, 151, "Criterio2"));
		d.criterionList.add(new Criterion(0, (float) 0.4, 152, "Criterio3"));
		int k=0;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				d.list[i][j] = k;
				k++;
			}
		}
//		System.out.println(d.toString());
		System.out.println(d.toAHP());
//		System.out.println(d.toWASPAS());
	}
	
	public DecisionMatrix(String alternative_name){
		this.alternative_name = alternative_name;
	}
	
	public double[][] getMatrix(){
		return list;
	}
	
	public String toString(){
		String str = "";
		int rows = list.length;
		int columns = criterionList.size();
		str+="Decision Matrix of component: "+alternative_name+"\n";
		str+="\n|";
		for(int k = 0; k < columns; k++){
			if(criterionList.get(k).getDomain()==5){
				str+= criterionList.get(k).getName()+"["+criterionList.get(k).getWeight()+"](DECREASING) | ";
			}else{
				str+= criterionList.get(k).getName()+"["+criterionList.get(k).getWeight()+"](INCREASING) | ";
			}
		}
		str+="\n\n";
		for(int i = 0; i < rows; i++){
			str+=" Alternative "+i+" | ";
			for(int j = 0; j < columns; j++){
				str+= getValue(i,j)+" | ";
			}
			str+="\n";
		}
		return str;
		
	}
	
	public String toWASPAS(){
		String str = "library(MCDM)\n";
		str+="d<-matrix(c(";
		int rows = list.length;
		int columns = criterionList.size();
		
		for(int i = 0; i < rows; i++){
//			str+=" Alternative "+i+" | ";
			for(int j = 0; j < columns; j++){
				str+= getValue(i,j);
				if(i<rows-1 || j < columns-1){
					str+=", ";
				}
			}
		}
		str+="),nrow = "+rows+",ncol = "+columns+")\n";
		str+="w <- c(";
		for(int k = 0; k < columns; k++){
			str+=criterionList.get(k).getWeight();
			if(k < columns-1){
				str+=", ";
			}
		}
		str+=")\n";
		str+="cb <- c(";
		for(int k = 0; k < columns; k++){
			if(criterionList.get(k).getDomain()==5){
				str+="\'min\'";
			}else{
				str+="\'max\'";
			}
			
			if(k < columns-1){
				str+=", ";
			}
		}
		str+=")\n";
		str+="lambda <- 0.5\n";
		str+="WASPAS(d,w,cb,lambda)";		
		return str;
		
	}
	
	public String toAHP(){
		String tab = "  ";
		String str = "Version: 1.0 \n\n######################### \n# Alternatives Section\n#\n\nAlternatives: &alternatives\n";
		int rows = list.length;
		int columns = criterionList.size();
		for(int i = 0; i < rows; i++){
			str+=tab+"Alternative"+i+":\n";
			for(int j = 0; j < columns; j++){
				str+=tab+tab+criterionList.get(j).getName()+": "+getValue(i,j)+"\n";
			}
//			str+="\n";
		}
		str+="#\n# End of Alternatives Section\n#####################################\n\n#####################################\n# Goal Section\n#\n\n\n";
		str+="Goal:\n";
		str+=tab+"name: "+alternative_name+"\n";
		str+=tab+"preferences: \n";
		str+=tab+tab+"priority: \n";
		for(int i = 0; i < criterionList.size(); i++){
			str+=tab+tab+tab+"- "+criterionList.get(i).getName()+": "+criterionList.get(i).getWeight()+"\n";
		}
//		 preferences:
//			    # preferences are typically defined pairwise
//			    # 1 means: A is equal to B
//			    # 9 means: A is highly preferrable to B
//			    # 1/9 means: B is highly preferrable to A
//			    pairwise:
//			      - [Cost, Safety, 3]
//			      - [Cost, Style, 7]
//			      - [Cost, Capacity, 3]
//			      - [Safety, Style, 9]
//			      - [Safety, Capacity, 1]
//			      - [Style, Capacity, 1/7]
//		str+=tab+tab+"children: \n";
//		for(int i = 0; i < criterionList.size(); i++){
//			str+=tab+tab+tab+criterionList.get(i).getName()+"\n";
//		}
		str+=tab+"children: *alternatives\n\n#\n# End of Goal Section\n#####################################";
		return str;
		
	}
	
	double getValue(int sequence, int criterion){
		try{
			return list[sequence][criterion];
		}catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	
	double getValueByCpId(int cp_id, int sequence){
		int seq = -1;
		for(Criterion c:criterionList){
			if(c.getCp_id()==cp_id){
				seq = c.getPosition();
			}
		}
		try{
			return list[sequence][seq];
		}catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	
	public boolean addCriterion(int domain, float weight, int cp_id, String name){
		try {
			criterionList.add(new Criterion(domain, weight, cp_id, name));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

		public void createMatrix(int numOfAlternatives, int numOfCriterion) {
		list = new double [numOfAlternatives][numOfCriterion];
	}

}

class Criterion{
	private int domain;
	private float weight;
	private int cp_id;
	private String name;
	private int position;
	
	public Criterion(int domain, float weight, int cp_id, String name){
		this.domain = domain;
		this.weight = weight;
		this.cp_id = cp_id;
		this.name = name;
	}
	
	public int getDomain() {
		return domain;
	}
	public void setDomain(int domain) {
		this.domain = domain;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public int getCp_id() {
		return cp_id;
	}
	public void setCp_id(int cp_id) {
		this.cp_id = cp_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
}