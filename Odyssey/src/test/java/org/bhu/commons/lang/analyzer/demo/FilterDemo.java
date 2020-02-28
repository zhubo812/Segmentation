package org.bhu.commons.lang.analyzer.demo;

import java.util.List;

import org.bhu.commons.lang.analyzer.bean.Term;
import org.bhu.commons.lang.analyzer.library.UserDefineLibrary;
import org.bhu.commons.lang.analyzer.recognition.NatureRecognition;
import org.bhu.commons.lang.analyzer.segment.analysis.ToAnalysis;
import org.bhu.commons.lang.analyzer.util.FilterModifWord;

public class FilterDemo {

//	@Test
	public void FilterTest() {
		// 加入停用词
//		FilterModifWord.insertStopWord("并且");
//		FilterModifWord.insertStopWord("但是");

		// 加入过滤词性词性
		String[] stopNatures = {"w", "m", "null"};
		FilterModifWord.insertStopNatures(stopNatures);

		List<Term> parse = ToAnalysis
				.parse("停用词过滤了。.并且修正词143922950性为用户自定义词性.但是你必须.must.设置停用词性词性词典");
		new NatureRecognition(parse).recognition();
		System.out.println(parse);

		UserDefineLibrary.insertWord("停用词", "userDefine", 1000);
		FilterModifWord.initStopwords();
		// 修正词性并且过滤停用
		parse = FilterModifWord.modifResult(parse);

		System.out.println(parse);
	}
	

	

}
