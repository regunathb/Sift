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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The <code>Tuple</code> represents a basic data structure of a named list of values in Sift. Tuple has a list of fields.
 * Each field can have a value or set of values attached to it. The list of fields is supplied at the initialisation and
 * cannot be dynamically changed. 
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 * 
 * Change log:
 * Changed the Tuple architecture, inspired by backtype.storm.tuple.TupleImpl
 * @author devashishshankar
 * @version 1.1, 21 Feb 2013
 */
public class Tuple {
	
	/** Common field names */
	public static String KEY = "key";
	public static String SOURCES = "sourceURIs";
	public static String VALUES = "values";

	/** The key part separator char */
	public static final String KEY_SEP_CHAR = ":";
	
	/** Constant to denote un-defined key name prefix */
	public static final String UNDEFINED_KEY = "UNDEFINED";
	
	/** String literal constants */
	public static final String VALUE_SEP_CHAR = ",";

	
	/** List of fields held by this tuple*/
	private List<Fields> fields;
	
	/** List of values held by this Tuple */
	private List<Object> values = new ArrayList<Object>();
	
	/**
	 * Constructor. 
	 * @param fields {@link Fields} The fields which the Tuple object will hold
	 */
	public Tuple(Fields... fields) { 
		this.fields = new ArrayList<Fields>(Arrays.asList(fields));
		this.values = new ArrayList<Object>(fields.length);
		for(int i=0; i< fields.length; i++) {
			this.values.add(null);
		}
	}	
	
	/**
	 * Overriden super class method. Creates a string representation of this Tuple using its key and values
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer(KEY);
		buffer.append(this.getValue(Fields.KEY));
		buffer.append(VALUES);		
		for (Object o : this.getList(Fields.VALUES)) {
			buffer.append(o + VALUE_SEP_CHAR);
		}
		return buffer.toString();
	}
	
	/** Setter Methods */
	public void setValue(Fields field, Object value) {
		this.values.set(this.getFieldIndex(field), value);
	}
	
	public void addToList(Fields field, Object value) {
		List<Object> fieldList = this.getList(field);
		if(fieldList==null) {
			fieldList = new ArrayList<Object>();
			this.setValue(field, fieldList);
		}
		fieldList.add(value);
	}
	/** End setter methods */
	
	/** Getter methods */
	public Object getValue(Fields field) throws RuntimeException {
		if(!this.contains(field)) {
			throw new RuntimeException("Field not found in Tuple");
		}
		return this.values.get(this.getFieldIndex(field));
	}

	public int getInt(Fields field){
		return (Integer) this.getValue(field);
	}
	
	public String getString(Fields field){
		return (String) this.getValue(field);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getList(Fields field) {
		if(this.getValue(field)==null) {
			List<Object> newList = new ArrayList<Object>();
			this.setValue(field, newList);
		}
		return (List<Object>) this.getValue(field);
	}
	public int getFieldIndex(Fields field) {
		return this.fields.indexOf(field);
	}
	
	public boolean contains(Fields field) {
		return this.fields.contains(field);
	}
	/** End getter methods */
}

