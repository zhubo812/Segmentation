package org.bhu.commons.lang.analyzer.bean;

public class NatureScore {

	int headwordIdx;
	int tailwordIdx;
	int headnatureIdx;
	int tailnatureIdx;
	double score;
	
	public int getHeadwordIdx() {
		return headwordIdx;
	}
	public void setHeadwordIdx(int headwordIdx) {
		this.headwordIdx = headwordIdx;
	}
	public int getTailwordIdx() {
		return tailwordIdx;
	}
	public void setTailwordIdx(int tailwordIdx) {
		this.tailwordIdx = tailwordIdx;
	}
	public int getHeadnatureIdx() {
		return headnatureIdx;
	}
	public void setHeadnatureIdx(int headnatureIdx) {
		this.headnatureIdx = headnatureIdx;
	}
	public int getTailnatureIdx() {
		return tailnatureIdx;
	}
	public void setTailnatureIdx(int tailnatureIdx) {
		this.tailnatureIdx = tailnatureIdx;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public NatureScore(int headwordIdx, int tailwordIdx, int headnatureIdx, int tailnatureIdx, double score) {
		super();
		this.headwordIdx = headwordIdx;
		this.tailwordIdx = tailwordIdx;
		this.headnatureIdx = headnatureIdx;
		this.tailnatureIdx = tailnatureIdx;
		this.score = score;
	}
	
	
	
}
