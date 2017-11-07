package br.ufc.storm.mcdm;

import java.io.IOException;
import java.util.ArrayList;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class DecisionMatrix {
	int sequence;
	private String alternative_name;
	private ArrayList<Criterion> criterionList = new ArrayList<>();
	private double list [][];
	RConnection c;


	public static void main(String [] a){
		DecisionMatrix d = new DecisionMatrix("TesteA");
		d.list = new double [3][3];
		d.criterionList.add(new Criterion(5, (float) 0.3, 150, "Criterio1"));
		d.criterionList.add(new Criterion(5, (float) 0.3, 151, "Criterio2"));
		d.criterionList.add(new Criterion(5, (float) 0.4, 152, "Criterio3"));
		int k=0;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				d.list[i][j] = k;
				System.out.print(k+" ");
				k++;
			}
			System.out.println("");
		}
		d.evalMMOORA();
		d.evalTOPSISVector();

		System.out.println(d.toRmcdmFunction("TOPSISLinear"));
	}

	public DecisionMatrix(String alternative_name){
		this.alternative_name = alternative_name;
	}

	public double[][] getMatrix(){
		return list;
	}

	public double[] getWeight(){
		double x[] = new double[criterionList.size()];
		for(int i=0; i < x.length; i++){
			x[i]=Double.parseDouble(criterionList.get(i).getWeight()+"");
		}
		return x;
	}
	
	public int[] evalMMOORA(){
		return evalMCDMmethod("MMOORA", "MultiMooraRanking");
	}
	
	public int[] evalTOPSISLinear(){
		return evalMCDMmethod("TOPSISLinear", "Ranking");
	}
	
	public int[] evalTOPSISVector(){
		return evalMCDMmethod("TOPSISVector", "Ranking");
	}
	
	public int[] evalMCDMmethod(String method, String column){

		double w [] = new double[criterionList.size()];
		String cb [] = new String[criterionList.size()];
		try {
			c = new RConnection();
			c.voidEval("library(MCDM)");
			c.assign("d", REXP.createDoubleMatrix(list));
			for(int i = 0; i < criterionList.size(); i++){
				w[i]=Double.parseDouble(criterionList.get(i).getWeight()+"");

				if(criterionList.get(i).getDomain()==5){
					cb[i]="min";
				}else{
					cb[i]="max";
				}
			}
			c.assign("w", w);
			c.assign("cb", cb);
			REXP rResponseObject = c.parseAndEval("try(eval("+method+"(d,w,cb)"+"),silent=TRUE)"); 
			if (rResponseObject.inherits("try-error")) { 
				System.out.println(rResponseObject.asString());; 
			}
			RList l = rResponseObject.asList();
			int[] ret = l.at(column).asIntegers();
//			System.out.println(method+" Ranking");
//			for(Integer i: ret){
//				System.out.println(i);
//			}
//			System.out.println("----------------------");
			return l.at(column).asIntegers();
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (REngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int[] evalMCDMmethod(String method, int column){

		double w [] = new double[criterionList.size()];
		String cb [] = new String[criterionList.size()];
		try {
			c = new RConnection();
			c.voidEval("library(MCDM)");
			c.assign("d", REXP.createDoubleMatrix(list));
			for(int i = 0; i < criterionList.size(); i++){
				w[i]=Double.parseDouble(criterionList.get(i).getWeight()+"");

				if(criterionList.get(i).getDomain()==5){
					cb[i]="min";
				}else{
					cb[i]="max";
				}
			}
			c.assign("w", w);
			c.assign("cb", cb);
			REXP rResponseObject = c.parseAndEval("try(eval("+method+"(d,w,cb)"+"),silent=TRUE)"); 
			if (rResponseObject.inherits("try-error")) { 
				System.out.println(rResponseObject.asString());; 
			}
			RList l = rResponseObject.asList();
			int[] ret = l.at(column).asIntegers();
			System.out.println(method+" Ranking");
			for(Integer i: ret){
				System.out.println(i);
			}
			System.out.println("----------------------");
			return l.at(column).asIntegers();
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (REngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String toString(){
		String str = "";
		int rows = list.length;
		int columns = criterionList.size();
		str+="Decision Matrix of component: "+alternative_name+"\n";
		str+="\n| ";
		for(int k = 0; k < columns; k++){
			if(criterionList.get(k).getDomain()==5){
				str+= criterionList.get(k).getName()+"["+criterionList.get(k).getWeight()+"](DECREASING) | ";
			}else{
				str+= criterionList.get(k).getName()+"["+criterionList.get(k).getWeight()+"](INCREASING) | ";
			}
		}
		str+="\n\n";
		for(int i = 0; i < rows; i++){
			str+=i+" & ";
			for(int j = 0; j < columns; j++){
				str+= getValue(i,j)+" & ";
			}
			str+="\n";
		}
		return str;

	}

	
	public String toRmcdmFunction(String func){
		String str = "library(MCDM)\n";
		str+="d<-matrix(c(";
		int rows = list.length;
		int columns = criterionList.size();

		for(int j = 0; j < columns; j++){
			for(int i = 0; i < rows; i++){
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
		str+=func+"(d, w, cb)";		
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