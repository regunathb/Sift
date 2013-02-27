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
package org.sift.sieve.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.trpr.platform.runtime.impl.config.FileLocator;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * <code>POSTagger</code> includes methods for Part of Speech Tagging of a sentence.
 * 
 * @author devashishshankar
 * @version 1.0, 19 Feb 2013
 */
public class POSTagger {

	/** @link {MaxentTagger} which actually tags the sentence */
	private MaxentTagger tagger;
	
	/**'Noun' label by the tagger */
	public static String[] NOUNS = {"NN","NNS"};
	
	/** Label for Subordinating conjuction */
	public static String[] SUBORTINATING_CONJUNCTIONS = {"IN"};
	
	/** Character for separating tag from word */
	final public static String TAG_SEP_CHAR = "_";
	
	/**
	 * Default constructor
	 * @param taggerLocation Location of the tagger dataset
	 */
	public POSTagger(String taggerFileName) {
		File taggerFile = FileLocator.findUniqueFile(taggerFileName);
		try {
			//Initialize the tagger
			tagger = new MaxentTagger(taggerFile.getAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Tags a line with parts of speech, e.g 'good display' => 'good_AJ display_NN'
	 * @param line Input line to be tagged
	 * @return Tagged line, with tag annotation attached after each word
	 */
	public String tagLine(String line) {
		String taggedLine = tagger.tagString(line);
		return taggedLine;
	}

	/**
	 * Returns whether a given tagged word is a noun	
	 * @param taggedWord Word which is tagged (has tag annotation attached)
	 * @return true, if the word has been tagged a noun, false otherwise
	 */
	public boolean isNoun(String taggedWord) {
		if(Arrays.asList(POSTagger.NOUNS).contains(this.getTag(taggedWord))) { //Noun
			return true;
		}
		return false;
	}

	/**
	 * Returns whether a given tagged word is a subordinate conjunction	
	 * @param taggedWord Word which is tagged (has tag annotation attached)
	 * @return true, if the word has been tagged a subordinate conjunction, false otherwise 
	 */
	public boolean isSupConj(String taggedWord) {
		if(Arrays.asList(POSTagger.SUBORTINATING_CONJUNCTIONS).contains(this.getTag(taggedWord))) { //Subordinate conjuction
			return true;
		}
		return false;
	}
	
	/**
	 * Removes the tag from a word
	 * @param taggedWord Word which is tagged (has tag annotation attached)
	 * @return untagged word
	 */
	public String untag(String taggedWord) {
		if(!taggedWord.contains(POSTagger.TAG_SEP_CHAR)) { //No tag
			return taggedWord;
		}
		return taggedWord.substring(0,taggedWord.lastIndexOf(POSTagger.TAG_SEP_CHAR));
	}
	
	/**
	 * Gets the tag annotation from a given tagged word
	 * @param taggedWord Word which is tagged (has tag annotation attached)
	 * @return the tag 
	 */
	public String getTag(String taggedWord) {
		if(!taggedWord.contains(POSTagger.TAG_SEP_CHAR)) { //No tag
			return null;
		}
		return taggedWord.substring(taggedWord.lastIndexOf('_')+1);
	}
}
