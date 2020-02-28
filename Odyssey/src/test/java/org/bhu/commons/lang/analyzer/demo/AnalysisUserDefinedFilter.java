package org.bhu.commons.lang.analyzer.demo;

import java.util.ArrayList;
import java.util.List;

import org.bhu.commons.lang.analyzer.bean.Term;
import org.bhu.commons.lang.analyzer.library.UserDefineLibrary;
import org.bhu.commons.lang.analyzer.segment.analysis.ToAnalysis;
import org.bhu.commons.lang.analyzer.util.FilterModifWord;
import org.junit.Test;

/**
 * 
 * @ClassName: AnalysisUserDefinedFilter
 * @Description: add user defined dictionary and stop words
 *
 */
public class AnalysisUserDefinedFilter {

	

	

	
	@Test
	public void toAnalysis() {
		String nature = "key";
		String sentiNature = "sentiment";
		String[] stopNatures = { "w", "r", "d", "mq", "null" };
		FilterModifWord.initStopwords();
		FilterModifWord.insertStopNatures(stopNatures);
		List<String> keywords = new ArrayList<String>();
		keywords.add("马来西亚政府");
		keywords.add("乌克兰政府");
		UserDefineLibrary.insertWord(keywords, nature, 1000);
		String sentence = "没有美国马来西亚政府乌克兰政府";
		

		List<Term> words = ToAnalysis.parse(sentence);
		words = FilterModifWord.modifResult(words, "  ");
		System.out.println(words);
	

	}
}
