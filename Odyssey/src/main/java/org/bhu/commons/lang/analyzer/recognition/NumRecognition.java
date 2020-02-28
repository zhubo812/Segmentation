package org.bhu.commons.lang.analyzer.recognition;

import org.bhu.commons.lang.analyzer.bean.Nature;
import org.bhu.commons.lang.analyzer.bean.Term;
import org.bhu.commons.lang.analyzer.dictionary.StaticDictionaryLoad;
import org.bhu.commons.lang.analyzer.util.CollectionUtil;
import org.bhu.commons.lang.analyzer.util.TermUtil;

public class NumRecognition {

	/**
	 * 数字+数字合并,Jackie
	 * 
	 * @param terms
	 */
	private static final String YEAR = "年";
	private static final String DI = "第";
	private static final String[] MilitaryUnits = {"班","排","连","营","团","旅","师","军"};
	public static void recognition(Term[] terms) {
		int length = terms.length - 1;
		Term from = null;
		Term to = null;
		Term temp = null;
		String tempName = null;
		for (int i = 0; i < length; i++) {
			if (terms[i] == null) {
				continue;
			} else if (".".equals(terms[i].getName()) || "．".equals(terms[i].getName())) {
				// 如果是.前后都为数字进行特殊处理
				to = terms[i].to();
				from = terms[i].from();
				if (from.termNatures().numAttr.flag && to.termNatures().numAttr.flag) {
					from.setName(from.getName() + "." + to.getName());
					TermUtil.termLink(from, to.to());
					terms[to.getOffe()] = null;
					terms[i] = null;
					i = from.getOffe() - 1;
				}
				continue;
			} else if (!terms[i].termNatures().numAttr.flag) {
				continue;
			}

			temp = terms[i];
			// 将所有的数字合并
			while ((temp = temp.to()).termNatures().numAttr.flag && !temp.getName().startsWith(DI)) {
				terms[i].setName(terms[i].getName() + temp.getName());
			}
			//判断结尾是军队
			if(CollectionUtil.EqualsOfAny(temp.getName(), MilitaryUnits)){
				terms[i].setName(terms[i].getName() + temp.getName());
				terms[i].setNature(Nature.NT);
				temp = temp.to();
			}
			// 如果是数字结尾
			if (StaticDictionaryLoad.isQuantifierRecognition && temp.termNatures().numAttr.numEndFreq > 0) {
				tempName = temp.getName();
				terms[i].setName(terms[i].getName() + tempName);
				int len = terms[i].getName().length();
				if(tempName.equals(YEAR)&& !(len==3||len==5)){
					terms[i].setNature(Nature.MQ);
				}
				else{
					terms[i].setNature(Nature.MQ);
				}
				terms[i].termNatures().personAttr.flag= false;
				temp = temp.to();
			}
			
			if(StaticDictionaryLoad.isTimeRecognition && temp.termNatures().timeAttr.timeEndFreq >0&&temp.getName().length()==1){
				terms[i].setName(terms[i].getName() + temp.getName());
				temp = temp.to();
				terms[i].setNature(Nature.T);
			}

			// 如果不等,说明terms[i]发生了改变
			if (terms[i].to() != temp) {
				TermUtil.termLink(terms[i], temp);
				// 将中间无用元素设置为null
				for (int j = i + 1; j < temp.getOffe(); j++) {
					terms[j] = null;
				}
				i = temp.getOffe() - 1;
			}
		}

	}

}
