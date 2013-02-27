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
package org.sift.runtime.impl;

import java.util.List;

import org.sift.runtime.Fields;
import org.sift.runtime.Tuple;
import org.sift.runtime.spi.OutputCollector;
import org.sift.runtime.spi.Processor;
import org.sift.winnow.StopWords;

/**
 * The <code>WordSplitterProcessor</code> is an implementation of the {@link Processor} that splits {@link Tuple} values as words following standard
 * interpretation of word boundaries
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class WordSplitterProcessor implements Processor {

	/** The n-grams to extract */
	private int nGram = StopWords.DEFAULT_N_GRAM;

	/** Additional stop words, if any */
	private StopWords stopWords;

	/**
	 * Interface method implementation. Splits the string values in the specified Tuple into independent words
	 * @see org.sift.runtime.spi.Processor#process(org.sift.runtime.Tuple, org.sift.runtime.spi.OutputCollector)
	 */
	public void process(Tuple tuple, OutputCollector collector) {
		Tuple returnTuple = tuple.clone();
		returnTuple.setValue(Fields.VALUES, null);
		List<Object> values = tuple.getList(Fields.VALUES);
		for(Object value:values) {
			String line = (String) value;
			String[] tokens = line.split(StopWords.WORD_BOUNDARY);
			for (int i = 0; i < tokens.length; i++) {
				StringBuffer tokenBuffer = new StringBuffer();
				for (int j = 0; j < this.getnGram(); j++) {
					if (i+j <  tokens.length) {
						tokenBuffer.append(tokens[i+j]);
						tokenBuffer.append(StopWords.WORD_BOUNDARY_STRING);
					} else {
						break;
					}
					String word = tokenBuffer.toString().trim();
					if (this.getStopWords() != null && !this.getStopWords().isStopWord(word)) {
						returnTuple.addToList(Fields.VALUES, word);
					}
				}
			}
		}
		collector.emit(returnTuple);
	}

	/**
	 * Convenience method to consistently return word lengths as interpreted by Sift
	 * @param words the String containing one or more words
	 * @return numbers of words found in the specified string
	 */
	public static int getWordsLength(String words) {
		return words.split(StopWords.WORD_BOUNDARY).length;
	}

	/** Getter/Setter for values */
	public int getnGram() {
		return this.nGram;
	}
	public void setnGram(int nGram) {
		this.nGram = nGram;
	}
	public StopWords getStopWords() {
		return this.stopWords;
	}
	public void setStopWords(StopWords stopWords) {
		this.stopWords = stopWords;
	}

}
