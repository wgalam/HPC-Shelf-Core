package br.ufc.storm.model;

import br.ufc.storm.jaxb.SliceType;

public class ACSLICE{
	public ACSLICE(int ac, SliceType sl) {
		super();
		this.ac = ac;
		this.sl = sl;
	}
	int ac;
	SliceType sl;
	public int getAc() {
		return ac;
	}
	public void setAc(int ac) {
		this.ac = ac;
	}
	public SliceType getSlice() {
		return sl;
	}
	public void setSlice(SliceType sl) {
		this.sl = sl;
	}



}