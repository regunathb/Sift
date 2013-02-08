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

import org.sift.runtime.Tuple;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.core.io.Resource;

/**
 * The <code>TupleLineMapper</code> is an implementation of the Spring Batch {@link LineMapper} that maps a single line into a {@link Tuple}
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class TupleLineMapper implements LineMapper<Tuple> {

	/** The Resource instance that this LineMapper is mapping lines from */
	private Resource resource;
	
	/**
	 * Interface method implementation. Maps the input line into a {@link Tuple} with line number as key and the line contents as a single String value
	 * @see org.springframework.batch.item.file.LineMapper#mapLine(java.lang.String, int)
	 */
	public Tuple mapLine(String line, int lineNumber) throws Exception {
		Tuple tuple = new Tuple(String.valueOf(lineNumber), this.resource.getFilename());
		tuple.addValue(line);
		return tuple;
	}

	/** Getter/Setter methods */
	public Resource getResource() {
		return this.resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}	

}
