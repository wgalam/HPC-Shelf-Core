package br.ufc.storm.model;

import br.ufc.storm.jaxb.AbstractUnitType;

public class ACAUNIT{
	public ACAUNIT(int ac, AbstractUnitType aut) {
		super();
		this.ac = ac;
		this.aut = aut;
	}
	int ac;
	AbstractUnitType aut;
	public int getAc() {
		return ac;
	}
	public void setAc(int ac) {
		this.ac = ac;
	}
	public AbstractUnitType getAUT() {
		return aut;
	}
	public void setAUT(AbstractUnitType aut) {
		this.aut = aut;
	}



}