package org.bhu.commons.lang.analyzer.segment.analysis;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.bhu.commons.lang.analyzer.bean.NewWord;
import org.bhu.commons.lang.analyzer.bean.Term;
import org.bhu.commons.lang.analyzer.crf.CRFSegment;
import org.bhu.commons.lang.analyzer.dictionary.LearnTool;
import org.bhu.commons.lang.analyzer.dictionary.StaticDictionaryLoad;
import org.bhu.commons.lang.analyzer.library.DATDictionary;
import org.bhu.commons.lang.analyzer.library.NatureLibrary;
import org.bhu.commons.lang.analyzer.recognition.NatureRecognition;
import org.bhu.commons.lang.analyzer.recognition.NewWordRecognition;
import org.bhu.commons.lang.analyzer.recognition.NumRecognition;
import org.bhu.commons.lang.analyzer.recognition.UserDefineRecognition;
import org.bhu.commons.lang.analyzer.segment.Analysis;
import org.bhu.commons.lang.analyzer.util.AnalyzerReader;
import org.bhu.commons.lang.analyzer.util.FilterModifWord;
import org.bhu.commons.lang.analyzer.util.Graph;
import org.bhu.commons.lang.analyzer.util.NameFix;
import org.bhu.commons.lang.analyzer.util.WordAlert;
import org.bhu.commons.lang.trie.domain.Forest;
import org.nlp.sentence.segment.SentencesUtil;

/**
 * 自然语言分词,具有未登录词发现功能。建议在自然语言理解中用。搜索中不要用
 * 
 * 
 * 
 */
public class NlpAnalysis extends Analysis {

	private LearnTool learn = null;

	private static final CRFSegment DEFAULT_SLITWORD = StaticDictionaryLoad.getCRFSplitWord();

	@Override
	protected List<Term> getResult(final Graph graph) {
		// TODO Auto-generated method stub

		Merger merger = new Merger() {
			@Override
			public List<Term> merger() {
				graph.walkPath();

				// 数字发现
				if (graph.hasNum) {
					NumRecognition.recognition(graph.terms);
				}

				// 词性标注
				List<Term> result = getResult();
				new NatureRecognition(result).recognition();

				if (learn == null) {
					learn = new LearnTool();
				}
				learn.learn(graph, DEFAULT_SLITWORD);

				// 通过crf分词
				List<String> words = DEFAULT_SLITWORD.cut(graph.chars);

				for (String word : words) {
					if (word.length() < 2 || DATDictionary.isInSystemDic(word) || WordAlert.isRuleWord(word)) {
						continue;
					}
					learn.addTerm(new NewWord(word, NatureLibrary.getNature("nw")));
				}

				// 用户自定义词典的识别
				new UserDefineRecognition(graph.terms, forests).recognition();
				graph.rmLittlePath();
				graph.walkPathByScore();

				// 进行新词发现
				new NewWordRecognition(graph.terms, learn).recognition();
				graph.walkPathByScore();

				// 修复人名左右连接
				NameFix.nameAmbiguity(graph.terms);

				// 优化后重新获得最优路径
				result = getResult();

				// 激活辞典
				for (Term term : result) {
					learn.active(term.getName());
				}

				setRealName(graph, result);

				return result;
			}

			private List<Term> getResult() {
				// TODO Auto-generated method stub
				List<Term> result = new ArrayList<Term>();
				int length = graph.terms.length - 1;
				for (int i = 0; i < length; i++) {
					if (graph.terms[i] == null) {
						continue;
					}
					result.add(graph.terms[i]);
				}
				return result;
			}
		};
		return merger.merger();
	}

	public NlpAnalysis() {
	};

	/**
	 * 用户自己定义的词典
	 * 
	 * @param forest
	 */

	public NlpAnalysis(Forest... forests) {
		this.forests = forests;
		parse("1");
	}

	public NlpAnalysis(LearnTool learn, Forest... forests) {
		this.forests = forests;
		this.learn = learn;
	}

	public NlpAnalysis(Reader reader, Forest... forests) {
		this.forests = forests;
		super.resetContent(new AnalyzerReader(reader));
	}

