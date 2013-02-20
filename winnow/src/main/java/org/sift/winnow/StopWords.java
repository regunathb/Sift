/*
 * Copyright 2012-2015, the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sift.winnow;

import java.util.LinkedList;
import java.util.List;

/**
 * The <code>StopWords</code> is a static collection of pre-determined stop words in the English
 * language.
 * 
 * @author Regunath B
 * @version 1.0, 24 Jan 2013
 */
public class StopWords {
	
	/** The regex used to identify line boundaries */
	public static final String LINE_BOUNDARY = "\\\\n";
	
	/** The regex used to identify word boundaries */
	public static final String WORD_BOUNDARY = "\\s+|[^a-zA-Z0-9]+"; // includes multiple occurrences of spaces, special chars except . (dot)

	/** Word boundary for n-grams*/
	public static final String WORD_BOUNDARY_STRING = " ";	
	
	/** The default value for n-grams*/
	public static final int DEFAULT_N_GRAM = 1;
		
	/** List of stop words */	
	private static final String[] STOP_WORDS = {
		"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9",
		"/","\\","\\n",",",".","<",">","!","&","*","%","$","#","@","-","+","//","\\\\","(",")",
		"an","and","are","as","at","be","but","by","eight","five","for","four","go",
		"has","have","he","her","his","if","in","into","is","it","its","my","nine","no","not","of","on",
		"one","or","seven","six","so","such","ten","that","the","then","there","these","they","this","three","to","too","two",
		"will","with",
		"&amp;","&quot;","amp","quot","&gt","&mdash","gt","mdash",
	};
	
	/** List of conjunctions */
	private static final String[] CONJUNCTIONS = {
		"a","an","and","as","at","both","but","either","for","is","just","neither","nor","of","only","or",
		"so","this","the","to","whether","yet",
	};
	
	/** List of stop words */
	private List<String> stopWords = new LinkedList<String>();

	/** List of stop words */
	private List<String> conjunctionWords = new LinkedList<String>();

	/**
	 * Constructor for this class
	 */
	public StopWords() {
		for (String word : STOP_WORDS) {
			this.stopWords.add(word);
		}
		for (String word : CONJUNCTIONS) {
			this.conjunctionWords.add(word);
		}		
	}
	
	/**
	 * Determines if the specified word is a stop word by checking against the static list of words maintained by this class
	 * @param word the word to be checked for stop words
	 * @return true if it is a stop word, false otherwise
	 */
	public boolean isStopWord(String word) {
		if (this.stopWords.contains(word)) {
			return true;
		} 
		// check if it is a stop word, but is at the start (or at the end) of the phrase
		String[] words = word.split(WORD_BOUNDARY);
		if (this.conjunctionWords.contains(words[0]) || this.conjunctionWords.contains(words[words.length - 1])) {
			return true;
		}
		return false;
	}

	/** Getter/Setter methods */	
	public List<String> getStopWords() {
		return this.stopWords;
	}
	public void setStopWords(List<String> stopWords) {
		this.stopWords = stopWords;
	}
	public List<String> getConjunctionWords() {
		return this.conjunctionWords;
	}
	public void setConjunctionWords(List<String> conjunctionWords) {
		this.conjunctionWords = conjunctionWords;
	}

}