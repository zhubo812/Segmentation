package org.bhu.commons.lang.analyzer.segment;

import static org.bhu.commons.lang.analyzer.library.DATDictionary.IN_SYSTEM;
import static org.bhu.commons.lang.analyzer.library.DATDictionary.status;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.bhu.commons.lang.analyzer.bean.Entity;
import org.bhu.commons.lang.analyzer.bean.Term;
import org.bhu.commons.lang.analyzer.bean.TermNature;
import org.bhu.commons.lang.analyzer.bean.TermNatures;
import org.bhu.commons.lang.analyzer.dictionary.StaticDictionaryLoad;
import org.bhu.commons.lang.analyzer.library.UserDefineLibrary;
import org.bhu.commons.lang.analyzer.segment.impl.GetWordsImpl;
import org.bhu.commons.lang.analyzer.util.AnalyzerReader;
import org.bhu.commons.lang.analyzer.util.Graph;
import org.bhu.commons.lang.analyzer.util.NQHelper;
import org.bhu.commons.lang.analyzer.util.StringUtil;
import org.bhu.commons.lang.analyzer.util.TimeHelper;
import org.bhu.commons.lang.analyzer.util.WordAlert;
import org.bhu.commons.lang.trie.domain.Forest;
import org.bhu.commons.lang.trie.domain.GetWord;

/**
 * 基本分词+人名识别
 * 
 * @author Jackie
 * 
 */
public abstract class Analysis {

	/**
	 * 用来记录偏移量
	 */
	public int offe;

	/**
	 * 分词的类
	 */
	private GetWordsImpl gwi = new GetWordsImpl();

	protected Forest[] forests = null;

	private Forest ambiguityForest = UserDefineLibrary.ambiguityForest;

	/**
	 * 文档读取流
	 */
	private AnalyzerReader br;

	protected Analysis() {
	};

	private LinkedList<Term> terms = new LinkedList<Term>();
	/**
	 * while 循环调用.直到返回为null则分词结束
	 * 
	 * @return
	 * @throws IOException
	 */

	public Term next() throws IOException {
		Term term = null;
		if (!terms.isEmpty()) {
			term = terms.poll();
			term.updateOffe(offe);
			return term;
		}

		String temp = br.readLine();
		offe = br.getStart();
		while (StringUtil.isBlank(temp)) {
			if (temp == null) {
				return null;
			} else {
				temp = br.readLine();
			}

		}

		// 歧异处理字符串

		analysisStr(temp);

		if (!terms.isEmpty()) {
			term = terms.poll();
			term.updateOffe(offe);
			return term;
		}

		return null;
	}

	/**
	 * 一整句话分词,用户设置的歧异优先
	 * 
	 * @param temp
	 */
	private void analysisStr(String temp) {
		temp = WordAlert.ToDBC(temp);
		Graph gp = new Graph(temp.trim());
		int startOffe = 0;
		List<Entity> timex = TimeHelper.getTimex(temp);
		List<Entity> nqx = NQHelper.getNumQ(temp);
		if (this.ambiguityForest != null) {
			GetWord gw = new GetWord(this.ambiguityForest, gp.chars);
			String[] params = null;
			while ((gw.getFrontWords()) != null) {
				if (gw.offe > startOffe) {
					analysis(gp, startOffe, gw.offe,timex, nqx);
				}
				params = gw.getParams();
				startOffe = gw.offe;
				for (int i = 0; i < params.length; i += 2) {
					gp.addTerm(new Term(params[i], startOffe, new TermNatures(new TermNature(params[i + 1], 1))));
					startOffe += params[i].length();
				}
			}
		}
		if (startOffe < gp.chars.length - 1) {
			analysis(gp, startOffe, gp.chars.length,timex, nqx);
		}
		List<Term> result = this.getResult(gp);

//		terms.addAll(result);
		this.addAll(result);
	}



