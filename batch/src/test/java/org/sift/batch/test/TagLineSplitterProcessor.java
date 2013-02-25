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

import org.sift.runtime.Fields;
import org.sift.runtime.Tuple;
import org.sift.runtime.impl.LineSplitterProcessor;
import org.sift.runtime.spi.OutputCollector;
import org.sift.runtime.spi.Processor;

/**
 * The <code>TagLineSplitterProcessor</code> is an implementation of the {@link Processor} that splits {@link Tuple}
 *  values as lines using the functionalities of <code>TagLineSplitterProcessor</code>, but keeps intact the
 *  tag value interpretation of Trooper (i.e, makes the first element of value as tagValue)
 * 
 * @author devashishshankar
 * @version 1.0, 18 Feb 2013
 */
public class TagLineSplitterProcessor implements Processor {

	/** Instance of the generic {@link LineSplitterProcessor} which has methods for Line splitting*/
	LineSplitterProcessor lineSplitterProcessor;

	/** Character delimiting the first value (which is tag value) */
	static public String TAG_VALUE_SEP_CHAR = "\t";

	/**
	 * Default constructor. Injects {@link LineSplitterProcessor} into this class
	 * @param lineSplitterProcessor
	 */
	public TagLineSplitterProcessor(LineSplitterProcessor lineSplitterProcessor) {
		this.lineSplitterProcessor = lineSplitterProcessor;
	}

	/**
	 * Interface method implementation. Splits the string values in the specified Tuple into independent lines
	 * @see org.sift.runtime.spi.Processor#process(org.sift.runtime.Tuple, org.sift.runtime.spi.OutputCollector)
	 */
	@Override
	public void process(Tuple tuple, OutputCollector collector) {

		Object[] values = tuple.getList(Fields.VALUES).toArray();
		//Get the tag value. Tag is the productID
		String tag = (String)values[0];
		tag = tag.substring(0, tag.indexOf(TagLineSplitterProcessor.TAG_VALUE_SEP_CHAR));
		for (Object value : tuple.getList(Fields.VALUES)) {
			for(String line : this.lineSplitterProcessor.getLines(((String)value).toLowerCase())) {
				if(line.length()>LineSplitterProcessor.minLineLength) {
					Tuple returnTuple = tuple.clone();
					//Adding a new field, TAG to hold the tag
					returnTuple.addField(Fields.TAG);
					returnTuple.setValue(Fields.VALUES, null);
					returnTuple.addToList(Fields.VALUES, line.trim());
					returnTuple.setValue(Fields.TAG, tag);
					collector.emit(returnTuple);
				}
			}	
		}
	}
}
