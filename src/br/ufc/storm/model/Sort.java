package br.ufc.storm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.ufc.storm.jaxb.CalculatedFunctionTermType;
import br.ufc.storm.jaxb.CandidateListType;

public class Sort {
	
	public static void main(String[] args) {
		ArrayList<Double> d = new ArrayList<>();
		d.add(2.5);
		d.add(1.5);
		d.add(3.0);
		d.add(4.5);
		d.add(0.5);
		System.out.println(sort(d));
	}
		
	public static String sort(ArrayList<Double> l){
		ArrayList<Par> list = new ArrayList<>();
		String s="";
		for(int i = 0; i < l.size(); i++){
			list.add(new Par(i, l.get(i)));
		}
		Collections.sort (list, new ComparatorDouble(false));
		for(Par p: list){
			s+=p.getOrder()+1+"\n";
		}
		return s;
	}
	
	public static String sort1(ArrayList<Par> l){
		ArrayList<Par> list = new ArrayList<>();
		String s="";
		for(int i = 0; i < l.size(); i++){
			list.add(new Par(i, l.get(i).getValue()));
		}
		Collections.sort (list, new ComparatorDouble(false));
		for(Par p: list){
			s+=p.getOrder()+1+"\n";
		}
		return s;
	}
	
	public static String sort(CandidateListType l){
		String s="";
		ArrayList<ArrayList<Par>> m = new ArrayList<>();
		int parameters = 0;
		int candidates = l.getCandidate().size();
		if(l.getCandidate().size() > 0){
			parameters = l.getCandidate().get(0).getRankingArguments().size();
		}
		
		
		for(int i = 0; i < parameters; i++){
			m.add(new ArrayList<>());
			for(int j =0; j < candidates; j++){
				m.get(i).add(new Par(j+1, l.getCandidate().get(j).getRankingArguments().get(i).getValue()));
			}
//			System.out.println(sort1(m.get(i))+"\n\n");
			Collections.sort (m.get(i), new ComparatorDouble(true));
		}

		for(int i=0; i < candidates; i++){
			for(int j = 0; j < parameters; j++){
				s+=m.get(j).get(i).getOrder()+"["+m.get(j).get(i).getValue()+"]"+" ";
			}
			s+="\n";
		}
			
		return s;
	}
	
}

class Par{
	private int order;
	private Double value;
	public Par(int order, Double value){
		this.order=order;
		this.value=value;		
	}
	public int getOrder(){
		return order;
	}
	public Double getValue(){
		return value;
	}
	
}


@SuppressWarnings("rawtypes")
class ComparatorDouble implements Comparator {  
	boolean crescente = true;  

	public ComparatorDouble(boolean crescente) {  
		this.crescente = crescente;  
	}  

	public int compare(Object o1, Object o2) {  
		Par p1 = (Par) o1;  
		Par p2 = (Par) o2;  
		if (crescente) {  
			return p1.getValue() < p2.getValue() ? -1 : (p1.getValue() > p2.getValue() ? +1 : 0);  
		} else {  
			return p1.getValue() < p2.getValue() ? +1 : (p1.getValue() > p2.getValue() ? -1 : 0);  
		}  
	}  
}  