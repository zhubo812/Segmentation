package org.bhu.commons.lang.analyzer.multithread;

import org.bhu.commons.lang.analyzer.multithread.FileMultiThreadAnalyzer;
import org.junit.Test;

public class MultiThreadAnalyzerTest {

	@Test
	public void MultiThreadFileTest(){
		String path = "F:/1";
		String outPath = "F:/2";
		FileMultiThreadAnalyzer anaylzerThd = new FileMultiThreadAnalyzer(path, outPath);

		Thread th1 = new Thread(anaylzerThd, "线程1");
		Thread th2 = new Thread(anaylzerThd, "线程2");
		Thread th3 = new Thread(anaylzerThd, "线程3");

		th1.start();
		th2.start();
		th3.start();
	}
}
