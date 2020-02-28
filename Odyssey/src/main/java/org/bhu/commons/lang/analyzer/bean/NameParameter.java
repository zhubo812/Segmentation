package org.bhu.commons.lang.analyzer.bean;

public class NameParameter {

	private double surname;
	private double middlechar;
	private double tailchar;
	
	
	
	public NameParameter(double surname, double middlechar, double tailchar) {
		super();
		this.surname = surname;
		this.middlechar = middlechar;
		this.tailchar = tailchar;
	}
	public double getSurname() {
		return surname;
	}
	public void setSurname(double surname) {
		this.surname = surname;
	}

	public double getMiddlechar() {
		return middlechar;
	}
	public void setMiddlechar(double middlechar) {
		this.middlechar = middlechar;
	}
	public double getTailchar() {
		return tailchar;
	}
	public void setTailchar(double tailchar) {
		this.tailchar = tailchar;
	}
	@Override
	public String toString() {
		return  surname + "\t"
				+ middlechar + "\t" + tailchar;
	}
	
	
}
