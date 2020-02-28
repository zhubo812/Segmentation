package org.bhu.commons.lang.analyzer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bhu.commons.lang.analyzer.bean.NatureScore;
import org.bhu.commons.lang.analyzer.bean.Term;
import org.bhu.commons.lang.analyzer.bean.TermNature;
import org.bhu.commons.lang.analyzer.library.NatureLibrary;

public class NatureHelper {
	private static Set<String> natureSet = new HashSet<String>();
	static{
		natureSet.add("nr");
		natureSet.add("nry");
		natureSet.add("jnr");
		natureSet.add("ns");
		natureSet.add("t");
	}
	private static Term[] terms = null;

	public static List<Term> modifyNatures(List<Term> list){
		terms = (Term[])list.toArray(new Term[list.size()]);
		
		for(int i=0;i<list.size()-1;i++){
//			System.out.println(list.get(i).natrue().natureStr);
			if(natureSet.contains(list.get(i).natrue().natureStr)){
				continue;
			}
			updateNature(list.get(i), list.get(i+1), i);
		}
		
		
		return Arrays.asList(terms);
	}
	
	private static void updateNature(Term term1, Term term2, int index){
		TermNature[] natures1 = term1.termNatures().termNatures;
		TermNature[] natures2 = term2.termNatures().termNatures;
		List<NatureScore> scores = new ArrayList<NatureScore>();
		if(natures1.length==1&&natures2.length==1){
			return;
		}
		for(int i=0; i< natures1.length;i++){
			for(int j=0; j<natures2.length;j++){
				double score = NatureLibrary.getNaturesValue(natures1[i].nature.natureStr, natures2[j].nature.natureStr);
				NatureScore ns = new NatureScore(index, index+1, i, j, score);
				scores.add(ns);
			}
		}
		CollectionUtil.sortList(scores, "score", "DESC");
		NatureScore ns = scores.get(0);
		term1.setNature(term1.termNatures().termNatures[ns.getHeadnatureIdx()].nature);
		term2.setNature(term2.termNatures().termNatures[ns.getTailnatureIdx()].nature);
		
		terms[index]= term1;
		terms[index+1] = term2;
		
	}
	
	private void printNatures(Term term){
		TermNature[] natures = term.termNatures().termNatures;
		for(TermNature nature : natures){
			System.out.print(nature.nature.natureStr+"\t");
			
		}
		System.out.println();
	}
}
