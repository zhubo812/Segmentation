package org.bhu.commons.lang.analyzer.dictionary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.bhu.commons.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import org.bhu.commons.lang.algoritm.ahocorasick.trie.Trie;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author Jackie
 */
public class TestAhoCorasickDoubleArrayTrie extends TestCase
{
    private AhoCorasickDoubleArrayTrie<String> buildASimpleAhoCorasickDoubleArrayTrie()
    {
        // Collect test data set
        TreeMap<String, String> map = new TreeMap<String, String>();
        String[] keyArray = new String[]
                {
                        "hers",
                        "his",
                        "she",
                        "he"
                };
        for (String key : keyArray)
        {
            map.put(key, key);
        }
        // Build an AhoCorasickDoubleArrayTrie
        AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<String>();
        acdat.build(map);
        return acdat;
    }

    private void validateASimpleAhoCorasickDoubleArrayTrie(AhoCorasickDoubleArrayTrie<String> acdat)
    {
        // Test it
        final String text = "uhers";
        acdat.parseText(text, new AhoCorasickDoubleArrayTrie.IHit<String>()
        {
            @Override
            public void hit(int begin, int end, String value)
            {
                System.out.printf("[%d:%d]=%s\n", begin, end, value);
                assertEquals(text.substring(begin, end), value);
            }
        });
        // Or simply use
        List<AhoCorasickDoubleArrayTrie<String>.Hit<String>> wordList = acdat.parseText(text);
        System.out.println(wordList);
    }
    public void testBuildAndParseSimply() throws Exception
    {
        AhoCorasickDoubleArrayTrie<String> acdat = buildASimpleAhoCorasickDoubleArrayTrie();
        validateASimpleAhoCorasickDoubleArrayTrie(acdat);
    }

    public void testBuildAndParseWithBigFile() throws Exception
    {
        // Load test data from disk
        Set<String> dictionary = loadDictionary("cn/dictionary.txt");
        final String text = loadText("cn/text.txt");
        // You can use any type of Map to hold data
        Map<String, String> map = new TreeMap<String, String>();
//        Map<String, String> map = new HashMap<String, String>();
//        Map<String, String> map = new LinkedHashMap<String, String>();
        for (String key : dictionary)
        {
            map.put(key, key);
        }
        // Build an AhoCorasickDoubleArrayTrie
        AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<String>();
        acdat.build(map);
        // Test it
        acdat.parseText(text, new AhoCorasickDoubleArrayTrie.IHit<String>()
        {
            @Override
            public void hit(int begin, int end, String value)
            {
                assertEquals(text.substring(begin, end), value);
            }
        });
    }

    private String loadText(String path) throws IOException
    {
        StringBuilder sbText = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(path), "UTF-8"));
        String line;
        while ((line = br.readLine()) != null)
        {
            sbText.append(line).append("\n");
        }
        br.close();

        return sbText.toString();
    }

    private Set<String> loadDictionary(String path) throws IOException
    {
        Set<String> dictionary = new TreeSet<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(path), "UTF-8"));
        String line;
        while ((line = br.readLine()) != null)
        {
            dictionary.add(line);
        }
        br.close();

        return dictionary;
    }

    private void runTest(String dictionaryPath, String textPath) throws IOException
    {
        Set<String> dictionary = loadDictionary(dictionaryPath);
        String text = loadText(textPath);
        // Build a ahoCorasickNaive implemented by robert-bor
        Trie ahoCorasickNaive = new Trie();
        for (String word : dictionary)
        {
            ahoCorasickNaive.addKeyword(word);
        }
        ahoCorasickNaive.parseText(""); // More fairly, robert-bor's implementation needs to call this to build ac automata.
        // Build a AhoCorasickDoubleArrayTrie implemented by hankcs
        AhoCorasickDoubleArrayTrie<String> ahoCorasickDoubleArrayTrie = new AhoCorasickDoubleArrayTrie<String>();
        TreeMap<String, String> dictionaryMap = new TreeMap<String, String>();
        for (String word : dictionary)
        {
            dictionaryMap.put(word, word);  // we use the same text as the property of a word
        }
        ahoCorasickDoubleArrayTrie.build(dictionaryMap);
        // Let's test the speed of the two Aho-Corasick automata
        System.out.printf("Parsing document which contains %d characters, with a dictionary of %d words.\n", text.length(), dictionary.size());
        long start = System.currentTimeMillis();
        ahoCorasickNaive.parseText(text);
        long costTimeNaive = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        ahoCorasickDoubleArrayTrie.parseText(text, new AhoCorasickDoubleArrayTrie.IHit<String>()
        {
            @Override
            public void hit(int begin, int end, String value)
            {

            }
        });
        long costTimeACDAT = System.currentTimeMillis() - start;
        System.out.printf("%-15s\t%-15s\t%-15s\n", "", "Naive", "ACDAT");
        System.out.printf("%-15s\t%-15d\t%-15d\n", "time", costTimeNaive, costTimeACDAT);
        System.out.printf("%-15s\t%-15.2f\t%-15.2f\n", "char/s", (text.length() / (costTimeNaive / 1000.0)), (text.length() / (costTimeACDAT / 1000.0)));
        System.out.printf("%-15s\t%-15.2f\t%-15.2f\n", "rate", 1.0, costTimeNaive / (double) costTimeACDAT);
        System.out.println("===========================================================================");
    }

    /**
     * Compare my AhoCorasickDoubleArrayTrie with robert-bor's aho-corasick, notice that robert-bor's aho-corasick is
     * compiled under jdk1.8, so you will need jdk1.8 to run this test<br>
     * To avoid JVM wasting time on allocating memory, please use -Xms512m -Xmx512m -Xmn256m .
     * @throws Exception
     */
    @Test
    public void testBenchmark() throws Exception
    {
        runTest("en/dictionary.txt", "en/text.txt");
        runTest("cn/dictionary.txt", "cn/text.txt");
    }

    public void testSaveAndLoad() throws Exception
    {
        AhoCorasickDoubleArrayTrie<String> acdat = buildASimpleAhoCorasickDoubleArrayTrie();
        final String tmpPath = System.getProperty("java.io.tmpdir").replace("\\\\", "/") + "/acdat.tmp";
        System.out.println("Saving acdat to: " + tmpPath);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tmpPath));
        out.writeObject(acdat);
        out.close();
        System.out.println("Loading acdat from: " + tmpPath);
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(tmpPath));
        acdat = (AhoCorasickDoubleArrayTrie<String>) in.readObject();
        validateASimpleAhoCorasickDoubleArrayTrie(acdat);
    }
}
