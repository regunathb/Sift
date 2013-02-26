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
package org.sift.sieve.impl;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sift.sieve.spi.AspectFactory;

/**
 * <code> XMLFileAspectFactory </code> is an XML based implementation of {@link AspectFactory}.
 * It generates and persists from an XML File
 * 
 * @author devashishshankar
 * @version 1.0, 23rd Feb, 2013
 */
public class XMLFileAspectFactory extends AspectFactory {

	/**
	 * Constructor
	 * @param resource URI from which GroupID and Aspect information has to be loaded
	 */
	public XMLFileAspectFactory(URI resource) {
		super(resource);
	}
	
	/**
	 * Default Constructor
	 */
	public XMLFileAspectFactory() {
		super();
	}
	
	/**
	 * Interface method Implementation. XML file based implementation
	 * @see AspectFactory#persist(URI)
	 */
	@Override
	public void persist(URI resource) {

		try {
			File aspectFile = new File(resource);
			//Writing to file
			if (!aspectFile.exists()) {
				aspectFile.createNewFile();
			}
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream(resource.getPath())));
			encoder.writeObject(super.groupIDtoAspect);
			encoder.close();
		}
		catch(IOException e) {

		}
	}

	/**
	 * Interface method Implementation. XML file based implementation
	 * @see AspectFactory#generate(URI)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void generate(URI resource) {
		try {
			XMLDecoder decoder = new XMLDecoder(
					new BufferedInputStream(
							new FileInputStream(resource.getPath())));
			super.groupIDtoAspect = (Map<String, List<Aspect>>) decoder.readObject();
			decoder.close();
		}
		catch(IOException e) {
			System.err.println("Resource doesn't exist");
			super.groupIDtoAspect = new HashMap<String, List<Aspect>>();
		}
	}
	
	/** A method for testing  */
	public static void main(String args[]) throws URISyntaxException, FileNotFoundException{
		AspectFactory a = new XMLFileAspectFactory();
		a.addgroupID("100");
		Aspect asp1 = new Aspect("camera");
		asp1.getSynonyms().add("Camera");
		a.addAspectTogroupID("100", asp1);	
		a.addAspectTogroupID("100", new Aspect("battery"));
		a.persist(new URI("file:///home/devashishshankar/aspectfactory1.xml"));
		URI resource = new URI("file:///home/devashishshankar/aspectfactory1.xml");
		AspectFactory b = new XMLFileAspectFactory(resource);
		System.out.println(b.getAspect("bamera", "100"));		
	}
	
}
