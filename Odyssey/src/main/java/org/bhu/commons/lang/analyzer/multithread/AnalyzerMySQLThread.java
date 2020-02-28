package org.bhu.commons.lang.analyzer.multithread;


import java.util.Queue;

import org.bhu.commons.lang.analyzer.segment.analysis.NlpAnalysis;

public class AnalyzerMySQLThread {

	public static void main(String[] args) {
		//得到数据库表的ID集合，加入队列queue
	}
}

class MySQLThreadAnalyzer implements Runnable{
	String url;
	String outPath;
	Queue<String> queue;
	static NlpAnalysis analyzer;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOutPath() {
		return outPath;
	}

	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}

	public Queue<String> getQueue() {
		return queue;
	}

	public void setQueue(Queue<String> queue) {
		this.queue = queue;
	}

	public MySQLThreadAnalyzer(String url, Queue<String> queue, String outPath) {
		super();
		this.url = url;
		this.queue = queue;
		this.outPath = outPath;

	}

	//@Override
	public void run() {

		String str;
		while ((str = this.queue.poll()) != null) {
			System.out.println(Thread.currentThread().getName() + "正在处理文件：" + str
					+ "\t" + "剩余文件数：" + this.queue.size());
			//FileReader reader = new FileReader(str);
			//String segTxt = getWordWithCate(reader);

		}
	}
//	public static String getWords(FileReader reader) {
//		String result = "";
//		for(String line; (line=reader.readLine())!=null;){
//			result += NlpAnalysis.parseReturnSegLine(line) + "\n";
//		}
//		return result;
//	}
//
//	public static String getWordWithCate(FileReader reader) {
//		String result = "";
//		for(String line; (line=reader.readLine())!=null;){
//			result += analyzer.parseReturnTagLine(line) + "\n";
//		}
//		return result;
//	}
}