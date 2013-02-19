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
import org.sift.winnow.StopWords;

/**
 * The <code>LineSplitterProcessor</code> is an implementation of the {@link Processor} that splits {@link Tuple} values as lines following standard
 * interpretation of line boundaries
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class LineSplitterProcessor implements Processor {
	
	/** Minimum length (in no. of characters) of a line for it to contain useful information */
	protected static int minLineLength = 3;
	
	/** The regex for splitting text into lines. It follows the following rules:
	 * 1. Splits on '\n'
	 * 2. Splits on '.' except decimal numbers (like '3.5')
	 */
	protected static String lineSplitRegex = "\\\\n|\\.(?!\\d)|(?<!\\d)\\.";
	
	/**
	 * Interface method implementation. Splits the string values in the specified Tuple into independent lines
	 * @see org.sift.runtime.spi.Processor#process(org.sift.runtime.Tuple, org.sift.runtime.spi.OutputCollector)
	 */
	public void process(Tuple tuple, OutputCollector collector) {
		Tuple returnTuple = new Tuple(tuple.getKey(), tuple.getSource());
		for (Object value : tuple.getValues()) {
			for(String line : this.getLines(((String)value).toLowerCase())) {
					if(line.length()>LineSplitterProcessor.minLineLength) {
						returnTuple.addValue(line.trim());
					}
			}	
		}
		collector.emit(returnTuple);
	}

	/**
	 * Helper method to take a raw line of text and return an array of strings, each representing a separate line
	 * @param rawLine the raw line to process
	 * @return array of line strings
	 */
	protected String[] getLines(String rawLine) {
		String a[] = rawLine.split(LineSplitterProcessor.lineSplitRegex);
		for(String i : a){
			i.replaceAll(StopWords.LINE_BOUNDARY, "");
			i.replaceAll("\\n", "");
		}
		
		return a;
	}	
}
