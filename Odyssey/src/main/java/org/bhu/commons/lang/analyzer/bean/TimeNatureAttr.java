package org.bhu.commons.lang.analyzer.bean;

public class TimeNatureAttr {

	public static final TimeNatureAttr NULL = new TimeNatureAttr();

	// 是否可能是一个数字
	public int timeFreq = -1;

	// 数字的结尾
	public int timeEndFreq = -1;

	// 最大词性是否是数字
	public boolean flag = false;

	public TimeNatureAttr() {
	}
}
