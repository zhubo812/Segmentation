package org.bhu.commons.lang.analyzer.enamex;

import java.util.HashMap;

import org.bhu.commons.lang.analyzer.bean.EasternAsianName;
import org.bhu.commons.lang.analyzer.bean.NameParameter;
import org.bhu.commons.lang.analyzer.bean.Nature;
import org.bhu.commons.lang.analyzer.bean.Term;
import org.bhu.commons.lang.analyzer.bean.TermNature;
import org.bhu.commons.lang.analyzer.util.TermUtil;

public class EasternAsianNameHelper {

	private static HashMap<String, NameParameter> asianNameMap = EasternAsianName.asianNameMap;

	public EasternAsianNameHelper() {
	}

	public static void recognition(Term[] terms) {
		recogntion_(terms);
	}

	private static void recogntion_(Term[] terms) {
		Term term = null;
		for (int i = 0; i < terms.length; i++) {
			term = terms[i];
			if (term == null || !term.termNatures().personAttr.flag) {
				continue;
			}
			if (term.termNatures().personAttr.isSurname) {
//				try {
					getSurname(terms, i);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
////					e.printStackTrace();
//					for(Term t : terms){
//						System.err.print(t.toString());
//					}
//					System.out.println();
//				}
			}
		}

	}

	private static void getSurname(Term[] terms, int index) {
		String surname = terms[index].getName();
		int len = 0;
		int tolen = 0;
		double tuNameProp = 0;
		double triNameProp = 0;
		Term temp = null;
		for (int i = index; i < terms.length - 2; i++) {
			if(terms[i]==null){
				continue;
			}
			temp = terms[i].to();
			if(temp==null){
				continue;
			}
			if(temp.getName().equals(TermNature.END.nature.natureStr)||i>index+2){
				break;
			}
			len = temp.getName().length();

			switch (len) {
			case 1:
				tuNameProp = getValue(surname, null, temp.getName());
				boolean tosurname=false;
				if(temp.to()!=null){
				tosurname = temp.to().termNatures().personAttr.isSurname;
				}
				if (tolen == 1 && !tosurname) {
					String tempto = temp.to().getName();
					tolen = tempto.length();
					triNameProp = getValue(surname, temp.getName(), tempto);
				}
				if (triNameProp > 0.01) {

					while (i < index + 2) {
						terms[index].setName(terms[index].getName() + temp.getName());
						i++;
						temp = terms[i].to();
					}
					terms[index].setNature(Nature.NR);
					if (terms[index].to() != temp) {
						TermUtil.termLink(terms[index], temp);
						// 将中间无用元素设置为null
						for (int j = index + 1; j < temp.getOffe(); j++) {
							terms[j] = null;
						}
						index = temp.getOffe() - 1;
					}

					return;
				} else {
					if (tuNameProp > 0.01) {
						terms[i].setName(terms[i].getName() + temp.getName());
						terms[i].setNature(Nature.NR);
						
						temp = temp.to();
						if (terms[i].to() != temp) {
							TermUtil.termLink(terms[i], temp);
							// 将中间无用元素设置为null
							for (int j = i + 1; j < temp.getOffe(); j++) {
								terms[j] = null;
							}
							i = temp.getOffe() - 1;
						}
						return;
					}
				}
				return;
			case 2:
				triNameProp = getValue(surname, temp.getName().substring(0, 1), temp.getName().substring(1));
				if (triNameProp > 0) {
					terms[i].setName(terms[i].getName() + temp.getName());
					terms[i].setNature(Nature.NR);
					temp = temp.to();
					if (terms[i].to() != temp) {
						TermUtil.termLink(terms[i], temp);
						// 将中间无用元素设置为null
						for (int j = i + 1; j < temp.getOffe(); j++) {
							terms[j] = null;
						}
						i = temp.getOffe() - 1;
					}
					return;
				}
				break;
			default:
				break;
			}
		}

		// if(triNameProp>0){
		//
		// }
	}

	private static double getValue(String surname, String middleName, String tailName) {
		double propsur = 0;
		double propmid = 0;
		double proptail = 0;
		if (middleName != null) {
			if(asianNameMap.containsKey(surname)){
			propsur = asianNameMap.get(surname).getSurname();
			}
			if(asianNameMap.containsKey(middleName)){
			propmid = asianNameMap.get(middleName).getMiddlechar();
			}
			if(asianNameMap.containsKey(tailName)){
			proptail = asianNameMap.get(tailName).getTailchar();
			}
		} else {
			if(asianNameMap.containsKey(surname)){
				propsur = asianNameMap.get(surname).getSurname();
				}
				if(asianNameMap.containsKey(middleName)){
				propmid = asianNameMap.get(middleName).getMiddlechar();
				}
				if(asianNameMap.containsKey(tailName)){
				proptail = asianNameMap.get(tailName).getTailchar();
				}
			if (propmid > 0) {
				return (propsur * 100) * (propmid * 100);
			} else if (proptail > 0) {
				return (propsur * 100) * (proptail * 100);
			}
		}
		return (propsur * 100) * (propmid * 100) * (proptail * 100);

	}
}
