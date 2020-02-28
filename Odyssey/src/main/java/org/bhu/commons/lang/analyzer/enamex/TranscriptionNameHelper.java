package org.bhu.commons.lang.analyzer.enamex;

import java.util.HashSet;
import java.util.Set;

import org.bhu.commons.lang.analyzer.bean.Nature;
import org.bhu.commons.lang.analyzer.bean.Term;
import org.bhu.commons.lang.analyzer.bean.TermNature;
import org.bhu.commons.lang.analyzer.util.TermUtil;

public class TranscriptionNameHelper {

	private static String ForeignBegin = "阿埃艾爱安昂奥巴白拜班邦包保葆鲍贝本彼毕宾波玻伯勃博布蔡查柴昌彻楚聪" + "达代戴黛丹道德登邓狄迪帝蒂丁杜厄恩法范方菲费芬丰佛弗福富盖甘冈高戈"
			+ "哥格葛根贡古顾瓜圭哈海汉杭豪赫黑亨侯胡华怀惠霍基吉加嘉贾简江焦杰金津久" + "卡开凯坎康考柯科克肯孔库夸匡奎魁昆拉腊莱赖兰朗劳勒雷莉李里理丽利列林琳"
			+ "刘柳龙隆露卢鲁路吕伦罗洛马玛迈麦曼芒梅门蒙孟米密明缪摩莫墨默姆穆内纳奈南" + "瑙妮尼宁牛纽农努诺欧帕派潘庞培佩彭皮平珀朴普齐奇契恰钱乔切钦琼丘邱屈"
			+ "瑞若撒萨塞赛桑瑟森沙莎尚绍舍申圣施什史舒朔思斯松苏所索塔泰坦汤唐陶特滕" + "田铁廷通图托瓦万旺威韦维魏温翁沃乌伍武西希悉锡席夏香肖谢辛欣休许雪雅亚"
			+ "延扬尧耶叶伊易因英尤于雨约赞泽曾扎詹珍朱卓兹宗祖佐胥";

	private static String ForeignMid = "阿埃艾爱安昂奥巴柏拜班邦保葆鲍北贝倍本比彼陛辟别宾滨波伯博卜布蔡策岑查" + "柴彻楚茨慈次存措达代戴黛丹当道得德登邓迪狄地第蒂丁东杜顿多厄恩尔耳伐法方菲"
			+ "斐费芬丰冯夫弗福辅富盖甘高戈哥格各根贡古瓜圭哈海罕汉翰豪诃赫黑亨洪胡华怀" + "惠霍基吉季加佳贾焦杰金津久居喀卡开凯坎康考柯科可克肯孔扣库夸匡奎昆阔拉"
			+ "腊莱赉赖兰朗劳乐勒雷蕾累莉里力历立丽利连廉列烈林琳龄留柳龙隆卢鲁路吕律略伦" + "罗萝洛马玛迈曼芒茅梅美门蒙米密敏明缪谟摩莫默姆木穆拿内纳那娜乃奈南瑙嫩能妮"
			+ "尼年涅宁纽农努诺欧帕派潘庞佩彭蓬丕皮匹平泼珀普浦齐奇契恰强乔切钦琴青琼" + "丘热儒撒萨塞赛桑瑟森沙莎山珊尚绍舍申盛施什石史士寿舒丝思斯松苏索塔太泰"
			+ "坦汤唐陶特滕提惕铁汀廷通图托瓦万旺威薇韦维伟卫温文翁沃乌吴伍武晤西希锡" + "席夏香晓肖歇谢辛欣兴休修胥雅亚延扬尧耶叶伊依易因尤约宰赞泽曾扎詹章真治朱卓" + "兹祖佐·藉";

