package org.bhu.commons.lang.analyzer.util;

import java.util.List;
import java.util.regex.Pattern;

import org.bhu.commons.lang.analyzer.bean.Entity;
import org.bhu.commons.lang.analyzer.enamex.EntityHelper;

public class NQHelper extends EntityHelper{
	private static final String regStr = 
			"(([一二三四五六七八九十]+年)([一二三四五六七八九十零]+个月)?([一二三四五六七八九十零]+天)?)|"
					+ "(([一二三四五六七八九十两]|[1-9]{1,2})个(半|多)?月)" ;//数量词


	static {
		patterns = Pattern.compile(regStr);
	}
	
	public static List<Entity> getNumQ(String src){
		return getEntity(src);
	}
}
