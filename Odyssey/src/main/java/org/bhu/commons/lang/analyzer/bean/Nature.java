package org.bhu.commons.lang.analyzer.bean;

import org.bhu.commons.lang.analyzer.library.NatureLibrary;

/**
 * 这里面封装了一些基本的词性.
 * 
 * @author Jackie
 * 
 */
public class Nature {
	// 词性的名称
	public final String natureStr;
	// 词性对照表的位置
	public final int index;
	// 词性的下标值
	public final int natureIndex;
	// 词性的频率
	public final int allFrequency;

	public static final Nature NW = NatureLibrary.getNature("nw");

	public static final Nature NRF = NatureLibrary.getNature("nrf");
	
	public static final Nature NRY = NatureLibrary.getNature("nry");

	public static final Nature NR = NatureLibrary.getNature("nr");
	
	public static final Nature NT = NatureLibrary.getNature("nt");
	
	public static final Nature MQ = NatureLibrary.getNature("mq");
	
	public static final Nature T = NatureLibrary.getNature("t");
	
	public static final Nature M = NatureLibrary.getNature("m");
	
	public static final Nature NS = NatureLibrary.getNature("ns");

	public static final Nature NULL = NatureLibrary.getNature("null");

	public Nature(String natureStr, int index, int natureIndex, int allFrequency) {
		this.natureStr = natureStr;
		this.index = index;
		this.natureIndex = natureIndex;
		this.allFrequency = allFrequency;
	}

	public Nature(String natureStr) {
		this.natureStr = natureStr;
		this.index = 0;
		this.natureIndex = 0;
		this.allFrequency = 0;
	}

	@Override
	public String toString() {
		return natureStr + ":" + index + ":" + natureIndex;
	}
}