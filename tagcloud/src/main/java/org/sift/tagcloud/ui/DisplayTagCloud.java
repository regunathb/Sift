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

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Random;

import org.sift.tagcloud.TagCloud;
import org.trpr.platform.core.PlatformException;

/**
 * The <code>DisplayTagCloud</code> is a sub-type of {@link TagCloud} suited for
 * generating a visual display. Portions of this code is based on Pierre
 * Lindenbaum's sample at :
 * http://plindenbaum.blogspot.in/2010/10/playing-with-worldle
 * -algorithm-tag.html
 * 
 * @author Regunath B
 * @version 1.0, 25 Jan 2013
 */
public class DisplayTagCloud<S extends DisplayTag> extends TagCloud<S> {

	/** Defaults for the display elements */
	private static final int BIGGEST_FONT_SIZE = 120;
	private static final int SMALLEST_FONT_SIZE = 20;
	private static final String FONT_FAMILY = "Serif";
	private static final boolean ROTATE_TAGS = true;
	private static final int DELTA_DEGREE = 10;
	private static final double DELTA_RADIUS = 10.0;
	private static final int BORDER = 2;

	/** Display elements initialized to defaults */
	private int biggestFontSize = BIGGEST_FONT_SIZE;
	private int smallestFontSize = SMALLEST_FONT_SIZE;
	private String fontFamily = FONT_FAMILY;
	private boolean rotateTags = ROTATE_TAGS;
	private Rectangle2D imageBounds = new Rectangle2D.Double(0, 0, 0, 0);
	private Integer preferredImageWidth;

	/** Random instance */
	private Random random = new Random();

	/**
	 * Constructor for this class.
	 * 
	 * @param subject
	 *            the String identifier for the subject of this tag cloud
	 */
	public DisplayTagCloud(String subject) {
		super(subject);
	}

	/**
	 * Interface method implementation. Lays out this tag cloud using the
	 * contained DisplayTag instances.
	 * 
	 * @see org.sift.tagcloud.TagCloud#layoutTagCloud()
	 */
	public void layoutTagCloud() throws PlatformException {

		if (this.getTagsList().isEmpty()) {
			return;
		}

		// randomize the tag positions in the tag-list
		Collections.shuffle(this.getTagsList(), this.random);

		DisplayTag first = this.getTagsList().get(0);
		double high = -Double.MAX_VALUE;
		double low = Double.MAX_VALUE;
		for (DisplayTag tag : this.getTagsList()) {
			high = Math.max(high, tag.getWeight());
			low = Math.min(low, tag.getWeight());
		}

		/* create small image */
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		/* get graphics from this image */
		Graphics2D g = Graphics2D.class.cast(img.getGraphics());
		FontRenderContext frc = g.getFontRenderContext();

		for (DisplayTag tag : this.getTagsList()) {
			String ff = tag.getFontFamily();
			if (ff == null) {
				ff = this.fontFamily;
			}
			int fontSize = (int) (((tag.getWeight() - low) / (high - low)) * (this.getBiggestFontSize() - this.getSmallestFontSize()))
					+ this.getSmallestFontSize();
			Font font = new Font(ff, Font.BOLD, fontSize);
			TextLayout textLayout = new TextLayout(tag.getDisplayText(), font, frc);
			Shape shape = textLayout.getOutline(null);
			if (this.isRotateTags() && this.random.nextBoolean()) {
				AffineTransform rotate = AffineTransform.getRotateInstance(-Math.PI / 2.0); // negative value to rotate anti-clockwise
				shape = rotate.createTransformedShape(shape);
			}
			Rectangle2D bounds = shape.getBounds2D();
			AffineTransform centerTr = AffineTransform.getTranslateInstance(
					-bounds.getCenterX(), -bounds.getCenterY());
			tag.setShape(centerTr.createTransformedShape(shape));
			tag.setBounds(tag.getShape().getBounds2D());
		}
		g.dispose();

		// first point
		Point2D.Double center = new Point2D.Double(0, 0);

		for (int i = 1; i < this.getTagsList().size(); ++i) {
			DisplayTag current = this.getTagsList().get(i);

			// calculate current center
			center.x = 0;
			center.y = 0;
			double totalWeight = 0.0;
			for (int prev = 0; prev < i; ++prev) {
				DisplayTag wPrev = this.getTagsList().get(prev);
				center.x += (wPrev.getBounds().getCenterX()) * wPrev.getWeight();
				center.y += (wPrev.getBounds().getCenterY()) * wPrev.getWeight();
				totalWeight += wPrev.getWeight();
			}
			center.x /= (totalWeight);
			center.y /= (totalWeight);

			boolean done = false;
			double radius = 0.5 * Math.min(first.getBounds().getWidth(),first.getBounds().getHeight());

			while (!done) {
				int startDeg = this.random.nextInt(360);
				// loop over spiral
				int prev_x = -1;
				int prev_y = -1;
				for (int deg = startDeg; deg < startDeg + 360; deg += DELTA_DEGREE) {
					double rad = ((double) deg / Math.PI) * 180.0;
					int cx = (int) (center.x + radius * Math.cos(rad));
					int cy = (int) (center.y + radius * Math.sin(rad));
					if (prev_x == cx && prev_y == cy) {
						continue;
					}
					prev_x = cx;
					prev_y = cy;

					AffineTransform moveTo = AffineTransform.getTranslateInstance(cx, cy);
					Shape candidate = moveTo.createTransformedShape(current.getShape());
					Rectangle2D bound1 = null;
					bound1 = new Rectangle2D.Double(current.getBounds().getX()
							+ cx, current.getBounds().getY() + cy,
							current.getBounds().getWidth(),
							current.getBounds().getHeight());
					// any collision ?
					int prev = 0;
					for (prev = 0; prev < i; ++prev) {
						if (bound1.intersects(this.getTagsList().get(prev).getBounds())) {
							break;
						}
					}
					// no collision: we're done
					if (prev == i) {
						current.setShape(candidate);
						current.setBounds(candidate.getBounds2D());
						done = true;
						break;
					}
				}
				radius += DELTA_RADIUS;
			}
		}

		double minx = Integer.MAX_VALUE;
		double miny = Integer.MAX_VALUE;
		double maxx = -Integer.MAX_VALUE;
		double maxy = -Integer.MAX_VALUE;
		for (DisplayTag tag : this.getTagsList()) {
			minx = Math.min(minx, tag.getBounds().getMinX());
			miny = Math.min(miny, tag.getBounds().getMinY());
			maxx = Math.max(maxx, tag.getBounds().getMaxX());
			maxy = Math.max(maxy, tag.getBounds().getMaxY());
		}
		AffineTransform shiftTr = AffineTransform.getTranslateInstance(-minx,-miny);
		for (DisplayTag tag : this.getTagsList()) {
			tag.setShape(shiftTr.createTransformedShape(tag.getShape()));
			tag.setBounds(tag.getShape().getBounds2D());
		}
		this.imageBounds = new Rectangle2D.Double(0, 0, maxx - minx + BORDER, maxy - miny + BORDER);
	}

	/** Start Getter/Setter methods */	
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
	public Rectangle2D getImageBounds() {
		return this.imageBounds;
	}
	public Integer getPreferredImageWidth() {
		return this.preferredImageWidth;
	}
	public void setPreferredImageWidth(Integer preferredImageWidth) {
		this.preferredImageWidth = preferredImageWidth;
	}
	/** End Getter/Setter methods */

}
