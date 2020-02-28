package org.bhu.commons.lang.analyzer.multithread;

import java.util.Queue;

import org.bhu.commons.lang.analyzer.segment.analysis.NlpAnalysis;
import org.bhu.commons.lang.analyzer.util.FileUtil;

class FileMultiThreadAnalyzer implements Runnable {
	String path;
	String outPath;
	static NlpAnalysis analyzer;
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getOutPath() {
		return outPath;
	}

	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}

	Queue<String> queue;

	public FileMultiThreadAnalyzer(String path, String outPath) {
		super();
		analyzer = new NlpAnalysis();
		this.path = path;
		this.outPath = outPath;
		queue = FileUtil.getAllFiles2Queue(this.path);
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
//	
//	@SuppressWarnings("static-access")
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
