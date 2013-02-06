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
package org.sift.tagcloud.impl.image.png;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.sift.tagcloud.spi.image.ImageFileWriter;
import org.sift.tagcloud.ui.DisplayTag;
import org.sift.tagcloud.ui.DisplayTagCloud;

/**
 * The <code>PNGImageWriter</code> is a sub-type of {@link ImageFileWriter} that stores tag clouds as PNG image files
 * @see {@link DisplayTagCloud} 
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class PNGImageWriter<S extends DisplayTag> extends ImageFileWriter<S> {
	
	/** Constructor for this class*/
	public PNGImageWriter() {
		super(ImageFileWriter.PNG);
	}

	/**
	 * Writes the specified DisplayCloud as a PNG image to the specified file location
	 * @see org.sift.tagcloud.spi.image.ImageFileWriter#writeImageContents(java.io.File, org.sift.tagcloud.ui.DisplayTagCloud)
	 */
	protected void writeImageContents(File file, DisplayTagCloud<S> displayTagCloud) throws RuntimeException {
		
		AffineTransform scale = new AffineTransform();
		Dimension imageDimension=new Dimension((int)displayTagCloud.getImageBounds().getWidth(),(int)displayTagCloud.getImageBounds().getHeight());	
		
		if (displayTagCloud.getPreferredImageWidth()!=null) {
			double ratio=displayTagCloud.getPreferredImageWidth()/imageDimension.getWidth();
			imageDimension.width=displayTagCloud.getPreferredImageWidth();
			imageDimension.height=(int)(imageDimension.getHeight()*ratio);
			scale=AffineTransform.getScaleInstance(ratio, ratio);
		}		
		
		BufferedImage img=new BufferedImage(imageDimension.width, imageDimension.height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g=(Graphics2D)img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setTransform(scale);
		
		for(DisplayTag tag : displayTagCloud.getTagsList()) {
			Color c = tag.getFill();
			if(c!=null) {
				g.setColor(c);
				g.fill(tag.getShape());
			}			
			c=tag.getStroke();
			if(c!=null) {
				Stroke old=g.getStroke();
				g.setStroke(new BasicStroke(tag.getLineHeight(),BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
				g.setColor(c);
				g.draw(tag.getShape());
				g.setStroke(old);
			}
		}
		
		g.dispose();
		try {
			ImageIO.write(img, ImageFileWriter.PNG, file);
		} catch (IOException e) {
			throw new RuntimeException("Error writing output file for display cloud : " + displayTagCloud.getSubject() + " to file : " + file.getAbsolutePath(),e);
		}
		
	}

}
