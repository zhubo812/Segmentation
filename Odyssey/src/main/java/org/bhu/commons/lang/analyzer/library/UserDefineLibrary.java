package org.bhu.commons.lang.analyzer.library;

import static org.bhu.commons.lang.analyzer.dictionary.StaticDictionaryLoad.LIBRARYLOG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.bhu.commons.lang.analyzer.dictionary.StaticDictionaryLoad;
import org.bhu.commons.lang.analyzer.util.IOUtil;
import org.bhu.commons.lang.analyzer.util.StringUtil;
import org.bhu.commons.lang.trie.domain.Forest;
import org.bhu.commons.lang.trie.domain.Value;
import org.bhu.commons.lang.trie.domain.WoodInterface;
import org.bhu.commons.lang.trie.library.Library;

;

/**
 * 用户自定义词典操作类
 * 
 * @author Jackie
 */
public class UserDefineLibrary {

	public static final String DEFAULT_NATURE = "userDefine";

	public static final Integer DEFAULT_FREQ = 1000;

	public static final String DEFAULT_FREQ_STR = "1000";

	public static Forest FOREST = null;

	public static Forest ambiguityForest = null;

	static {
		initUserLibrary();
		initAmbiguityLibrary();
		try {
			ambiguityForest = Library.makeForest(StaticDictionaryLoad.ambiguityLibrary);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 关键词增加
	 * 
	 * @param keyWord
	 *            所要增加的关键词
	 * @param nature
	 *            关键词的词性
	 * @param freq
	 *            关键词的词频
	 */
	public static void insertWord(String keyword, String nature, int freq) {
		String[] paramers = new String[2];
		paramers[0] = nature;
		paramers[1] = String.valueOf(freq);
		Value value = new Value(keyword, paramers);
		Library.insertWord(FOREST, value);
	}
	
	public static void insertWord(List<String> keywords, String nature, int freq) {
		for(String keyword : keywords){
			String[] paramers = new String[2];
			paramers[0] = nature;
			paramers[1] = String.valueOf(freq);
			Value value = new Value(keyword, paramers);
			Library.insertWord(FOREST, value);
		}
	}

	/**
	 * 加载纠正词典
	 */
	private static void initAmbiguityLibrary() {
		String ambiguityLibrary = StaticDictionaryLoad.ambiguityLibrary;
		if (StringUtil.isBlank(ambiguityLibrary)) {
			LIBRARYLOG.warning("init ambiguity  warning :" + ambiguityLibrary + " because : file not found or failed to read !");
			return;
		}
		ambiguityLibrary = StaticDictionaryLoad.ambiguityLibrary;
		File file = new File(ambiguityLibrary);
		if (file.isFile() && file.canRead()) {
			try {
				ambiguityForest = Library.makeForest(ambiguityLibrary);
				if(ambiguityLibrary == null){
					System.err.println("ambiguityLibrary is not loaded correctly!");
				}
			} catch (Exception e) {
				LIBRARYLOG.warning("init ambiguity  error :" + new File(ambiguityLibrary).getName() + " because : not find that file or can not to read !");
				e.printStackTrace();
			}
			LIBRARYLOG.info("init ambiguityLibrary ok!");
		} else {
			LIBRARYLOG.warning("init ambiguity  warning :" + new File(ambiguityLibrary).getName() + " because : file not found or failed to read !");
		}
	}

	/**
	 * 加载用户自定义词典和补充词典
	 */
	private static void initUserLibrary() {
		// TODO Auto-generated method stub
		try {
			FOREST = new Forest();
			// 加载用户自定义词典
			String userLibrary = StaticDictionaryLoad.userLibrary;
			loadLibrary(FOREST, userLibrary);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 单个文件加载词典
	public static void loadFile(Forest forest, File file) {
		// TODO Auto-generated method stub
		if (!file.canRead()) {
			LIBRARYLOG.warning("file in path " + file.getName() + " can not to read!");
			return;
		}
		String temp = null;
		BufferedReader br = null;
		String[] strs = null;
		Value value = null;
		try {
			br = IOUtil.getReader(new FileInputStream(file), "UTF-8");
			while ((temp = br.readLine()) != null) {
				if (StringUtil.isBlank(temp)||temp.startsWith("//")) {
					continue;
				} else {
					strs = temp.split("\t");

					strs[0] = strs[0].toLowerCase();

					// 如何核心辞典存在那么就放弃
					if (StaticDictionaryLoad.isSkipUserDefine && DATDictionary.getId(strs[0]) > 0) {
						continue;
					}

					if (strs.length != 3) {
						value = new Value(strs[0], DEFAULT_NATURE, DEFAULT_FREQ_STR);
					} else {
						value = new Value(strs[0], strs[1], strs[2]);
					}
					Library.insertWord(forest, value);
				}
			}
			LIBRARYLOG.info("init user userLibrary ok path is : " + file.getName());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtil.close(br);
			br = null;
		}
	}

	/**
	 * 加载词典,传入一本词典的路径.或者目录.词典后缀必须为.dic
	 */
	public static void loadLibrary(Forest forest, String path) {
		// 加载用户自定义词典
		File file = null;
		if (path != null) {
			file = new File(path);
			if (!file.canRead() || file.isHidden()) {
				LIBRARYLOG.warning("init userLibrary  warning :" + new File(path).getAbsolutePath() + " because : file not found or failed to read !");
				return;
			}
			if (file.isFile()) {
				loadFile(forest, file);
			} else if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().trim().endsWith(".dic")) {
						loadFile(forest, files[i]);
					}
				}
			} else {
				LIBRARYLOG.warning("init user library  error :" + new File(path).getAbsolutePath() + " because : not find that file !");
			}
		}
	}

	/**
	 * 删除关键词
	 */
	public static void removeWord(String word) {
		Library.removeWord(FOREST, word);
	}

	public static String[] getParams(String word) {
		WoodInterface temp = FOREST;
		for (int i = 0; i < word.length(); i++) {
			temp = temp.get(word.charAt(i));
			if (temp == null) {
				return null;
			}
		}
		if (temp.getStatus() > 1) {
			return temp.getParams();
		} else {
			return null;
		}
	}

	public static String[] getParams(Forest forest, String word) {
		WoodInterface temp = forest;
		for (int i = 0; i < word.length(); i++) {
			temp = temp.get(word.charAt(i));
			if (temp == null) {
				return null;
			}
		}
		if (temp.getStatus() > 1) {
			return temp.getParams();
		} else {
			return null;
		}
	}

	public static boolean contains(String word) {
		return getParams(word) != null;
	}

	/**
	 * 将用户自定义词典清空
	 */
	public static void clear() {
		FOREST.clear();
	}

}
