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
package org.sift.runtime;

import java.util.LinkedList;
import java.util.List;

/**
 * The <code>Tuple</code> represents a basic data structure in Sift. It contains a list of values (that may be ordered) and a key identifying the Tuple.
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class Tuple {

	/** The key identifying this Tuple*/
	private String key;
	
	/** List of values held by this Tuple */
	private List<String> values = new LinkedList<String>();
	
	/** Constructors*/
	public Tuple(String key) {
		this.key = key;
	}	
	public Tuple(String key, List<String> values) {
		this(key);
		this.values = values;
	}	
	
	/** Mutator methods for the Tuple values*/
	public void addValue(String value) {
		this.values.add(value);
	}	
	public boolean removeValue(String value) {
		return this.values.remove(value);
	}
	
	/** Getter/Setter methods*/
	public String getKey() {
		return this.key;
	}
	public List<String> getValues() {
		return this.values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}	
	
}
