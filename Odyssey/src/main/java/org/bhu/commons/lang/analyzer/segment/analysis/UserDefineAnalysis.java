package org.bhu.commons.lang.analyzer.segment.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bhu.commons.lang.analyzer.bean.Term;
import org.bhu.commons.lang.analyzer.dictionary.StaticDictionaryLoad;
import org.bhu.commons.lang.analyzer.enamex.AsianPersonRecognition;
import org.bhu.commons.lang.analyzer.enamex.ForeignPersonRecognition;
import org.bhu.commons.lang.analyzer.library.UserDefineLibrary;
import org.bhu.commons.lang.analyzer.recognition.NumRecognition;
import org.bhu.commons.lang.analyzer.recognition.UserDefineRecognition;
import org.bhu.commons.lang.analyzer.segment.Analysis;
import org.bhu.commons.lang.analyzer.util.AnalyzerReader;
import org.bhu.commons.lang.analyzer.util.FileReader;
import org.bhu.commons.lang.analyzer.util.FileUtil;
import org.bhu.commons.lang.analyzer.util.FilterModifWord;
import org.bhu.commons.lang.analyzer.util.Graph;
import org.bhu.commons.lang.analyzer.util.NameFix;
import org.bhu.commons.lang.trie.domain.Forest;

/**
 * 默认用户自定义词性优先
 * @author Jackie
 *
 */
public class UserDefineAnalysis extends Analysis{

	@Override
	protected List<Term> getResult(final Graph graph) {
		// TODO Auto-generated method stub
		Merger merger = new Merger() {
			@Override
			public List<Term> merger() {
				// TODO Auto-generated method stub
				graph.walkPath();
				// 数字发现
				if (StaticDictionaryLoad.isNumRecognition && graph.hasNum) {
					NumRecognition.recognition(graph.terms);
				}

				// 姓名识别
				if (graph.hasPerson && StaticDictionaryLoad.isNameRecognition) {
					// 亚洲人名识别
					new AsianPersonRecognition(graph.terms).recognition();
					graph.walkPathByScore();
					NameFix.nameAmbiguity(graph.terms);
					// 外国人名识别
					new ForeignPersonRecognition(graph.terms).recognition();
					graph.walkPathByScore();
				}

				// 用户自定义词典的识别
				userDefineRecognition(graph, forests);

				return getResult();
			}

			private void userDefineRecognition(final Graph graph, Forest... forests) {
				new UserDefineRecognition(graph.terms, forests).recognition();
				graph.rmLittlePath();
				graph.walkPathByScore();
			}

			private List<Term> getResult() {
				// TODO Auto-generated method stub
				List<Term> result = new ArrayList<Term>();
				int length = graph.terms.length - 1;
				for (int i = 0; i < length; i++) {
					if (graph.terms[i] != null) {
						result.add(graph.terms[i]);
					}
				}
				setRealName(graph, result);

				FilterModifWord.modifResult(result);
				return result;
			}
		};
		return merger.merger();
	}

	private UserDefineAnalysis() {
		
	};

	public static void initUsrLib(){
		String dir = "data/";
		List<String> filelist = FileUtil.getAllFiles(dir);
		for(String path : filelist){
			if(!path.endsWith(".dic"))continue;
			File file = new File(path);
//			System.out.println(file.getAbsolutePath());
			FileReader reader = new FileReader(file.getAbsolutePath());
			List<String> array = reader.read2List();
			for(String line  : array){
				if(line.trim().length()==0)continue;
				String[] items = line.split("\t");
				if(items.length==3){
					UserDefineLibrary.insertWord(items[0], items[1], Integer.parseInt(items[2]));
				}
				else if(items.length==1){
					UserDefineLibrary.insertWord(items[0], "usr", 1000);
				}
			}
		}
	}
	
	public static void initAphLexicon(){
		BufferedReader br = null;
			try {
				br = StaticDictionaryLoad.getAphWordReader();
				String temp = null;
				String[] items = null;
				
				while ((temp = br.readLine()) != null) {
					items = temp.split("\t");
					if(items.length==3){
						UserDefineLibrary.insertWord(items[0], items[1], Integer.parseInt(items[2]));
					}
				}

				if (br != null)
					br.close();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 用户自己定义的词典
	 * 
	 * @param forest
	 */
	public UserDefineAnalysis(Forest... forests) {
		if (forests == null) {
			forests = new Forest[] { UserDefineLibrary.FOREST };
		}
		this.forests = forests;
	}

	public UserDefineAnalysis(BufferedReader reader, Forest... forests) {
		this.forests = forests;
		super.resetContent(new AnalyzerReader(reader));
	}

	public static List<Term> parse(String str) {
		return new UserDefineAnalysis().parseStr(str);
	}

	public static List<Term> parse(String str, Forest... forests) {
		return new UserDefineAnalysis(forests).parseStr(str);
	}

//	public static List<Term> userDefineParser(String scr){
//		return FilterModifWord.modifResult(parse(scr));
//	}
//	
//	private static List<String> getTermNameList(List<Term> terms){
//		List<String> words = new ArrayList<String>();
//		for(Term term : terms){
//			if(term.getName().equals(""))continue;
//			words.add(term.getName());
//		}
//		return words;
//	}
//	
//	private static List<String> getTermNameLine(List<Term> terms){
//		List<String> words = new ArrayList<String>();
//		for(Term term : terms){
//			if(term.getName().equals(""))continue;
//			words.add(term.getName());
//		}
//		return words;
//	}
//	
//	public static List<String> parserAll_WordBag_Word_Usr(String scr){
//		return getTermNameList(userDefineParser(scr));
//	}
//	
////	public static String parserAll_WordBag_Line_Filter(String scr){
////		return getTermNameList(userDefineParser(scr));
////	}
//	
//	public static String parser_WordBagLine_WordNature_Usr(String scr){
//		StringBuilder sb = new StringBuilder();
//		List<Term> list = userDefineParser(scr);
//		for(Term term : list){
//			sb.append(term.toString()).append("  ");
//		}
//		return sb.toString();
//	}
//	public static String parser_WordBagLine_Word_Usr(String scr){
//		StringBuilder sb = new StringBuilder();
//		List<Term> list = userDefineParser(scr);
//		for(Term term : list){
//			sb.append(term.getName()).append("  ");
//		}
//		return sb.toString();
//	}
//	
//	public static List<String> parse_WordBag_Word_Usr(String scr){
//		List<String> words = new ArrayList<String>();
//		List<Term> list = userDefineParser(scr);
//		for(Term term : list){
//			words.add(term.getName());
//		}
//		return words;
//	}
//	
//	
//	public static List<String> parse_WordBag_Word_Usr_Filter(String scr){
//		List<String> words = new ArrayList<String>();
//		List<Term> list = userDefineParser(scr);
//		list = FilterModifWord.modifResult(list);
//		for(Term term : list){
//			words.add(term.getName());
//		}
//		return words;
//	} 
//	
//	
//	public static String parse_WordLine_Word_Usr_Filter(String scr){
//		StringBuilder sb = new StringBuilder();
//		List<Term> list = userDefineParser(scr);
//		list = FilterModifWord.modifResult(list);
//		for(Term term : list){
//			sb.append(term.getName()).append("  ");
//		}
//		return sb.toString();
//	} 
	

}
