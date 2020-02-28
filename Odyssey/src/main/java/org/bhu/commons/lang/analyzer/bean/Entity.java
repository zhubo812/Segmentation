package org.bhu.commons.lang.analyzer.bean;

public class Entity {

	String expression;
	int startIndx;
	int endIndex;
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public int getStartIndx() {
		return startIndx;
	}
	public void setStartIndx(int startIndx) {
		this.startIndx = startIndx;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	public Entity(String expression, int startIndx, int endIndex) {
		super();
		this.expression = expression;
		this.startIndx = startIndx;
		this.endIndex = endIndex;
	}
	public Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
