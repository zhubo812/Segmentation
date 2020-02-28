package org.bhu.commons.lang.analyzer.multithread;

import org.bhu.commons.lang.analyzer.segment.analysis.ToAnalysis;

public class MultiThread implements Runnable{
	ToAnalysis analyzer;
	
	public void Test() {
		
		new Thread(new MultiThread(),"FirstThread").start();
		new Thread(new MultiThread(),"SecondThread").start();
		
	}
	
	public MultiThread() {
		super();
		analyzer = new ToAnalysis();
	}

//	@Override
	@Override
	public void run() {
		System.out.println(ToAnalysis.parse("你试试 不就知道了"));
	}
}
