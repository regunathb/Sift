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
package org.sift.tagcloud.impl.image.postscript;

import java.awt.Color;
import java.awt.geom.PathIterator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.UnknownFormatConversionException;

import org.sift.tagcloud.spi.image.ImageFileWriter;
import org.sift.tagcloud.ui.DisplayTag;
import org.sift.tagcloud.ui.DisplayTagCloud;
import org.trpr.platform.core.PlatformException;

/**
 * The <code>PostscriptImageWriter</code> is a sub-type of {@link ImageFileWriter} that stores tag clouds as Postscript image files
 * @see {@link DisplayTagCloud} 
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class PostscriptImageWriter <S extends DisplayTag> extends ImageFileWriter<S> {
	
	/** Constructor for this class*/
	public PostscriptImageWriter() {
		super(ImageFileWriter.POST_SCRIPT);
	}

	/**
	 * Writes the specified DisplayCloud as a Postscript image to the specified file location
	 * @see org.sift.tagcloud.spi.image.ImageFileWriter#writeImageContents(java.io.File, org.sift.tagcloud.ui.DisplayTagCloud)
	 */
	protected void writeImageContents(File file, DisplayTagCloud<S> displayTagCloud) throws PlatformException {

		PrintWriter out;
		try {
			out = new PrintWriter(file);
			out.println("%!PS-Adobe-2.0");
			out.println("%%%BoundingBox: 0 0 "+(int)displayTagCloud.getImageBounds().getWidth()+" "+(int)displayTagCloud.getImageBounds().getHeight());
			out.println("%%EndComments");
			
			for(DisplayTag tag : displayTagCloud.getTagsList()) {
			
				out.print(""+ tag.getLineHeight() + " setlinewidth");			
				out.print(" newpath");
				
				double mPenX=0;
				double mPenY=0;
				double tab[] = new double[6];
				PathIterator pathiterator = tag.getShape().getPathIterator(null);
				
				while(!pathiterator.isDone()) {
					int currSegmentType= pathiterator.currentSegment(tab);
					for(int i=1;i< tab.length;i+=2) {
						tab[i] = displayTagCloud.getImageBounds().getHeight()-tab[i];
					}
					switch(currSegmentType) {
						case PathIterator.SEG_MOVETO:
							out.print(' ');
							out.print(tab[0]);out.print(' '); out.print(tab[1]);
							out.print(" moveto");
							break;
						case PathIterator.SEG_LINETO:
							out.print(' ');
							out.print(tab[0]);out.print(' '); out.print(tab[1]);
							out.print(" lineto");
							mPenX=tab[0];
							mPenY=tab[1];
							break;
						case PathIterator.SEG_CLOSE:
							out.print(" closepath");
							break;
						case PathIterator.SEG_QUADTO:
							double lastX = mPenX;
							double lastY = mPenY;
							double c1x = lastX + (tab[0] - lastX) * 2 / 3;
							double c1y = lastY + (tab[1] - lastY) * 2 / 3;
							double c2x = tab[2] - (tab[2] - tab[0]) * 2/ 3;
							double c2y = tab[3] - (tab[3] - tab[1]) * 2/ 3;
							out.print(' ');
							out.print(c1x);out.print(' '); out.print(c1y);
							out.print(' ');
							out.print(c2x);out.print(' '); out.print(c2y);
							out.print(' ');
							out.print(tab[2]);out.print(' '); out.print(tab[3]);
							out.print(" curveto");
							mPenX = tab[2];
							mPenY = tab[3];
							break;
						case PathIterator.SEG_CUBICTO:
							out.print(' ');
							out.print(tab[0]);out.print(' '); out.print(tab[1]);
							out.print(' ');
							out.print(tab[2]);out.print(' '); out.print(tab[3]);
							out.print(' ');
							out.print(tab[4]);out.print(' '); out.print(tab[5]);
							out.print(" curveto ");
							mPenX = tab[4];
							mPenY = tab[5];
							break;
						default:
							throw new UnknownFormatConversionException("Error formatting Postscript ouput. Cannot handle : " + currSegmentType);
						}
						pathiterator.next();
					}
				
				Color c = tag.getFill();
				if(c==null) {
					c = displayTagCloud.getFillColor();
				}				
				out.print(c.getRed()/255.0);
				out.print(' ');
				out.print(c.getGreen()/255.0);
				out.print(' ');
				out.print(c.getBlue()/255.0);
				out.print(' ');
				out.print(" setrgbcolor fill");

				c = tag.getStroke();
				if(c==null) {
					c = displayTagCloud.getStrokeColor();
				}				
				out.print(c.getRed()/255.0);
				out.print(' ');
				out.print(c.getGreen()/255.0);
				out.print(' ');
				out.print(c.getBlue()/255.0);
				out.print(' ');
				out.print(" setrgbcolor stroke");
			}
		
		out.println(" showpage");
		out.flush();
		out.close();
		
		} catch (FileNotFoundException e) {
			LOGGER.error("Error writing output file for display cloud : " + displayTagCloud.getSubject() + " to file : " + file.getAbsolutePath(),e);
			throw new PlatformException("Error writing output file for display cloud : " + displayTagCloud.getSubject() + " to file : " + file.getAbsolutePath(),e);
		}
			
	}
}
