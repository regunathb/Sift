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

import java.util.ArrayList;
import java.util.List;

import org.sift.runtime.Fields;
import org.sift.runtime.Tuple;
import org.sift.runtime.spi.OutputCollector;
import org.sift.runtime.spi.Processor;

/** 
 * <code>AspectExtractionProcessor</code> is a Processor that extracts "aspects" from Review sentences.
 * "aspect" is the main keyword(s) about which the sentence is (Subject of the sentence).
 * 
 * @author devashishshankar
 * @version 1.0 19 Feb, 2013
 */
public class AspectExtractionProcessor implements Processor {

	/** Character delimiting the first value (which is tag value) */
	static public String TAG_VALUE_SEP_CHAR = "\t";


	/** Class providing methods for POS Tagging */
	private POSTagger posTagger;

	/**
	 * Interface. Method implementation. Keeps only the aspects from the tuples. Note, this method increases the number of values
	 * in the tuple. For eg, if there are 3 aspects in a line, each will be pushed as a value.
	 * @see {org.sift.runtime.spi.Processor#process}
	 */
	@Override
	public void process(Tuple tuple, OutputCollector collector) {
		tuple.getString(Fields.KEY);
		String groupID = ""; //TODO: get category
		Tuple returnTuple = tuple.clone();
		returnTuple.setValue(Fields.VALUES, null);
		//Take the first value (At this point there should be only 1 Value in the Tuple)
		Object value = tuple.getList(Fields.VALUES).get(0);
		String line = (String) value;
		String taggedLine = this.posTagger.tagLine(line);
		List<String> aspects = new ArrayList<String>();
		String currentAspect = "";
		for (String word:taggedLine.split(" ")) {
			if(this.posTagger.isNoun(word)) {
				currentAspect+=(" "+this.posTagger.untag(word));
			}
			else if(this.posTagger.isSupConj(word)) {
				if(currentAspect.length()>0) {
					currentAspect+=(" "+this.posTagger.untag(word));
				}
			}
			else {
				if(currentAspect.length()>0) {
					aspects.add(currentAspect);
					currentAspect = "";
				}
			}
		}
		for (String i : aspects) {
			returnTuple.addToList(Fields.VALUES, i);
		}
	
	collector.emit(returnTuple);
}

/** Getter/Setter methods */
public void setPosTagger(POSTagger posTagger) {
	this.posTagger = posTagger;
}
/** End Getter/Setter methods */

}
