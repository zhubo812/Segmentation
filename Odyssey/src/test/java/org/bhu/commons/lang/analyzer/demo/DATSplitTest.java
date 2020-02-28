package org.bhu.commons.lang.analyzer.demo;

import org.bhu.commons.lang.analyzer.segment.impl.GetWordsImpl;
import org.junit.Test;

public class DATSplitTest {
	@Test
	public void test() {
		GetWordsImpl gwi = new GetWordsImpl();
		gwi.setStr("程序员祝海林和朱会震是在孙健的左面和右面.范凯在最右面.再往左是李松洪");
		String temp = null ;
		while((temp = gwi.allWords())!=null){
			System.out.println(temp);
		}
	}
}
