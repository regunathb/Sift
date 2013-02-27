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
 * can be dynamically changed. 
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
		for(int i=0; i<fields.length; i++) {
			this.values.add(null);
		}
	}	
	
	/**
	 * Overriden super class method. Creates a string representation of this Tuple using its key and values
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer("TUPLE");
		buffer.append(" ");
		for (Fields field : this.getFields()) {
			buffer.append(field);
			buffer.append(":");
			buffer.append(this.getValue(field));
			buffer.append("; ");
		}

		return buffer.toString();
	}
	
	/**
	 * Dynamically adds a new field in the tuple. Starting value is null
	 * @param field {@link Fields} field to be added
	 */
	public void addField(Fields field) {
		if(this.fields.contains(field))
			return;
		this.fields.add(field);
		this.values.add(null);
	}
	
	/** Setter Methods */
	public void setValue(Fields field, Object value) {
		this.values.set(this.getFieldIndex(field), value);
	}
	
	/**
	 * Adds a value to list. If field is null, a new list is created. If field is not a list, CastException is thrown
	 * @param field Field to which value has to be added
	 * @param value value to be added
	 * 
	 */
	public void addToList(Fields field, Object value) {
		if(value==null)
			return;
		List<Object> fieldList = this.getList(field);
		if(fieldList==null) {
			fieldList = new ArrayList<Object>();
			this.setValue(field, fieldList);
		}
		fieldList.add(value);
	}
	/** End setter methods */
	
	/** Getter methods */
	
	/**
	 * Gets all the @link {Fields} in the current instance
	 * @return List of fields
	 */
	public List<Fields> getFields() {
		return this.fields;
	}
	
	/**
	 * Gets the value of the field
	 * @param field {@link Fields} of which the value is required
	 * @return Object reference
	 */
	public Object getValue(Fields field){
		if(!this.contains(field)) {
			return null;
		}
		return this.values.get(this.getFieldIndex(field));
	}
	
	/**
	 * Gets the value of an integer field
	 * @param field {@link Fields} of which the value is required
	 * @return int, A CastException is thrown if field is not an int
	 */
	public int getInt(Fields field){
		return (Integer) this.getValue(field);
	}

	/**
	 * Gets the value of a String field
	 * @param field {@link Fields} of which the value is required
	 * @return String, A CastException is thrown if field is not a String
	 */
	public String getString(Fields field){
		return (String) this.getValue(field);
	}

	/**
	 * Gets the value of a List field.
	 * @param field {@link Fields} of which the value is required
	 * @return a reference to the list, A CastException is thrown if field is not a List
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getList(Fields field) {
		if(this.getValue(field)==null) {
			List<Object> newList = new ArrayList<Object>();
			this.setValue(field, newList);
		}
		return (List<Object>) this.getValue(field);
	}
	/** End Getters */
	
	/**
	 * Checks if a field is present in the tuple
	 * @param field 
	 * @return true, if field is found
	 */
	public boolean contains(Fields field) {
		return this.fields.contains(field);
	}
	
	/**
	 * Returns a copy of the Tuple (Warning: The new tuple contains references to the old tuple in case of lists)
	 */
	public Tuple clone() {
		Tuple returnTuple = new Tuple(this.fields.toArray(new Fields[0]));
		for (Fields field:this.getFields()) {
			returnTuple.setValue(field, this.getValue(field));
		}
		return returnTuple;
	}
	
	/**
	 * Gets the index of the field. The index can be used to get the corresponding value
	 */
	private int getFieldIndex(Fields field) {
		return this.fields.indexOf(field);
	}
}

