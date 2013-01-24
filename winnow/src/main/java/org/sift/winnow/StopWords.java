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
	
	/** List of stop words */	
	private static final List<String> STOP_WORDS = new LinkedList<String>();
	
	static {
		STOP_WORDS.add("is");
		STOP_WORDS.add("in");
		STOP_WORDS.add("on");
		STOP_WORDS.add("at");
		STOP_WORDS.add("the");
		STOP_WORDS.add("this");
		STOP_WORDS.add("his");
		STOP_WORDS.add("her");
		STOP_WORDS.add("by");
		STOP_WORDS.add("of");
		STOP_WORDS.add("if");
		STOP_WORDS.add("with");
		STOP_WORDS.add("for");
		STOP_WORDS.add("it");		
		STOP_WORDS.add("you");		
		STOP_WORDS.add("my");		
		STOP_WORDS.add("that");		
		STOP_WORDS.add("they");		
		STOP_WORDS.add("and");		
		STOP_WORDS.add("to");		
		STOP_WORDS.add("are");		
		STOP_WORDS.add("but");		
		STOP_WORDS.add("be");		
		STOP_WORDS.add("as");		
		STOP_WORDS.add("these");		
	}
	
	/**
	 * Determines if the specified word is a stop word by checking against the static list of words maintained by this class
	 * @param word the word to be checked for stop words
	 * @return true if it is a stop word, false otherwise
	 */
	public static boolean isStopWord(String word) {
		return StopWords.STOP_WORDS.contains(word);
	}
	
}
