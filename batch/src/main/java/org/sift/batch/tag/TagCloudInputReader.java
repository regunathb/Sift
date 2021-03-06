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
package org.sift.batch.tag;

import java.net.URI;
import java.util.Stack;

import org.sift.runtime.Fields;
import org.sift.runtime.Tuple;
import org.sift.runtime.spi.OutputCollector;
import org.sift.tagcloud.ui.DisplayTag;
import org.sift.tagcloud.ui.DisplayTagCloud;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * The <code>TagCloudInputReader</code> is an implementation of the Spring Batch {@link ItemReader} that reads Tag cloud input data as {@link Tuple}
 * instances from an {@link OutputCollector}
 * 
 * @author Regunath B
 * @version 1.0, 31 Jan 2013
 */
public class TagCloudInputReader implements ItemReader<DisplayTagCloud<DisplayTag>> {

	/** The Monitor object for serializing thread access*/
	private static final Object MONITOR = new Object();

	/** The OutputCollector to get Tuples and their aggregated values */
	private OutputCollector collector;

	/** Stack storing more tag clouds, which are popped one by one */
	private Stack<DisplayTagCloud<DisplayTag>> tagCloudList = new Stack<DisplayTagCloud<DisplayTag>>();

	/** Factory for generating TagCLouds */
	private TagCloudFactory tagCloudFactory;

	/**
	 * Interface method implementation. Serializes read on the OutputCollector while reading Tuple data for creating TagCloud instances
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	public DisplayTagCloud<DisplayTag> read() throws Exception, UnexpectedInputException,ParseException {
		synchronized(MONITOR) {
			if(!this.tagCloudList.empty()) { //Pop the existing Tag cloud
				return this.tagCloudList.pop();
			}
			if (this.collector.getEmittedTuples().size() == 0) { //Stack is empty and collector has no more elements. Reading finished
				return null;
			}
			if(this.tagCloudList.empty()) { //Populate the stack
				String[] tupleValues = this.getSubjectAndTag(this.collector.getEmittedTuples().get(0).getString(Fields.KEY));
				String subject = tupleValues[0];
				this.tagCloudFactory = new TagCloudFactory(this.collector.getEmittedTuples().get(0));
				while(!this.collector.getEmittedTuples().isEmpty()) {
					Tuple t = this.collector.getEmittedTuples().get(0);
					tupleValues = this.getSubjectAndTag(t.getString(Fields.KEY));
					String displayText = this.getSubjectAndTag(t.getString(Fields.KEY))[1];
					if (tupleValues[0].equals(subject)) {  //Display tag is not an empty string
						if(displayText.length()>1) {
							DisplayTag displayTag = new DisplayTag(displayText, (Integer)t.getList(Fields.VALUES).get(0));
							for (Object source: t.getList(Fields.SOURCES)) {
								displayTag.getTagSourcesURIs().add((URI)source);
							}
							this.tagCloudFactory.add(t, displayTag);
							this.collector.getEmittedTuples().remove(0);
						} else {
							this.collector.getEmittedTuples().remove(0);
						}
					} else {
						break;
					}
				}
				this.tagCloudList.addAll(this.tagCloudFactory.getAll());
			} 
			return this.tagCloudList.pop();
		}
	}

	/**
	 * Helper method to get the tag cloud subject and the tag display text for a the specified Tuple key
	 * @param key the Tuple key
	 * @return String array of tag cloud subject and the tag display text
	 */
	private String[] getSubjectAndTag(String key) {
		String[] values = new String[2];
		values[0] = key.substring(0, key.indexOf(Tuple.KEY_SEP_CHAR));
		values[1] = key.substring(key.indexOf(Tuple.KEY_SEP_CHAR) + 1, key.length());
		return values;
	}

	/** Getter/Setter methods */
	public OutputCollector getCollector() {
		return this.collector;
	}
	public void setCollector(OutputCollector collector) {
		this.collector = collector;
	}

	public TagCloudFactory getTagCloudFactory() {
		return tagCloudFactory;
	}

	public void setTagCloudFactory(TagCloudFactory tagCloudFactory) {
		this.tagCloudFactory = tagCloudFactory;
	}

}
