package org.bhu.commons.lang.analyzer.bean;

import java.util.ArrayList;
import java.util.List;

public class WordConvert extends Word{

	public WordConvert(String name, String pos) {
		super(name, pos);
	}

	public static List<Word> getWords(List<Term> terms){
		List<Word> words = new ArrayList<Word>();
		for(Term term : terms){
			Word word = new Word(term.getName(), term.getNatureStr());
			words.add(word);
		}
		return words;
	}
	
	public static String getWordsLine(List<Term> terms){
		StringBuilder line = new StringBuilder();
		for(Term term : terms){
			Word word = new Word(term.getName(), term.getNatureStr());
			line.append(word.toString()+" ");
		}
		return line.toString();
	}
}
