package org.bhu.commons.lang.analyzer.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bhu.commons.lang.analyzer.bean.Nature;
import org.bhu.commons.lang.analyzer.bean.Term;
import org.bhu.commons.lang.analyzer.dictionary.DicReader;
import org.bhu.commons.lang.analyzer.library.UserDefineLibrary;
import org.bhu.commons.lang.analyzer.segment.analysis.UserDefineAnalysis;
import org.bhu.commons.lang.trie.domain.Forest;

/*
 * 停用词过滤,修正词性到用户词性,修正用户词典.
 */
public class FilterModifWord {

	private static Set<String> FILTER = new HashSet<String>();

	private static String TAG = "#";

	public static boolean isWord;
	
	private static boolean isTag = false;
	
	public static void initStopwords(){
//		FastReader reader = new FastReader("library/stopwords.txt");
//		String[] lines = reader.readLines();
		FileReader reader = new FileReader(DicReader.getInputStream("org/bhu/commons/lang/analyzer/resources/stopwords.dct"));
		insertStopWord(reader.read2List());
	}
	
	public static void initUsrLib(){
		UserDefineAnalysis.initUsrLib();
	}
	
	public static void initAphLexicon(){
		UserDefineAnalysis.initAphLexicon();
	}
	
	public static void insertStopWord(List<String> filterWords) {
		FILTER.addAll(filterWords);
	}

	public static void insertStopWord(String... filterWord) {
		for (String word : filterWord) {
			FILTER.add(word);
		}
	}

	public static void insertStopNatures(String... filterNatures) {
		isTag = true;
		for (String natureStr : filterNatures) {
			FILTER.add(TAG + natureStr);
		}
	}

	/*
	 * 停用词过滤并且修正词性
	 */
	public static List<Term> modifResult(List<Term> all) {
		List<Term> result = new ArrayList<Term>();
		try {
			for (Term term : all) {
				if (FILTER.size() > 0 && (FILTER.contains(term.getName()) || (isTag && FILTER.contains(TAG + term.natrue().natureStr)))) {
					term.setName("");
					result.add(term);
					continue;
				}
				String[] params = UserDefineLibrary.getParams(term.getName());
				if (params != null) {
					term.setNature(new Nature(params[0]));
				}
				result.add(term);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("FilterStopWord.updateDic can not be null , " + "you must use set FilterStopWord.setUpdateDic(map) or use method set map");
		}
		return result;
	}
	
	public static List<Term> modifResult(List<Term> all, String tag) {
		List<Term> result = new ArrayList<Term>();
		try {
			for (Term term : all) {
				if (FILTER.size() > 0 && (FILTER.contains(term.getName()) || (isTag && FILTER.contains(TAG + term.natrue().natureStr)))) {
					term.setName(tag);
					result.add(term);
					continue;
				}
				String[] params = UserDefineLibrary.getParams(term.getName());
				if (params != null) {
					term.setNature(new Nature(params[0]));
				}
				result.add(term);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("FilterStopWord.updateDic can not be null , " + "you must use set FilterStopWord.setUpdateDic(map) or use method set map");
		}
		return result;
	}

	/*
	 * 停用词过滤并且修正词性
	 */
	public static List<Term> modifResult(List<Term> all, Forest... forests) {
		List<Term> result = new ArrayList<Term>();
		try {
			for (Term term : all) {
				if (FILTER.size() > 0 && (FILTER.contains(term.getName()) || FILTER.contains(TAG + term.natrue().natureStr))) {
					continue;
				}
				for (Forest forest : forests) {
					String[] params = UserDefineLibrary.getParams(forest, term.getName());
					if (params != null) {
						term.setNature(new Nature(params[0]));
					}
				}
				result.add(term);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("FilterStopWord.updateDic can not be null , " + "you must use set FilterStopWord.setUpdateDic(map) or use method set map");
		}
		return result;
	}
	
	
}
