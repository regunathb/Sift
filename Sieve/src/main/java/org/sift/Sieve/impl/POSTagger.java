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
package org.sift.Sieve.impl;

import java.io.IOException;
import java.util.Arrays;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * <code>POSTagger</code> includes methods for Part of Speech Tagging of a sentence.
 * 
 * @author devashishshankar
 * @version 1.0, 19 Feb 2013
 */
public class POSTagger {
	
	/** Location of the tagger model used for POS tagging. A tagger consists of a trained data set used for tagging */
	private String taggerLocation;
	
	/** @link {MaxentTagger} which actually tags the sentence */
	static MaxentTagger tagger;
	
	/**'Noun' label by the tagger */
	private String[] nouns = {"NN","NNS"};
	
	/** Label for Subordinating conjuction */
	private String[] conj = {"IN"};
	
	/**
	 * Default constructor
	 * @param taggerLocation Location of the tagger dataset
	 */
	public POSTagger(String taggerLocation) {
		this.taggerLocation = taggerLocation;
		try {
			//Initialize the tagger
			tagger = new MaxentTagger(this.taggerLocation);
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
		if(Arrays.asList(nouns).contains(this.getTag(taggedWord))) { //Noun
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
		if(Arrays.asList(conj).contains(this.getTag(taggedWord))) { //Subordinate conjuction
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
		if(!taggedWord.contains("_")) { //No tag
			return taggedWord;
		}
		return taggedWord.substring(0,taggedWord.lastIndexOf('_'));
	}
	
	/**
	 * Gets the tag annotation from a given tagged word
	 * @param taggedWord Word which is tagged (has tag annotation attached)
	 * @return the tag 
	 */
	public String getTag(String taggedWord) {
		if(!taggedWord.contains("_")) { //No tag
			return null;
		}
		return taggedWord.substring(taggedWord.lastIndexOf('_')+1);
	}
}
