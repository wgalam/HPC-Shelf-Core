package br.ufc.storm.mcdm;


public class Criterion{
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