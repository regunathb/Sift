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
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import org.sift.tagcloud.Tag;

/**
 * The <code>DisplayTag</code> is a sub-type of {@link Tag} suited for display in a tag cloud. It contains additional display related attributes such as
 * bounds, color etc.
 * 
 * @author Regunath B
 * @version 1.0, 25 Jan 2013
 */
public class DisplayTag extends Tag {
	
	/** Default list of display tag colors*/
	private static final List<Color> DEFAULT_FILL_COLORS = new LinkedList<Color>();
	
	/** Initialize with a list of default fill colors*/
	static {
		DEFAULT_FILL_COLORS.add(Color.BLUE);
		DEFAULT_FILL_COLORS.add(Color.CYAN);
		DEFAULT_FILL_COLORS.add(Color.GRAY);
		DEFAULT_FILL_COLORS.add(Color.GREEN);
		DEFAULT_FILL_COLORS.add(Color.MAGENTA);
		DEFAULT_FILL_COLORS.add(Color.ORANGE);
		DEFAULT_FILL_COLORS.add(Color.PINK);
		DEFAULT_FILL_COLORS.add(Color.YELLOW);
	}
	
	/** The default line height*/
	private static final float DEFAULT_LINE_HEIGHT = 1.0f;
	
	/** The Shape definition for displaying this Tag*/
	private Shape shape;
	
	/** The precise display bounds for this Tag*/
	private Rectangle2D bounds;
	
	/** The fill Color. Will get set when used to generate a tag cloud*/
	private Color fill=null;
	
	/** The stroke Color. Will get set when used to generate a tag cloud*/
	private Color stroke=null;
	
	/** The line height initialized to default value*/
	private float lineHeight=DEFAULT_LINE_HEIGHT;
	
	/** The font family*/
	private String fontFamily=null;
	
	/** Constructors*/
	public DisplayTag(String displayText) {
		this(displayText, Tag.DEFAULT_WEIGHT);
	}	
	public DisplayTag(String displayText, int weight) {
		super(displayText, weight);
		this.setFill(DEFAULT_FILL_COLORS.get((int)(Math.random() * DEFAULT_FILL_COLORS.size())));
		this.setStroke(this.getFill().darker());
	}	
	
	/** Start Getter/Setter methods*/
	public Shape getShape() {
		return this.shape;
	}
	public void setShape(Shape shape) {
		this.shape = shape;
	}
	public Rectangle2D getBounds() {
		return this.bounds;
	}
	public void setBounds(Rectangle2D bounds) {
		this.bounds = bounds;
	}
	public Color getFill() {
		return this.fill;
	}
	public void setFill(Color fill) {
		this.fill = fill;
	}
	public Color getStroke() {
		return this.stroke;
	}
	public void setStroke(Color stroke) {
		this.stroke = stroke;
	}
	public float getLineHeight() {
		return this.lineHeight;
	}
	public void setLineHeight(float lineHeight) {
		this.lineHeight = lineHeight;
	}
	public String getFontFamily() {
		return this.fontFamily;
	}
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}		
	/** End Getter/Setter methods*/
	
}
