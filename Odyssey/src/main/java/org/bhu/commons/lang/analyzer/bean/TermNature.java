package org.bhu.commons.lang.analyzer.bean;

import org.bhu.commons.lang.analyzer.library.NatureLibrary;

/**
 * 一个词里面会有一些词性
 * 
 * @author Jackie
 */
public class TermNature {
	/**
	 * 系统内置的几个
	 */
	public static final TermNature M = new TermNature("m", 1);
	public static final TermNature EN = new TermNature("en", 1);
	public static final TermNature RU = new TermNature("ru", 1);
	public static final TermNature GR = new TermNature("gr", 1);
	public static final TermNature BEGIN = new TermNature("始##始", 1);
	public static final TermNature END = new TermNature("末##末", 1);
	public static final TermNature USER_DEFINE = new TermNature("userDefine", 1);
	public static final TermNature NR = new TermNature("nr", 1);
	public static final TermNature NRY = new TermNature("nry", 1);
	public static final TermNature NT = new TermNature("nt", 1);
	public static final TermNature NW = new TermNature("nw", 1);
	public static final TermNature W = new TermNature("w", 1);
	public static final TermNature T = new TermNature("t", 1);
	public static final TermNature MQ = new TermNature("mq", 1);
	public static final TermNature NULL = new TermNature("x", 1);

	public Nature nature;

	public int frequency;

	public TermNature(String natureStr, int frequency) {
		this.nature = NatureLibrary.getNature(natureStr);
		this.frequency = frequency;
	}

	public static TermNature[] setNatureStrToArray(String natureStr) {
		natureStr = natureStr.substring(1, natureStr.length() - 1);
		String[] split = natureStr.split(",");
		String[] strs = null;
		Integer frequency = null;
		TermNature[] all = new TermNature[split.length];
		for (int i = 0; i < split.length; i++) {
			strs = split[i].split("=");
			frequency = Integer.parseInt(strs[1]);
			all[i] = new TermNature(strs[0].trim(), frequency);
		}
		return all;
	}

	@Override
	public String toString() {
		return this.nature.natureStr + "/" + frequency;
	}
}
