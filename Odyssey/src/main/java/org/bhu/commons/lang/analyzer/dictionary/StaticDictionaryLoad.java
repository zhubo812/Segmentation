package org.bhu.commons.lang.analyzer.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.bhu.commons.lang.analyzer.bean.AnalyzerItem;
import org.bhu.commons.lang.analyzer.crf.CRFSegment;
import org.bhu.commons.lang.analyzer.crf.Model;
import org.bhu.commons.lang.analyzer.library.DATDictionary;
import org.bhu.commons.lang.analyzer.util.IOUtil;
import org.bhu.commons.lang.analyzer.util.StringUtil;

/**
 * 这个类储存一些公用变量.
 * 
 * @author Jackie
 * 
 */
public class StaticDictionaryLoad {

	public static final Logger LIBRARYLOG = Logger.getLogger("DICLOG");

	// 是否开启人名识别
	public static boolean isNameRecognition = true;

	private static final Lock LOCK = new ReentrantLock();

	// 是否开启数字识别
	public static boolean isNumRecognition = true;

	// 是否数字和量词合并
	public static boolean isQuantifierRecognition = true;
	
	// 是否开启时间表达式识别
	public static boolean isTimeRecognition = true;

	// crf 模型

	private static CRFSegment crfSegment = null;

	public static boolean isRealName = false;

	/**
	 * 用户自定义词典的加载,如果是路径就扫描路径下的dic文件
	 */
	public static String userLibrary = null;

	public static String ambiguityLibrary = "data/ambiguity.dat";
	
//	private static final String RESOURCE = "/org/bhu/commons/lang/analyzer/resources/";
	private static final String RESOURCE = "resources/";
	

	/**
	 * 是否用户辞典不加载相同的词
	 */
	public static boolean isSkipUserDefine = false;

	static {
		/**
		 * 配置文件变量
		 */
		try {
			Properties props = new Properties();
			props.load(new FileInputStream("conf/library.properties"));
			userLibrary = "data";
			ambiguityLibrary = String.format("%s/ambiguity.dat",userLibrary);
			if (props.containsKey("isSkipUserDefine"))
				isSkipUserDefine = Boolean.valueOf(props.getProperty("isSkipUserDefine"));
			if (props.containsKey("isRealName"))
				isRealName = Boolean.valueOf(props.getProperty("isRealName"));
		} catch (Exception e) {
			LIBRARYLOG.warning("not find library.properties in classpath use it by default !");
		}
	}
	
