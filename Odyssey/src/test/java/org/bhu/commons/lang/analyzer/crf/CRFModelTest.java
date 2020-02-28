package org.bhu.commons.lang.analyzer.crf;

import org.bhu.commons.lang.analyzer.crf.CRFModel;
import org.bhu.commons.lang.analyzer.crf.Model;
import org.junit.Test;

public class CRFModelTest {

	@SuppressWarnings("static-access")
	@Test
	public void parseFileTest() throws Exception{
		String modelPath = "F:/data/segment/crf.model";
		String modelPathFile = "F:/data/segment/crf.txt";
		CRFModel model = new CRFModel();
		Model.loadModel(modelPath);
//		model.parseFileToModel(modelPathFile);
	}
}
