package org.bhu.commons.lang.analyzer.util;

import org.bhu.commons.lang.analyzer.crf.Model;
import org.bhu.commons.lang.analyzer.dictionary.DicReader;
import org.junit.Test;

public class ModelTest {

	@Test
	public void modelWriterTest() throws Exception{
		Model mode = Model.loadModel(DicReader.getInputStream("org/bhu/commons/lang/analyzer/resources/crf.model"));

		mode.writeModel("train_file/crfnew.model");
	}
}
