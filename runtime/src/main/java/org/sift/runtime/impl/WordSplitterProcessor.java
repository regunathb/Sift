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

import org.sift.runtime.Tuple;
import org.sift.runtime.spi.OutputCollector;
import org.sift.runtime.spi.Processor;

/**
 * The <code>WordSplitterProcessor</code> is an implementation of the {@link Processor} that splits {@link Tuple} values as words following standard
 * interpretation of word boundaries
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class WordSplitterProcessor implements Processor {
	
	/** The regex used to identify word boundaries */
	private static final String WORD_BOUNDARY = "\\s+";

	/**
	 * Interface method implementation. Splits the string values in the specified Tuple into independent words
	 * @see org.sift.runtime.spi.Processor#process(org.sift.runtime.Tuple, org.sift.runtime.spi.OutputCollector)
	 */
	public void process(Tuple tuple, OutputCollector collector) {
		Tuple returnTuple = new Tuple(tuple.getKey());
		for (Object line : tuple.getValues()) {
			String[] tokens = ((String)line).toLowerCase().split(WORD_BOUNDARY);
			for (String token : tokens) {
				returnTuple.addValue(token);
			}			
		}
		collector.emit(returnTuple);
	}
	
	/**
	 * Conveninece method to consistently return word lengths as interpreted by Sift
	 * @param words the String containing one or more words
	 * @return numbers of words found in the specified string
	 */
	public static int getWordsLength(String words) {
		return words.split(WORD_BOUNDARY).length;
	}
}
