package org.bhu.commons.lang.analyzer.bean;

public class NumNatureAttr {

	public static final NumNatureAttr NULL = new NumNatureAttr();

	// 是有可能是一个数字
	public int numFreq = -1;

	// 数字的结尾(是否为量词，-1表示不是量词)
	public int numEndFreq = -1;

	// 最大词性是否是数字
	public boolean flag = false;

	public NumNatureAttr() {
	}
}