	private void analysis(Graph gp, int startOffe, int endOffe, List<Entity> timex, List<Entity> nqx) {
		int start = 0;
		int end = 0;
		char[] chars = gp.chars;
		char c = 0;
		String str = null;
		if(timex!= null && timex.size()>0){
			
			for(Entity tx : timex){
				gp.addTerm(new Term(tx.getExpression(), tx.getStartIndx(), TermNatures.T));
//				indexList.removeAll( CollectionUtil.getIndexList(tx.getStartIndx(), tx.getEndIndex()));
			}
		}
		
		if(nqx!= null && nqx.size()>0){
			
			for(Entity tx : nqx){
				gp.addTerm(new Term(tx.getExpression(), tx.getStartIndx(), TermNatures.MQ));
			}
		}
		
		for (int i = startOffe; i < endOffe; i++) {
			int status = status(chars[i]);
			switch (status) {
			case 0:
				gp.addTerm(new Term(String.valueOf(chars[i]), i, TermNatures.NULL));
				break;
			case 3:
				gp.addTerm(new Term(String.valueOf(chars[i]), i, TermNatures.W));
				break;
			case 4:
				start = i;
				end = 1;
				while (++i < endOffe && status(chars[i]) == 4) {
					end++;
				}
				str = WordAlert.alertEnglish(chars, start, end);
				gp.addTerm(new Term(str, start, TermNatures.EN));
				i--;
				break;
			case 5:
				start = i;
				end = 1;
				if(start==i&&chars[i]=='.'){
					gp.addTerm(new Term(String.valueOf(chars[i]), i, TermNatures.W));
					break;
				}
				while (++i < endOffe && status(chars[i]) == 5) {
					end++;
				}
				str = WordAlert.alertNumber(chars, start, end);
				if(gp.terms[start] == null){
					gp.addTerm(new Term(str, start, TermNatures.M));
				}
				i--;
				break;
			case 6:
				start = i;
				end = 1;
				while (++i < endOffe && status(chars[i]) == 6) {
					end++;
				}
				str = new String(chars, start, end);
				gp.addTerm(new Term(str, start, TermNatures.RU));
				i--;
				break;
			case 7:
				start = i;
				end = 1;
				while (++i < endOffe && status(chars[i]) == 7) {
					end++;
				}
				str = new String(chars, start, end);
				gp.addTerm(new Term(str, start, TermNatures.GR));
				i--;
				break;

			default:
				start = i;
				end = i;
				c = chars[start];
				while (IN_SYSTEM[c] > 0) {
					end++;
					if (++i >= endOffe)
						break;
					c = chars[i];
				}

				if (start == end) {
					gp.addTerm(new Term(String.valueOf(c), i, TermNatures.NULL));
					continue;
				}

				gwi.setChars(chars, start, end);
				while ((str = gwi.allWords()) != null) {
					gp.addTerm(new Term(str, gwi.offe, gwi.getItem()));
				}

				/**
				 * 如果未分出词.以未知字符加入到gp中
				 */
				if (IN_SYSTEM[c] > 0 || status(c) > 3) {
					i -= 1;
				} else {
					gp.addTerm(new Term(String.valueOf(c), i, TermNatures.NULL));
				}
				

				break;
			}
		}
		
		
	}

	/**
	 * 将为标准化的词语设置到分词中
	 * 
	 * @param gp
	 * @param result
	 */
	protected void setRealName(Graph graph, List<Term> result) {

		if (!StaticDictionaryLoad.isRealName) {
			return;
		}

		String str = graph.realStr;

		for (Term term : result) {
			term.setRealName(str.substring(term.getOffe(), term.getOffe() + term.getName().length()));
		}
	}

	protected List<Term> parseStr(String temp) {
		analysisStr(temp);
		return terms;
	}

	protected abstract List<Term> getResult(Graph graph);

	public abstract class Merger {
		public abstract List<Term> merger();
	}
	
	private void addAll(List<Term> result){
		for(Term term : result){
			if(term.getName().trim().length()==0)continue;
			terms.add(term);
		}
	}

	/**
	 * 重置分词器
	 * 
	 * @param br
	 */
	public void resetContent(AnalyzerReader br) {
		this.offe = 0;
		this.br = br;
	}

	public void resetContent(Reader reader) {
		this.offe = 0;
		this.br = new AnalyzerReader(reader);
	}

	public void resetContent(Reader reader, int buffer) {
		this.offe = 0;
		this.br = new AnalyzerReader(reader, buffer);
	}

	public Forest getAmbiguityForest() {
		return ambiguityForest;
	}

	public void setAmbiguityForest(Forest ambiguityForest) {
		this.ambiguityForest = ambiguityForest;
	}
}
