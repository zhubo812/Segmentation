package org.bhu.commons.lang.analyzer.demo;

import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.bhu.commons.lang.analyzer.bean.Term;
import org.bhu.commons.lang.analyzer.library.UserDefineLibrary;
import org.bhu.commons.lang.analyzer.segment.analysis.ToAnalysis;
import org.bhu.commons.lang.analyzer.segment.analysis.UserDefineAnalysis;
import org.bhu.commons.lang.analyzer.util.FilterModifWord;
import org.junit.Test;

/***
 * 动态添加用户词典
 * @author Jackie
 *
 */
@SuppressWarnings("deprecation")
public class UserDefinedAnalysisTest {

	@Test
	public void test() {
		String newWord = "爸爸去哪儿";
		String nature = "aaaaa";
		String nw = "mh370";
		String na="key";
//		String nw2 = "维生素b2";
		String str = "维生素b2的查一下完全没有从来没哪个国家13个苹果上海电力2012年财务报表如下怎爸爸去哪儿么办mh370";
		
		//增加新词
		UserDefineLibrary.insertWord(newWord, nature, 1000);
//		UserDefineLibrary.insertWord(nw2, nature, 1000);
		UserDefineLibrary.insertWord(nw, na, 1000);
		UserDefineLibrary.insertWord("国家", "sentiment", 1000);
		List<Term> parse = ToAnalysis.parse(str);
		parse = FilterModifWord.modifResult(parse);
		System.out.println(parse);
		HashMap<String, Term> hs = new HashMap<String, Term>();
		for (Term term : parse) {
			hs.put(term.getName(), term);
		}

		Assert.assertTrue(hs.containsKey(newWord));

		Assert.assertEquals(hs.get(newWord).natrue().natureStr, nature);

		//删除词
		UserDefineLibrary.removeWord(newWord);
		parse = ToAnalysis.parse(str);
		System.out.println(parse);
		hs = new HashMap<String, Term>();
		for (Term term : parse) {
			hs.put(term.getName(), term);
		}

		Assert.assertTrue(!hs.containsKey(newWord));

	}
	
	@Test
	public void UsrFilter(){
		String src = "这么的维生素b2的查一下完全没有从来没哪个国家13个苹果上海电力2012年财务报表如下怎爸爸去哪儿么办mh370";
//		FilterModifWord.initUsrLib();
		FilterModifWord.initStopwords();
		String sline = FilterModifWord.modifResult(ToAnalysis.parse(src)).toString();
		System.out.println(sline);
	}
}
