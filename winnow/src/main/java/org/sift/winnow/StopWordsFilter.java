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

import org.sift.runtime.Tuple;
import org.sift.runtime.spi.OutputCollector;
import org.sift.runtime.spi.Processor;


/**
 * The <code>StopWordsFilter</code> is an implementation of the Sift runtime's {@link Processor} that filters out stop words in the {@link Tuple} data list
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class StopWordsFilter implements Processor {
	
	/** the StopWords instance */
	private StopWords stopWords;

	/**
	 * Interface method implementation. Removes stop words words in the specified Tuple and emits a Tuple without them
	 * @see org.sift.runtime.spi.Processor#process(org.sift.runtime.Tuple, org.sift.runtime.spi.OutputCollector)
	 */
	public void process(Tuple tuple, OutputCollector collector) {
		Tuple output = new Tuple(tuple.getKey());
		for (Object word : tuple.getValues()) {
			if (!this.stopWords.isStopWord((String)word)) {
				output.addValue(word);
			}
		}
		collector.emit(output);
	}
	
	/** Getter/Setter methods */
	public StopWords getStopWords() {
		return this.stopWords;
	}
	public void setStopWords(StopWords stopWords) {
		this.stopWords = stopWords;
	}

}
