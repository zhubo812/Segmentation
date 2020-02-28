package org.bhu.commons.lang.analyzer.util;

import java.util.List;

import org.bhu.commons.lang.analyzer.bean.Tokenizable;
import org.bhu.commons.lang.analyzer.bean.Word;
import org.bhu.commons.lang.analyzer.bean.WordConvert;
import org.bhu.commons.lang.analyzer.segment.analysis.NlpAnalysis;

public class NLPTokenizer implements Tokenizable{
	public NLPTokenizer (){
		initUsrDic();
	}
	
	public void initFilterSegMode(){
		String[] stopNatures = { "w", "r", "d", "mq","t", "null" };
		FilterModifWord.initStopwords();
		FilterModifWord.insertStopNatures(stopNatures);
	}
	
	public void initUsrDic(){
		FilterModifWord.initUsrLib();
	}

	public List<Word> getWords(String src) {
		return WordConvert.getWords(NlpAnalysis.getTerms(src));
	}


	public String getWordsLine(String src) {
		return NlpAnalysis.getWordsLine(src);
	}

	public List<String> getWordBag(String src) {
		return NlpAnalysis.getWordBag(src);
	}

	public String getWordNatureLine(String src) {
		return NlpAnalysis.getWordNatureLine(src);
	}

	
}
