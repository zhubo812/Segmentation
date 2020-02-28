package org.bhu.commons.lang.analyzer.bean;

import java.util.List;

public interface Tokenizable {

	
	//返回Term序列
	List<Word> getWords(String src);

	
	//返回词构成的字符串，即Term.name
	String getWordsLine(String src);

	
	//返回词列表，即Term.name
	List<String> getWordBag(String src);

	
	//返回Term字串，即Term.name/Term.nature
	String getWordNatureLine(String src);
	void initUsrDic();

}
