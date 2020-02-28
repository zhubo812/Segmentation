package org.bhu.test;

import org.bhu.commons.lang.analyzer.util.TokenizeUtils;

public class TimeTest {

	
	public static void main(String[] args) {
		String[] inPut = {
				
				"1997年8月12日","1997年8月12号","8月12号","上午八点"
				,"去年8月","8月","98年8月","8月12号","去年8月12日后",
				"下午2点11分"
				
		};
		TokenizeUtils tokenizer = new TokenizeUtils();
		tokenizer.initUsrDic();
		System.out.println(tokenizer.getWordNatureLine(""));
		long length = 0L;
		long start = System.currentTimeMillis();
		for (String s : inPut) {
			length += s.getBytes().length;
//			List<Term> words = NlpAnalysis.parse(s);
//			System.out.println(tokenizer.segment(s));
//			System.out.println(s);
			System.out.println(tokenizer.getWordNatureLine(s));
		}
		long elapsed = (System.currentTimeMillis() - start);
		System.out.println(String.format("time elapsed:%d, rate:%fkb/s", elapsed,
	            (length * 1.0) / 1024.0f / (elapsed * 1.0 / 1000.0f)));
	}
}
