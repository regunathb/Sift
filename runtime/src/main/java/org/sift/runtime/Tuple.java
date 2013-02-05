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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The <code>Tuple</code> represents a basic data structure in Sift. It contains a list of values (that may be ordered) and a key identifying the Tuple.
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class Tuple {
	
	/** The key part separator char */
	public static final String KEY_SEP_CHAR = ":";
	
	/** Constant to denote un-defined key name prefix */
	public static final String UNDEFINED_KEY = "UNDEFINED";
	
	/** String literal constants */
	private static final String KEY = " Key = ";
	private static final String VALUES = " Values = ";
	public static final String VALUE_SEP_CHAR = ",";

	/** The key identifying this Tuple*/
	private String key;
	
	/** List of values held by this Tuple */
	private List<Object> values = new LinkedList<Object>();
	
	/** Constructors*/
	public Tuple(String key) {
		this.key = key;
	}	
	public Tuple(String key, List<Object> values) {
		this(key);
		this.values = values;
	}	
	
	/**
	 * Overriden super class method. Creates a string representation of this Tuple using its key and values
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer(KEY);
		buffer.append(this.getKey());
		buffer.append(VALUES);
		for (Object o : this.getValues()) {
			buffer.append(o + VALUE_SEP_CHAR);
		}
		return buffer.toString();
	}
	
	/** Mutator methods for the Tuple values*/
	public void addValue(Object value) {
		this.values.add(value);
	}	
	public boolean removeValue(Object value) {
		return this.values.remove(value);
	}
	
	/** Getter/Setter methods*/
	public String getKey() {
		return this.key;
	}
	public List<Object> getValues() {
		return this.values;
	}
	public void setValues(Object... values) {
		this.values.clear();
		Collections.addAll(this.values, values);
	}	
	
}
