package br.ufc.storm.model;

public class MaxElement {

	private int count = 0;
	private Double value;
	
	public MaxElement(Double v) {
		setValue(v);
	}
	public int getCount() {
		return count;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
		count++;
	}
	
}
