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
package org.sift.tagcloud.impl.image.svg;

import java.awt.Color;
import java.awt.geom.PathIterator;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UnknownFormatConversionException;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.sift.tagcloud.spi.image.ImageFileWriter;
import org.sift.tagcloud.ui.DisplayTag;
import org.sift.tagcloud.ui.DisplayTagCloud;
import org.trpr.platform.core.PlatformException;

/**
 * The <code>SVGImageWriter</code> is a sub-type of {@link ImageFileWriter} that stores tag clouds as SVG image files
 * @see {@link DisplayTagCloud} 
 * 
 * @author Regunath B
 * @version 1.0, 28 Jan 2013
 */
public class SVGImageWriter<S extends DisplayTag> extends ImageFileWriter<S> {

	/** Constructor for this class*/
	public SVGImageWriter() {
		super(ImageFileWriter.SVG);
	}

	/**
	 * Writes the specified DisplayCloud as a SVG image to the specified file location
	 * @see org.sift.tagcloud.spi.image.ImageFileWriter#writeImageContents(java.io.File, org.sift.tagcloud.ui.DisplayTagCloud)
	 */
	protected void writeImageContents(File file, DisplayTagCloud<S> displayTagCloud) throws PlatformException {	
		
		final String SVG="http://www.w3.org/2000/svg";
		final String XLINK="http://www.w3.org/1999/xlink";
		XMLOutputFactory xmlfactory= XMLOutputFactory.newInstance();
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(file);
			XMLStreamWriter w= xmlfactory.createXMLStreamWriter( fout,"UTF-8");
			w.writeStartDocument("UTF-8","1.0");
			w.writeStartElement("svg","svg",SVG);
			w.writeAttribute("xmlns", XMLConstants.XML_NS_URI, "svg", SVG);
			w.writeAttribute("xmlns", XMLConstants.XML_NS_URI, "xlink", XLINK);
			w.writeAttribute("width",String.valueOf(displayTagCloud.getImageBounds().getWidth()));
			w.writeAttribute("height",String.valueOf(displayTagCloud.getImageBounds().getHeight()));
			w.writeStartElement("svg","title",SVG);
			w.writeCharacters("Tag Cloud for : " + displayTagCloud.getSubject());
			w.writeEndElement();
			
			for(DisplayTag tag : displayTagCloud.getTagsList()) {
				
				if(tag.getTagURL() != null) {
					w.writeStartElement("svg","a",SVG);
					w.writeAttribute("xlink",XLINK,"href",tag.getTagURL().toExternalForm());
				}		
				w.writeEmptyElement("svg","path",SVG);				
				if(tag.getTitle()!=null) {
					w.writeAttribute("title", tag.getTitle());
				}
			
				String style="";
				Color c = tag.getFill();
				style+="fill:"+(c==null?"none":toRGB(c))+";";
				c = tag.getStroke();
				style+="stroke:"+ (c==null ? "none" : toRGB(c)) + ";";
				style+="stroke-width:"+ tag.getLineHeight() + ";";
				w.writeAttribute("style", style);
	
				StringBuilder path=new StringBuilder();
				double tab[] = new double[6];
				PathIterator pathiterator = tag.getShape().getPathIterator(null);
				while(!pathiterator.isDone()) {
					int currSegmentType= pathiterator.currentSegment(tab);
					switch(currSegmentType) {
						case PathIterator.SEG_MOVETO:
							path.append( "M " + (tab[0]) + " " + (tab[1]) + " ");
							break;
						case PathIterator.SEG_LINETO:
							path.append( "L " + (tab[0]) + " " + (tab[1]) + " ");
							break;
						case PathIterator.SEG_CLOSE:
							path.append( "Z ");
							break;
						case PathIterator.SEG_QUADTO:
							path.append( "Q " + (tab[0]) + " " + (tab[1]));
							path.append( " "  + (tab[2]) + " " + (tab[3]));
							path.append( " ");
							break;
						case PathIterator.SEG_CUBICTO:
							path.append( "C " + (tab[0]) + " " + (tab[1]));
							path.append( " "  + (tab[2]) + " " + (tab[3]));
							path.append( " "  + (tab[4]) + " " + (tab[5]));
							path.append( " ");
							break;
						default:
							throw new UnknownFormatConversionException("Error formatting SVG ouput. Cannot handle : " + currSegmentType);
						}
					pathiterator.next();
				}
				w.writeAttribute("d", path.toString());					
				if(tag.getTagURL()!=null) {
					w.writeEndElement();
				}
			}	
			w.writeEndDocument();
			w.flush();
			w.close();
			fout.flush();
			fout.close();
		} catch (Exception e) {
			LOGGER.error("Error writing output file for display cloud : " + displayTagCloud.getSubject() + " to file : " + file.getAbsolutePath(),e);
			throw new PlatformException("Error writing output file for display cloud : " + displayTagCloud.getSubject() + " to file : " + file.getAbsolutePath(),e);
		}		
		
	}
	
	/**
	 * Helper method to get RGB values for a Color
	 * @param c the Color object
	 * @return RGB values string in SVG format
	 */
	private String toRGB(Color c) {
		return "rgb("+c.getRed()+","+c.getGreen()+","+c.getBlue()+")";
	}
	
}
