package org.bhu.commons.lang.analyzer.util;

import java.util.List;
import java.util.regex.Pattern;

import org.bhu.commons.lang.analyzer.bean.Entity;
import org.bhu.commons.lang.analyzer.enamex.EntityHelper;

public class TimeHelper extends EntityHelper{

	private static final String regStr = 
			"(((上|这|本|下)+(周|星期)([一二三四五六天日]|[1-6])?)(周中|早|晚|前|后|中午)|"
//			+ "(([一二三四五六七八九十两]|[1-9]{1,2})个(半|多)?月)|" 
			+ "(((上|下)个?|本|中|新\\d*)世纪((末|中期|中叶|前期|初期|后期)?))|" 
			+ "((下|上)个?|本])(赛季)|" 
			+ "(今|去|前)(年)?(年底)|"
			+ "((早些|某个|晚间|前些|这(个)?)+时候))|"
			+ "(((19|20)?[\\d]{2}年)?((([一二三四五六七八九十]{1,2})|([0-9]{1,2}))月(底|初)?)(([一二三四五六七八九十]+|[0-9]{1,2})(日|号))?(前|后)?)|"
			+ "(!第([一二三四五六七八九十]+|[\\d]{1,2})(日|号)(前|后|左右)?)|" 
			+ "((前|昨|今|明|后)(天|日)?(|早|晚)(晨|上|间)?)|"
			+ "(((([二十]{1,2})?[一三四五六七八九]+)|[0-9]){1,2}(时|点)((([一二三四五六七八九十]+)|[0-9]{1,2})分)?([0-9]{1,2}秒)?)|"
			+ "((19|20)?[\\d]{2}年)";
//			+ "([〇零一二三四五六七八九十百千万]+(年))";

	static {
		patterns = Pattern.compile(regStr);
	}

	public static List<Entity> getTimex(String src){
		return getEntity(src);
	}

}