	private static InputStream getStream(String name){
//		return StaticDictionaryLoad.class.getResourceAsStream(String.format("%s%s", RESOURCE,name));
		InputStream input = null;
//		System.out.println(name);
		try {
			input =new FileInputStream(new File(String.format("%s%s", RESOURCE,name)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
	
	private static InputStream getZipFileStream(String name) {
//		String path = StaticDictionaryLoad.class.getResource(String.format("%s%s", RESOURCE,name)).getPath();
		String path = String.format("%s%s", RESOURCE,name);
//		System.out.println(path);
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
				StaticDictionaryLoad.LIBRARYLOG.info(path);
			}
			return is;
	}

	/**
	 * 人名词典
	 * 
	 * @return
	 */
	public static BufferedReader getPersonReader() {
// 		return DicReader.getZipFileBufferedReader(getZipFileStream("person.bin"));
		return DicReader.getReader(getStream("person.dct"));
	}
	
	/***
	 * 存储中文姓名 数据
	 * @return
	 */
	public static BufferedReader getAsianNameReader(){
		return DicReader.getZipFileBufferedReader(getZipFileStream("asianname.zip"));
	}

	/**
	 * 机构名词典
	 * 
	 * @return
	 */
	public static BufferedReader getCompanReader() {
		return DicReader.getReader(getStream("company.data"));
	}

	/**
	 * 机构名词典
	 * 
	 * @return
	 */
	public static BufferedReader getNewWordReader() {
		return DicReader.getZipFileBufferedReader(getZipFileStream("neoword.bin"));
	}


	/**
	 * 数字词典
	 * 
	 * @return
	 */
	public static BufferedReader getNumberReader() {
		return DicReader.getZipFileBufferedReader(getZipFileStream("numberLibrary.bin"));
	}

	/**
	 * 英文词典
	 * 
	 * @return
	 */
	public static BufferedReader getEnglishReader() {
		return DicReader.getZipFileBufferedReader(getZipFileStream("englishLibrary.bin"));
	}

	/**
	 * 词性表
	 * 
	 * @return
	 */
	public static BufferedReader getNatureMapReader() {
		return DicReader.getReader(getStream("nature.map"));
	}

	/**
	 * 词性关联表
	 * 
	 * @return
	 */
	public static BufferedReader getNatureTableReader() {
		return DicReader.getReader(getStream("nature.table"));
	}
	
	public static BufferedReader getNatureRelationReader() {
		return DicReader.getReader(getStream("nature.model"));
	}

	/**
	 * 得到姓名单字的词频词典
	 * 
	 * @return
	 */
	public static BufferedReader getPersonFreqReader() {
//		System.out.println("loading person/name_freq.dic");
		return DicReader.getReader("person/name_freq.dic");
	}
	
	/**
	 * 字母词词典
	 * 
	 * @return
	 */
	public static BufferedReader getAphWordReader() {
		return DicReader.getZipFileBufferedReader(getZipFileStream("aphword.bin"));
	}

	/**
	 * 名字词性对象反序列化
	 * 可以停用
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, int[][]> getPersonFreqMap() {
		InputStream inputStream = null;
		ObjectInputStream objectInputStream = null;
		Map<String, int[][]> map = new HashMap<String, int[][]>(0);
		try {
			inputStream = getStream("asian_name.data");
			objectInputStream = new ObjectInputStream(inputStream);
			map = (Map<String, int[][]>) objectInputStream.readObject();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectInputStream != null)
					objectInputStream.close();
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * 词与词之间的关联表数据
	 * 
	 * @return
	 */
	public static void initBigram() {
		BufferedReader reader = null;
		try {
			reader = DicReader.getReader(getStream("model.dct"));
			String temp = null;
			String[] strs = null;
			int freq = 0;
			while ((temp = reader.readLine()) != null) {
				if (StringUtil.isBlank(temp)) {
					continue;
				}
				strs = temp.split("\t");
				String[] array = strs[1].split(" ");
				for(String item : array){
					String[] tmp = item.split("=");
					freq = Integer.parseInt(tmp[1]);
					AnalyzerItem fromItem = DATDictionary.getItem(strs[0]);

					AnalyzerItem toItem = DATDictionary.getItem(tmp[0]);
					if (fromItem == AnalyzerItem.NULL && strs[0].contains("#")) {
						fromItem = AnalyzerItem.BEGIN;
					}

					if (toItem == AnalyzerItem.NULL && tmp[0].contains("#")) {
						toItem = AnalyzerItem.END;
					}

					if (fromItem == AnalyzerItem.NULL || toItem == AnalyzerItem.NULL) {
						continue;
					}
					
					if(fromItem.bigramEntryMap==null){
						fromItem.bigramEntryMap = new HashMap<Integer, Integer>() ;
					}

					fromItem.bigramEntryMap.put(toItem.index, freq) ;
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.close(reader);
		}
		
	}
	

	/**
	 * 得到默认的模型
	 * 
	 * @return
	 */

	public static CRFSegment getCRFSplitWord() {
		// TODO Auto-generated method stub
		if (crfSegment != null) {
			return crfSegment;
		}
		LOCK.lock();
		if (crfSegment != null) {
			return crfSegment;
		}

		try {
			long start = System.currentTimeMillis();
			LIBRARYLOG.info("begin init crf model!");
			crfSegment = new CRFSegment(Model.loadModel(DicReader.getInputStream(String.format("%s%s", RESOURCE,"crf.model"))));
			LIBRARYLOG.info("load crf crf use time:" + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			LOCK.unlock();
		}

		return crfSegment;
	}

}
