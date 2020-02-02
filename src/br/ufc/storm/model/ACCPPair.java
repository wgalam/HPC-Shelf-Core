package br.ufc.storm.model;

import br.ufc.storm.jaxb.ContextParameterType;

public class ACCPPair{
	public ACCPPair(int ac, Object cp, int parameter_type) {
		super();
		this.ac = ac;
		this.cp = cp;
		this.parameter_type = parameter_type;
	}
	int ac;
	Object cp;
	int parameter_type;
	public int getAc() {
		return ac;
	}
	public void setAc(int ac) {
		this.ac = ac;
	}
	public Object getCp() {
		return cp;
	}
	public void setCp(Object cp) {
		this.cp = cp;
	}
	public int getParameter_type() {
		return parameter_type;
	}
	public void setParameter_type(int parameter_type) {
		this.parameter_type = parameter_type;
	}
	
}