	private static String ForeignEnd = "阿埃艾爱安昂奥巴白拜班邦堡贝本比毕庇边宾波伯勃博卜布采藏策岑查察彻垂茨达" + "大戴丹当得德登迪底第蒂丁东杜敦顿多厄恩尔耳法凡范方菲费芬丰冯佛夫弗福盖"
			+ "甘干冈高戈哥格根艮贡古圭果哈海罕汉翰豪赫黑亨洪胡华惠霍基吉加嘉贾姜杰捷金" + "津京鸠卡凯坎康考科克肯孔库夸奎昆拉腊来莱赖兰朗劳乐勒雷蔂莉黎里丽利莲廉"
			+ "列烈林琳麟柳龙隆卢鲁吕略伦罗洛马玛迈满曼芒梅门蒙米明缪摩莫默姆穆内纳娜" + "奈南瑙嫩妮尼涅宁纽农努诺欧帕潘庞佩彭蓬皮平珀普齐奇契恰乔切钦琴琼丘"
			+ "日荣茹瑞若撒萨塞赛桑瑟森沙莎山珊尚绍舍申生施什士舒丝思斯松苏孙索塔太泰坦" + "唐特滕藤汀廷亭通透图托脱娃瓦万旺威薇韦维温文翁沃乌西希锡霞夏香肖歇谢辛"
			+ "欣休修煦逊亚娅延扬耶叶伊易因音英尤约赞泽曾扎治兹宗佐脱";

	private static char[] HeadChar = ForeignBegin.toCharArray();
	private static char[] MidChar = ForeignMid.toCharArray();
	private static char[] EndChar = ForeignEnd.toCharArray();

	private static Set<Character> HeadSet = new HashSet<Character>();
	private static Set<Character> MidSet = new HashSet<Character>();
	private static Set<Character> TailSet = new HashSet<Character>();

	static {
		load(HeadChar, HeadSet);
		load(MidChar, MidSet);
		load(EndChar, TailSet);
	}

	private static void load(char[] ch, Set<Character> set) {
		for (char c : ch) {
			set.add(c);
		}
	}

	private static boolean isHeadChar(char c) {
		return HeadSet.contains(c);
	}

	private static boolean isMidChar(char c) {
		return MidSet.contains(c);
	}

	private static boolean isTailChar(char c) {
		return TailSet.contains(c);
	}

	public static void recognition(Term[] terms) {
		StringBuilder sbName = new StringBuilder();
		int appendTimes = 0;
		int index = 0;

		boolean flag = false;
		for (int i = 0; i < terms.length; i++) {
			if (terms[i] == null || terms[i].natrue() == Nature.NS) {
				continue;
			}
//			if(terms[i].getName().equals(TermNature.END.nature.natureStr)){
//				break;
//			}
			Nature nature = terms[i].natrue();
			char c = terms[i].getName().charAt(0);
			int len = terms[i].getName().length();
			// 这里需要判断当前词是否为音译人名、音译地名、单汉字且为HeadSet中
			if (appendTimes == 0 && ((nature == Nature.NRY || nature == Nature.NR )|| (isHeadChar(c)&&len==1))) {
				if (len==1) {
					sbName.append(terms[i].getName());
					appendTimes++;
				} else {
					String temp = terms[i].getName();
					sbName.append(temp.charAt(0));
					appendTimes++;
					for (int j = 1; j < temp.length(); j++) {
						if (isMidChar(temp.charAt(j))) {
							sbName.append(temp.charAt(j));
							appendTimes++;
						} else if (isTailChar(temp.charAt(j))) {
							sbName.append(temp.charAt(j));
							appendTimes++;
							flag = true;
						}
						else{
							appendTimes=0;
							sbName.delete(0, sbName.length());
							break;
						}
					}
				}
				index = i;
			}

			else if (appendTimes > 0) {
				String temp = terms[i].getName();
				for (int j = 0; j < temp.length(); j++) {
					if (isMidChar(temp.charAt(j))) {
						sbName.append(temp.charAt(j));
						appendTimes++;
						if (isTailChar(temp.charAt(j))) {
							flag = true;
						}
					} else if (isTailChar(temp.charAt(j))) {
						sbName.append(temp.charAt(j));
						appendTimes++;
						flag = true;
					} else {
						if (appendTimes > 1 && flag) {
							terms[index].setName(sbName.toString());
							terms[index].setNature(Nature.NRY);
							TermUtil.termLink(terms[index], terms[i]);
							// 将中间无用元素设置为null
							for (int k = index + 1; k < terms[i].getOffe(); k++) {
								terms[k] = null;
							}
							appendTimes = 0;
							sbName.delete(0, sbName.length());
							break;
						} else {
							appendTimes = 0;
							sbName.delete(0, sbName.length());
							break;
						}
					}
				}
			}
		}
	}

}