	public NlpAnalysis(Reader reader, LearnTool learn, Forest... forests) {
		this.forests = forests;
		this.learn = learn;
		super.resetContent(new AnalyzerReader(reader));
	}
	
	
	/**
	 * 返回分词标注结果字串
	 * @param src
	 * @return
	 */
	public static String parseReturnTagLine(String src){
		return getTagResultLine(parse(src));
	}
	
	/**
	 * 返回分词结果字串
	 * @param src
	 * @return
	 */
	public static String parseReturnSegLine(String src){
		return getSegResultLine(parse(src));
	}
	
	public static String parseReturnSegTagLine(String src){
		return getTagResultLine(parse(src));
	}
	
	/**
	 * 返回分词标注结果中的词序列字串
	 * @param terms
	 * @return
	 */
	private static String getSegResultLine(List<Term> terms){
		String resultLine = "";
		for(Term term : terms){
			resultLine += term.getName() + " ";
		}
		return resultLine;
	}
	
	/**
	 * 返回分词标注结果序列字串
	 * @param terms
	 * @return
	 */
	private static String getTagResultLine(List<Term> terms){
		String resultLine = "";
		for(Term term : terms){
			resultLine += term.toString() + " ";
		}
		return resultLine;
	}
	
	public static List<String> wordParse(String src){
		List<String> sens = SentencesUtil.toSentenceList(src);
		List<String> words = new ArrayList<String>();
		for (String sen : sens) {
			words.addAll(wordParsesen(sen));
		}
		return words;
	}

	private static List<String> wordParsesen(String str) {
		List<Term> terms = new NlpAnalysis().parseStr(str.trim());
		List<String> words = new ArrayList<String>();
		for(Term term : terms){
			words.add(term.getName());
		}
		return words;
	}
	
	private static List<Term> parsesen(String str) {
		return new NlpAnalysis().parseStr(str.trim());
	}
	
	public static List<Term> parsesen(String str, String[] filterNature, String...filterWords){
		FilterModifWord.insertStopWord(filterWords);
		FilterModifWord.insertStopNatures(filterNature);
		List<Term> words = parsesen(str);
		new NatureRecognition(words).recognition();
		words = FilterModifWord.modifResult(words);
		return words;
	}

	public static List<Term> parse(String str, Forest... forests) {
		return new NlpAnalysis(forests).parseStr(str);
	}

	public static List<Term> parse(String str, LearnTool learn, Forest... forests) {
		return new NlpAnalysis(learn, forests).parseStr(str);
	}


	private static List<Term> parse(String src){
		return new NlpAnalysis().parseStr(src);
	}
	

	
	private static List<Term> parserFilter(String scr){
		return FilterModifWord.modifResult(parse(scr));
	}
	
	private static List<String> getTermNameList(List<Term> terms){
		List<String> list = new ArrayList<String>();
		for(Term term : terms){
			list.add(term.getName());
		}
		return list;
	}
	
	private static String getTermNameLine(List<Term> terms){
		StringBuilder builder = new StringBuilder();
		for(Term term : terms){
			builder.append(term.getName()).append(" ");
		}
		return builder.toString();
	}

	private static String getTermNameNatureLine(List<Term> terms){
		StringBuilder builder = new StringBuilder();
		for(Term term : terms){
			builder.append(term.getName()).append("/").append(term.getNatureStr()).append(" ");
		}
		return builder.toString();
	}


	


	public static List<Term> getTerms(String src) {
		return parse(src);
	}


	public static List<Term> getTerms_Usr(String src) {
		return parserFilter(src);
	}


	public static String getWordsLine(String src) {
		return getTermNameLine(parse(src));
	}

	public static String getWordsLine_Usr(String src) {
		return getTermNameLine(parserFilter(src));
	}


	public static List<String> getWordBag(String src) {
		return getTermNameList(parse(src));
	}

	public static List<String> getWordBag_Usr(String src) {
		return getTermNameList(parserFilter(src));
	}



	public static String getWordNatureLine(String src) {
		return getTermNameNatureLine(parse(src));
	}


	public static String getWordNatureLine_Usr(String src) {
		return getTermNameNatureLine(parserFilter(src));
	}
	
	
}
