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
		
		System.out.println(d.toString());
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