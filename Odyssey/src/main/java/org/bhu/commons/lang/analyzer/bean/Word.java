package org.bhu.commons.lang.analyzer.bean;

public class Word{
	
	private String name;
	private String pos;
	
	public Word(String name, String pos){
		this.name= name;
		this.pos = pos;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	public String getPos() {
		// TODO Auto-generated method stub
		return this.pos;
	}
	
	public String toString(){
		return String.format("%s/%s", this.name, this.pos);
	}

}
