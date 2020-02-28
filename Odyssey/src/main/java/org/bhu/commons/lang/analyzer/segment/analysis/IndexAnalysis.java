package org.bhu.commons.lang.analyzer.segment.analysis;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.bhu.commons.lang.analyzer.bean.Term;
import org.bhu.commons.lang.analyzer.bean.TermNatures;
import org.bhu.commons.lang.analyzer.dictionary.StaticDictionaryLoad;
import org.bhu.commons.lang.analyzer.enamex.AsianPersonRecognition;
import org.bhu.commons.lang.analyzer.enamex.ForeignPersonRecognition;
import org.bhu.commons.lang.analyzer.recognition.NumRecognition;
import org.bhu.commons.lang.analyzer.recognition.UserDefineRecognition;
import org.bhu.commons.lang.analyzer.segment.Analysis;
import org.bhu.commons.lang.analyzer.segment.impl.GetWordsImpl;
import org.bhu.commons.lang.analyzer.util.AnalyzerReader;
import org.bhu.commons.lang.analyzer.util.Graph;
import org.bhu.commons.lang.analyzer.util.NameFix;
import org.bhu.commons.lang.trie.domain.Forest;

/**
 * 用于检索的分词方式
 * 
 * @author Jackie
 * 
 */
public class IndexAnalysis extends Analysis {

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

				// 姓名识别
				if (graph.hasPerson && StaticDictionaryLoad.isNameRecognition) {
					// 亚洲人名识别
					new AsianPersonRecognition(graph.terms).recognition();
//					graph.walkPathByScore();
//					NameFix.nameAmbiguity(graph.terms);
					// 外国人名识别
					new ForeignPersonRecognition(graph.terms).recognition();
					graph.walkPathByScore();
				}

				// 用户自定义词典的识别
				userDefineRecognition(graph, forests);

				return result();
			}

			private void userDefineRecognition(final Graph graph, Forest... forests) {
				new UserDefineRecognition(graph.terms, forests).recognition();
				graph.rmLittlePath();
				graph.walkPathByScore();
			}


			/**
			 * 检索的分词
			 * 
			 * @return
			 */
			private List<Term> result() {


				String temp = null;

				List<Term> result = new LinkedList<Term>();
				int length = graph.terms.length - 1;
				for (int i = 0; i < length; i++) {
					if (graph.terms[i] != null) {
						result.add(graph.terms[i]);
					}
				}

				LinkedList<Term> last = new LinkedList<Term>() ;
				for (Term term : result) {
					if (term.getName().length() >= 3) {
						GetWordsImpl gwi = new GetWordsImpl(term.getName());
						while ((temp = gwi.allWords()) != null) {
							if (temp.length() < term.getName().length() && temp.length()>1) {
								last.add(new Term(temp, gwi.offe + term.getOffe(), TermNatures.NULL));
							}
						}
					}
				}

				result.addAll(last) ;
				
				setRealName(graph, result);
				return result;
			}
		};

		return merger.merger();
	}

	private IndexAnalysis() {
	};

	public IndexAnalysis(Forest... forests) {
		this.forests = forests;
	}

	public IndexAnalysis(Reader reader, Forest... forests) {
		this.forests = forests;
		super.resetContent(new AnalyzerReader(reader));
	}

	public static List<Term> parse(String str) {
		return new IndexAnalysis().parseStr(str);
	}

	public static List<Term> parse(String str, Forest... forests) {
		return new IndexAnalysis(forests).parseStr(str);

	}
}
