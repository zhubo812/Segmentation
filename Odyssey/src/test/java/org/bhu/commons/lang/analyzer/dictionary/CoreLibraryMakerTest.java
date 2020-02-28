package org.bhu.commons.lang.analyzer.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.bhu.commons.lang.analyzer.bean.AnalyzerItem;
import org.bhu.commons.lang.analyzer.util.IOUtil;
import org.bhu.commons.lang.dat.DATMaker;
import org.bhu.commons.lang.dat.Item;

public class CoreLibraryMakerTest {

	private static final String punctuations = "!\"#&()*+,-:;<=>?@[\\]^_`{|}~¤§¨°·×÷ˉ–—―‖‘’“”………‰′″※←↑→↓∈∏∑∕√∝∞∥∧∩∪∮∴∵∶∽≈≠≤≥⊙⊥⌒─━│┃┌┏┐┑┒┓└┗┘┛├┟┣┤┥┪┫┬┰┴┼▇█■□▲△◆◇◎●★☆♂、。〃〇〈〉《》「」『』【】〓〔〕〖〗〞！，：？～";
	
	public static void main(String[] args) throws Exception {
		makeDic();
		DATMaker datM = new DATMaker();

		datM.maker("train_file/library.txt", AnalyzerItem.class);
		char[] puncs = punctuations.toCharArray();

		Item[] dat = datM.getDAT();

		insertToArray(dat, '%', (byte) 5, "{nb=1}");
		insertToArray(dat, '.', (byte) 5, "{nb=1}");
		insertToArray(dat, '±', (byte) 5, "{nb=1}");
		insertToArray(dat, '○', (byte) 5, "{nb=1}");
		
		//8=> 量词
		insertToArray(dat, '$', (byte) 8, "{q=1}");
		insertToArray(dat, '€', (byte) 8, "{q=1}");
		insertToArray(dat, '＄', (byte) 8, "{q=1}");
		insertToArray(dat, '￡', (byte) 8, "{q=1}");
		insertToArray(dat, '￥', (byte) 8, "{q=1}");
		
		for(int i = 0; i < puncs.length; i++){
			insertToArray(dat, puncs[i], (byte) 3, "{w=1}");
		}
		
		//4=> English Letter
		insertToArray(dat, '\'', (byte) 4, "{en=1}");
		for (int i = 'a'; i <= 'z'; i++) {
			insertToArray(dat, (char) i, (byte) 4, "{en=1}");
		}
		
		for (int i = 'ａ'; i <= 'ｚ'; i++) {
			insertToArray(dat, (char) i, (byte) 4, "{en=1}");
		}
		
		for (int i = 'Ａ'; i <= 'Ｚ'; i++) {
			insertToArray(dat, (char) i, (byte) 4, "{en=1}");
		}
		
		for (int i = 'A'; i <= 'Z'; i++) {
			insertToArray(dat, (char) i, (byte) 4, "{en=1}");
		}
		
		//5=> Number
		for (int i = '0'; i <= '9'; i++) {
			insertToArray(dat, (char) i, (byte) 5, "{nb=1}");
		}
		for (int i = '０'; i <= '９'; i++) {
			insertToArray(dat, (char) i, (byte) 5, "{nb=1}");
		}
		
		//6=> Russian Letter
		for (int i = 'а'; i <= 'я'; i++) {
			insertToArray(dat, (char) i, (byte) 6, "{ru=1}");
		}
		
		for (int i = 'А'; i <= 'Я'; i++) {
			insertToArray(dat, (char) i, (byte) 6, "{ru=1}");
		}
		
		//7=> Greek Letter
		for (int i = 945; i <= 969; i++) {
			if (i == 962)continue;
			insertToArray(dat, (char) i, (byte) 7, "{gr=1}");
		}
		
		for (int i = 913; i <= 937; i++) {
			if (i == 930)continue;
			insertToArray(dat, (char) i, (byte) 7, "{gr=1}");
		}
		
		//9=> 序号 serial number
		for (int i = 9312; i <= 9470; i++){
			insertToArray(dat, (char) i, (byte) 7, "{sn=1}");
		}

		
		datM.saveText("train_file/core.dct");

	}
	

	private static void insertToArray(Item[] dat, char c, byte status, String param) {
		AnalyzerItem item = new AnalyzerItem();
		item.name = String.valueOf(c);
		item.index = c;
		item.check = -1;
		item.status = status;
		item.param = param;
		dat[c] = item;
	}

	/**
	 * 输入词典的数据格式为[词性	词	频次（词性）]， 例如[nt	人大常委会	0]
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static void makeDic() throws NumberFormatException, IOException {
//		BufferedReader br = IOUtil.getReader("train_file/core.ini", "utf-8");//Jackie revise
		String path = "train_file/core.zip";
		BufferedReader br = DicReader.getZipFileBufferedReader(getZipFileStream(path));
		String temp = null;

		TreeMap<String, TreeMap<String, Integer>> dic = new TreeMap<String, TreeMap<String, Integer>>();

		while ((temp = br.readLine()) != null) {

			if (temp.indexOf('#') > -1) {
				continue;
			}

			temp = temp.replace(String.valueOf(((char) 0)), "");
			String[] split = temp.split("\t");

			if (dic.containsKey(split[0])) {
				if (dic.get(split[0]).containsKey(split[1])) {
					System.err.println(temp);
				}
				dic.get(split[0]).put(split[1], Integer.parseInt(split[2]));
			} else {
				try {
					TreeMap<String, Integer> tm = new TreeMap<String, Integer>();
					tm.put(split[1], Integer.parseInt(split[2]));
					dic.put(split[0], tm);
				} catch (Exception e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					System.err.println(temp);
				}
			}
		}

		IOUtil.writeMap(dic, "train_file/library.txt", IOUtil.UTF8);
	}
	
	private static InputStream getZipFileStream(String path) {
		InputStream is = null;
			try {
				// 获得zip信息
//				System.out.println(url.getPath());
				@SuppressWarnings("resource")
				ZipFile zipFile = new ZipFile(path);
				@SuppressWarnings("unchecked")
				Enumeration<ZipEntry> enu = (Enumeration<ZipEntry>) zipFile
						.entries();
				while (enu.hasMoreElements()) {
					ZipEntry zipElement = (ZipEntry) enu.nextElement();
					is = zipFile.getInputStream(zipElement);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return is;
	}
}
