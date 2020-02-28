package org.bhu.commons.lang.analyzer.segment.analysis;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.bhu.commons.lang.analyzer.bean.Nature;
import org.bhu.commons.lang.analyzer.bean.Term;
import org.bhu.commons.lang.analyzer.bean.TermNature;
import org.bhu.commons.lang.analyzer.bean.TermNatures;
import org.bhu.commons.lang.analyzer.dictionary.StaticDictionaryLoad;
import org.bhu.commons.lang.analyzer.enamex.EasternAsianNameHelper;
import org.bhu.commons.lang.analyzer.enamex.TranscriptionNameHelper;
import org.bhu.commons.lang.analyzer.library.UserDefineLibrary;
import org.bhu.commons.lang.analyzer.recognition.NumRecognition;
import org.bhu.commons.lang.analyzer.recognition.UserDefineRecognition;
import org.bhu.commons.lang.analyzer.segment.Analysis;
import org.bhu.commons.lang.analyzer.util.AnalyzerReader;
import org.bhu.commons.lang.analyzer.util.FilterModifWord;
import org.bhu.commons.lang.analyzer.util.Graph;
import org.bhu.commons.lang.analyzer.util.NatureHelper;
import org.bhu.commons.lang.trie.domain.Forest;
import org.nlp.sentence.segment.SentencesUtil;

/**
 * 标准分词
 * 
 * @author Jackie
 * 
 */
public class ToAnalysis extends Analysis {
	
	
	public ToAnalysis(){
	};
	
	public static void initUsrLib(){
		UserDefineAnalysis.initUsrLib();
	}
	
	public static void initStopwords(){
		FilterModifWord.initStopwords();
	}
	
	@Override
	protected List<Term> getResult(final Graph graph) {
		Merger merger = new Merger() {
			@Override
			public List<Term> merger() {
				graph.walkPath();
				// 数字发现
				if (StaticDictionaryLoad.isNumRecognition && graph.hasNum) {
					NumRecognition.recognition(graph.terms);
				}
				
				//识别时间

				// 姓名识别
				if (graph.hasPerson && StaticDictionaryLoad.isNameRecognition) {
					// 亚洲人名识别
					EasternAsianNameHelper.recognition(graph.terms);

					// 外国人名识别
//					new ForeignPersonRecognition(graph.terms).recognition();
					TranscriptionNameHelper.recognition(graph.terms);
//					graph.walkPathByScore();
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
				List<Term> result = new ArrayList<Term>();
				int length = graph.terms.length - 1;
				for (int i = 0; i < length; i++) {
					if (graph.terms[i] != null) {
//						System.out.println(graph.terms[i]);
//						printNatures(graph.terms[i]);
						result.add(graph.terms[i]);
					}
				}
				setRealName(graph, result);
				NatureHelper.modifyNatures(result);
				return result;
			}
		};
		return merger.merger();
	}

	private void printNatures(Term term){
		TermNature[] natures = term.termNatures().termNatures;
		for(TermNature nature : natures){
			System.out.print(nature.nature.natureStr+"\t");
			
		}
		System.out.println();
	}

	/**
	 * 用户自己定义的词典
	 * 
	 * @param forest
	 */
	public ToAnalysis(Forest... forests) {
		if (forests == null) {
			forests = new Forest[] { UserDefineLibrary.FOREST };
		}
		this.forests = forests;
	}

	public ToAnalysis(Reader reader, Forest... forests) {
		this.forests = forests;
		super.resetContent(new AnalyzerReader(reader));
	}
	

	
	private static List<Term> parse(String src){
		List<String> sens = SentencesUtil.toSentenceList(src);
		List<Term> words = new ArrayList<Term>();
		for(String sen : sens){

			try {
				words.addAll(new ToAnalysis().parseStr(sen));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println(sen);
			}
		}
		return words;
	}
	
	public static List<Term> parse(String str, Forest... forests) {
		return new ToAnalysis(forests).parseStr(str);
	}
	
	private static List<Term> parserFilter(String scr){
		return FilterModifWord.modifResult(parse(scr));
	}
	
	private static List<String> getTermNameList(List<Term> terms){
		List<String> list = new ArrayList<String>();
		for(Term term : terms){
			if(term.getName().trim().length()==0)continue;
			list.add(term.getName());
		}
		return list;
	}
	
	private static String getTermNameLine(List<Term> terms){
		StringBuilder builder = new StringBuilder();
		for(Term term : terms){
			if(term.getName().trim().length()==0)continue;
			builder.append(term.getName()).append(" ");
		}
		return builder.toString();
	}

	private static String getTermNameNatureLine(List<Term> terms){
		StringBuilder builder = new StringBuilder();
		for(Term term : terms){
			if(term.getName().trim().length()==0)continue;
			builder.append(term.getName()).append("/").append(term.getNatureStr()).append(" ");
		}
		return builder.toString();
	}

	public static List<Term> getTerms(String src) {
		return parse(src);
	}


	public static String getWordsLine(String src) {
		return getTermNameLine(parserFilter(src));
	}



	public static List<String> getWordBag(String src) {
		return getTermNameList(parserFilter(src));
	}



	public static String getWordNatureLine(String src) {
		return getTermNameNatureLine(parserFilter(src));
	}




	
	
	
	
	//=================================================
	

}
