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
package org.sift.tagcloud;

import java.net.URL;

/**
 * The <code>Tag</code> represents a displayed word in a tag cloud. This class hold data about
 * the actual text, its relative weight to other words in the text source(s) and meta data like
 * title and a URL to further information regarding this tag.
 * 
 * @author Regunath B
 * @version 1.0, 24 Jan 2013
 */
public class Tag {
	
	/** Default weight associated for this Tag, if none is specified*/
	public static final int DEFAULT_WEIGHT = 1;
	
	// Mandatory attributes
	/** The display text for this tag*/
	private String displayText;
	/** The weight associated with this tag. Note that this is relative to other tags from the same source(s)*/
	private int weight;
	
	// optional attributes
	/** Title for this tag i.e. an alternate display text*/
	private String title;
	/** The tag description*/
	private String description;
	/** The tag URL pointing to a resource (or) action that this tag may lead to*/
	private URL tagURL;
	/** URL pointing to source(s) from which this tag was derived from*/
	private URL tagSourcesURL;
	
	/** Constructors*/
	public Tag(String displayText) {
		this(displayText, DEFAULT_WEIGHT);
	}	
	public Tag(String displayText, int weight) {
		this.displayText = displayText;
		this.weight = weight;
	}
	
	/**
	 * Overriden method. Returns true if the displayText and weight matches
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		Tag anotherTag = (Tag)object;
		return (this.getDisplayText().equalsIgnoreCase(anotherTag.getDisplayText()) &&
				this.getWeight() == anotherTag.getWeight());
	}
	
	/** Start Getter/Setter methods*/
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public URL getTagURL() {
		return this.tagURL;
	}
	public void setTagURL(URL tagURL) {
		this.tagURL = tagURL;
	}
	public String getDisplayText() {
		return this.displayText;
	}
	public int getWeight() {
		return this.weight;
	}	
	public URL getTagSourcesURL() {
		return this.tagSourcesURL;
	}
	public void setTagSourcesURL(URL tagSourcesURL) {
		this.tagSourcesURL = tagSourcesURL;
	}
	/** End Getter/Setter methods*/

}
