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
package org.sift.tagcloud.ui;

import java.awt.Color;

import org.sift.tagcloud.TagCloud;

/**
 * The <code>DisplayTagCloud</code> is a sub-type of {@link TagCloud} suited for generating a 
 * visual display. This implementation generates an image file (of specified type) depicting the 
 * tag cloud.
 * 
 * @author Regunath B
 * @version 1.0, 25 Jan 2013
 */
public class DisplayTagCloud extends TagCloud {
	
	/** Defaults for the display elements */
	private static final Color FILL_COLOR = Color.YELLOW;
	private static final Color STROKE_COLOR = Color.RED;	
	private static final int BIGGEST_FONT_SIZE = 72;
	private static final int SMALLEST_FONT_SIZE = 10;
	private static final String FONT_FAMILY = "Courier";
	private static final boolean NO_ROTATE = false;

	/** Display elements initialized to defaults */
	private int biggestFontSize = BIGGEST_FONT_SIZE;
	private int smallestFontSize = SMALLEST_FONT_SIZE;
	private Color fillColor = FILL_COLOR;
	private Color strokeColor = STROKE_COLOR;
	private String fontFamily = FONT_FAMILY;
	private boolean rotateTags = NO_ROTATE;
	
	/**
	 * Constructor for this class.
	 * @param subject the String identifier for the subject of this tag cloud
	 */
	public DisplayTagCloud(String subject) {
		super(subject);
	}
	
	/** Start Getter/Setter methods*/
	public Color getFillColor() {
		return this.fillColor;
	}
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
	public Color getStrokeColor() {
		return this.strokeColor;
	}
	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}
	public String getFontFamily() {
		return this.fontFamily;
	}
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}	
	public boolean isRotateTags() {
		return this.rotateTags;
	}
	public void setRotateTags(boolean rotateTags) {
		this.rotateTags = rotateTags;
	}
	public int getBiggestFontSize() {
		return this.biggestFontSize;
	}
	public void setBiggestFontSize(int biggestFontSize) {
		this.biggestFontSize = biggestFontSize;
	}
	public int getSmallestFontSize() {
		return this.smallestFontSize;
	}
	public void setSmallestFontSize(int smallestFontSize) {
		this.smallestFontSize = smallestFontSize;
	}		
	/** End Getter/Setter methods*/

}
