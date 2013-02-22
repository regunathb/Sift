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

import org.sift.runtime.Fields;
import org.sift.runtime.Tuple;
import org.sift.runtime.spi.OutputCollector;
import org.sift.runtime.spi.Processor;
import org.sift.winnow.StopWords;

/**
 * The <code>LineSplitterProcessor</code> is an implementation of the {@link Processor} that splits {@link Tuple} values as lines following standard
 * interpretation of line boundaries
 * 
 * @author devashishshankar
 * @version 1.0, 28 Jan 2013
 */
public class LineSplitterProcessor implements Processor {

	/** Minimum length (in no. of characters) of a line for it to contain useful information */
	public static int minLineLength = 3;

	/** The regex for splitting text into lines. It follows the following rules:
	 * 1. Splits on '\n'
	 * 2. Splits on '.' except decimal numbers (like '3.5')
	 */
	public static String LINE_SPLIT_REGEX = "\\\\n|\\.(?!\\d)|(?<!\\d)\\.";
	
	/** Character representing new line */
	public static String NEW_LINE_CHAR = "\\n";
	
	/**
	 * Interface method implementation. Splits the string values in the specified Tuple into independent lines
	 * @see org.sift.runtime.spi.Processor#process(org.sift.runtime.Tuple, org.sift.runtime.spi.OutputCollector)
	 */
	public void process(Tuple tuple, OutputCollector collector) {

		Tuple returnTuple = new Tuple(Fields.KEY,Fields.SOURCES,Fields.VALUES);
		returnTuple.setValue(Fields.KEY, tuple.getString(Fields.KEY));
		returnTuple.setValue(Fields.SOURCES, tuple.getList(Fields.SOURCES));

		for (Object value : tuple.getList(Fields.VALUES)) {
			for(String line : this.getLines(((String)value).toLowerCase())) {
				if(line.length()>LineSplitterProcessor.minLineLength) {
					returnTuple.addToList(Fields.VALUES, line.trim());
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
	public String[] getLines(String rawLine) {
		String lines[] = rawLine.split(LineSplitterProcessor.LINE_SPLIT_REGEX);
		for(String line : lines){
			line.replaceAll(StopWords.LINE_BOUNDARY, "");
			line.replaceAll(LineSplitterProcessor.NEW_LINE_CHAR, "");
		}
		return lines;
	}	
}
