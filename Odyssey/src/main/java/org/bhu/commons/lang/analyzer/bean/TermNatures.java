package org.bhu.commons.lang.analyzer.bean;

/***
 * 
 * @ClassName: TermNatures
 * @Description: TODO() 每一个term都对应一个词性集合
 * @author Jackie zhubo812@gmail.com
 * @date 2015年8月11日 下午4:31:15
 * 
 */
public class TermNatures {

	public static final TermNatures M = new TermNatures(TermNature.M);
	
	public static final TermNatures MQ = new TermNatures(TermNature.MQ);

	public static final TermNatures NR = new TermNatures(TermNature.NR);
	
	public static final TermNatures NRY = new TermNatures(TermNature.NRY);

	public static final TermNatures EN = new TermNatures(TermNature.EN);

	public static final TermNatures RU = new TermNatures(TermNature.RU);

	public static final TermNatures GR = new TermNatures(TermNature.GR);

	public static final TermNatures END = new TermNatures(TermNature.END,
			50610, -1);

	public static final TermNatures BEGIN = new TermNatures(TermNature.BEGIN,
			50610, 0);

	public static final TermNatures NT = new TermNatures(TermNature.NT);

	public static final TermNatures NW = new TermNatures(TermNature.NW);

	public static final TermNatures T = new TermNatures(TermNature.T);

	public static final TermNatures W = new TermNatures(TermNature.W);

	public static final TermNatures NULL = new TermNatures(TermNature.NULL);;

	/***
	 * 关于这个term的所有词性
	 */
	public TermNature[] termNatures = null;

	/***
	 * 数字属性
	 */
	public NumNatureAttr numAttr = NumNatureAttr.NULL;

	/**
	 * 人名词性
	 */
	public PersonNatureAttr personAttr = PersonNatureAttr.NULL;

	/**
	 * 人名词性
	 */
	public TimeNatureAttr timeAttr = TimeNatureAttr.NULL;

	/**
	 * 默认词性
	 */
	public Nature nature = null;

	/**
	 * 所有的词频
	 */
	public int allFreq = 0;

	/**
	 * 词的id
	 */
	public int id = -2;

	/**
	 * 构造方法.一个词对应这种玩意
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param termNatures
	 * @param id
	 */
	public TermNatures(TermNature[] termNatures, int id) {
		this.id = id;
		this.termNatures = termNatures;
		// find maxNature
		int maxFreq = -1;
		TermNature termNature = null;
		for (int i = 0; i < termNatures.length; i++) {
			if (maxFreq < termNatures[i].frequency) {
				maxFreq = termNatures[i].frequency;
				termNature = termNatures[i];
			}
		}

		if (termNature != null) {
			this.nature = termNature.nature;
		}

		serAttribute();
	}

	public TermNatures(TermNature termNature) {
		termNatures = new TermNature[1];
		this.termNatures[0] = termNature;
		this.nature = termNature.nature;
		serAttribute();
	}

	public TermNatures(TermNature termNature, int allFreq, int id) {
		this.id = id;
		termNatures = new TermNature[1];
		termNature.frequency = allFreq;
		this.termNatures[0] = termNature;
		this.allFreq = allFreq;
	}

	private void serAttribute() {
		TermNature termNature = null;
		int max = 0;
		NumNatureAttr numNatureAttr = null;
		TimeNatureAttr timeNatureAttr = null;
		PersonNatureAttr asianNameNatureAttr = null;
		for (int i = 0; i < termNatures.length; i++) {
			termNature = termNatures[i];
			allFreq += termNature.frequency;
			max = Math.max(max, termNature.frequency);
			switch (termNature.nature.index) {
			case 18:// 数词
				if (numNatureAttr == null) {
					numNatureAttr = new NumNatureAttr();
				}
				numNatureAttr.numFreq = termNature.frequency;
				break;
			case 29:// 量词
				if (numNatureAttr == null) {
					numNatureAttr = new NumNatureAttr();
				}
				numNatureAttr.numEndFreq = termNature.frequency;
				break;
			case 33:// 时间词
				if (timeNatureAttr == null) {
					timeNatureAttr = new TimeNatureAttr();
				}
				timeNatureAttr.timeEndFreq = termNature.frequency;
				break;
			case 48://日本姓氏
				if (asianNameNatureAttr == null) {
					asianNameNatureAttr = new PersonNatureAttr();
				}
				break;
			case 51:
				if (asianNameNatureAttr == null) {
					asianNameNatureAttr = new PersonNatureAttr();
				}
//				 asianNameNatureAttr.allFreq = termNature.frequency;
				 break;
			}
		}
		if (numNatureAttr != null) {
			if (max == numNatureAttr.numFreq) {
				numNatureAttr.flag = true;
			}
			this.numAttr = numNatureAttr;
		}
		if (timeNatureAttr != null) {
			if (max == timeNatureAttr.timeFreq) {
				timeNatureAttr.flag = true;
			}
			timeNatureAttr.flag = true;
			this.timeAttr = timeNatureAttr;
		}
		if (asianNameNatureAttr != null) {
			asianNameNatureAttr.isSurname = true;
			this.personAttr = asianNameNatureAttr;
		}
	}

	public void setPersonNatureAttr(PersonNatureAttr personAttr) {
		if(this.personAttr.isSurname){
			personAttr.isSurname = true;
		}
		this.personAttr = personAttr;
	}

}
