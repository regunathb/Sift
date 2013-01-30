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
package org.sift.batch.test;

import java.util.HashMap;
import java.util.Map;

import org.sift.runtime.Tuple;
import org.sift.runtime.impl.WordSplitterProcessor;
import org.sift.runtime.spi.OutputCollector;
import org.sift.runtime.spi.Processor;

/**
 * The <code>TagIdentifierProcessor</code> is an implementation of the {@link Processor} that identifies tags for a tag cloud from {@link Tuple} values.
 * This implementation uses the first value in the Tuple as the tag cloud subject and subsequent values as tags in the identified tag cloud.
 * This processor emits a Tuple for every identified tag with a value denoting its weight. Weights may be assigned per word length.
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class TagIdentifierProcessor  implements Processor {

	/** Default tag weight */
	public static final String DEFAULT_WEIGHT = "1";
	
	/** Map of word lengths and associated weights */
	private Map<String,String> wordWeights = new HashMap<String,String>();
	
	/**
	 * Interface method implementation. Identifies tags from the {@link Tuple} values
	 * @see org.sift.runtime.spi.Processor#process(org.sift.runtime.Tuple, org.sift.runtime.spi.OutputCollector)
	 */
	public void process(Tuple tuple, OutputCollector collector) {
		Object[] values = tuple.getValues().toArray();
		String tag = (String)values[0];
		for (int i = 1; i < values.length; i++) {
			Tuple returnTuple = new Tuple(tag + Tuple.KEY_SEP_CHAR + (String)values[i]);
			int wordsLength = WordSplitterProcessor.getWordsLength((String)values[i]);
			String weight = this.getWordWeights().get(String.valueOf(wordsLength));
			if (weight == null) {
				// assign the default weight
				weight = DEFAULT_WEIGHT;
			}
			returnTuple.addValue(weight);
			collector.emit(returnTuple);			
		}
	}
	
	/** Getter/Setter methods */
	public Map<String, String> getWordWeights() {
		return this.wordWeights;
	}
	public void setWordWeights(Map<String, String> wordWeights) {
		this.wordWeights = wordWeights;
	}
	
}

