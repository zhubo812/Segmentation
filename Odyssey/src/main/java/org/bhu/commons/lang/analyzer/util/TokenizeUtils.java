package org.bhu.commons.lang.analyzer.util;

import java.util.List;

import org.bhu.commons.lang.analyzer.bean.Tokenizable;
import org.bhu.commons.lang.analyzer.bean.Word;
import org.bhu.commons.lang.analyzer.bean.WordConvert;
import org.bhu.commons.lang.analyzer.segment.analysis.ToAnalysis;

public class TokenizeUtils implements Tokenizable{

	public TokenizeUtils (){
		FilterModifWord.initAphLexicon();
	}
	
	public void initFilterSegMode(){
		String[] stopNatures = { "w", "r", "d", "mq","t", "null" };
		FilterModifWord.initStopwords();
		FilterModifWord.insertStopNatures(stopNatures);
	}
	
	public void initUsrDic(){
		FilterModifWord.initAphLexicon();
		FilterModifWord.initUsrLib();
	}

	public List<Word> getWords(String src) {
		return WordConvert.getWords(ToAnalysis.getTerms(src));
	}

	public String getWordsLine(String src) {
		return ToAnalysis.getWordsLine(src);
	}

	public List<String> getWordBag(String src) {
		return ToAnalysis.getWordBag(src);
	}

	public String getWordNatureLine(String src) {
		return ToAnalysis.getWordNatureLine(src);
	}

	
	
}
