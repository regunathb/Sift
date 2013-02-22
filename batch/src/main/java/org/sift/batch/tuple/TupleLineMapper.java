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
package org.sift.batch.tuple;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.sift.runtime.Fields;
import org.sift.runtime.Tuple;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * The <code>TupleLineMapper</code> is an implementation of the Spring Batch {@link LineMapper} that maps a single line into a {@link Tuple}
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class TupleLineMapper implements LineMapper<Tuple>, InitializingBean {

	/** The Resource instance that this LineMapper is mapping lines from */
	private Resource resource;
	
	/** The MultiResourceItemReader instance that uses this LineMapper via the delegate reader*/
	private MultiResourceItemReader<Tuple> itemReader;
	
	/**
	 * Interface method implementation. Maps the input line into a {@link Tuple} with line number as key and the line contents as a single String value
	 * @see org.springframework.batch.item.file.LineMapper#mapLine(java.lang.String, int)
	 */
	public Tuple mapLine(String line, int lineNumber) throws Exception {
		Resource currentResource = this.itemReader == null ? this.resource : this.itemReader.getCurrentResource();
		URI reviewURI = new URI(currentResource.getFile().getAbsolutePath()+"#"+String.valueOf(lineNumber));
		Tuple tuple = new Tuple(Fields.KEY,Fields.SOURCES,Fields.VALUES);
		//Add key
		tuple.setValue(Fields.KEY, String.valueOf(lineNumber));
		//Add sources
		List<URI> sourceURI = new ArrayList<URI>();
		sourceURI.add(reviewURI);
		tuple.setValue(Fields.SOURCES, sourceURI);
		//Add values
		List<String> initialValues = new ArrayList<String>();
		initialValues.add(line);
		tuple.setValue(Fields.VALUES, initialValues);
		return tuple;
	}

	/**
	 * Interface method implementation. Checks to see if atleast one Resource or MultiResourceItemReader is set
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (this.getResource() == null && this.itemReader == null) {
			throw new Exception("Atleast one of 'resource' or 'itemReader' must be set!");
		}
	}	

	/** Getter/Setter methods */
	public Resource getResource() {
		return this.resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public MultiResourceItemReader<Tuple> getItemReader() {
		return this.itemReader;
	}
	public void setItemReader(MultiResourceItemReader<Tuple> itemReader) {
		this.itemReader = itemReader;
	}

}
