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
package org.sift.batch.tuple;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.backportconcurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;
import org.trpr.platform.core.impl.logging.LogFactory;
import org.trpr.platform.core.spi.logging.Logger;

/**
 * The <code>MultiThreadedMultiResourceItemReader</code> class is an implementation of the Spring {@link ItemReader}, {@link ItemStream} interfaces
 * that reads batch step input data from multiple configured {@link Resource} instances. This class may be used in step configurations that use {@link ThreadPoolTaskExecutor}
 * i.e. can be used in multi-threaded reads. This class serializes access on {@link ItemReader#read()} across calling threads.
 * 
 * @author Regunath B
 * @version 1.0, 15 Apr 2013
 */
public class MultiThreadedMultiResourceItemReader <T> implements ItemReader<T>, ItemStream, InitializingBean {

	/** Logger instance for this class*/
	private static final Logger LOGGER = LogFactory.getLogger(MultiThreadedMultiResourceItemReader.class);
	
	/** Invariant to indicate invalid Resource index*/
	private static final int INVALID_INDEX = -1;
	
	/** The ResourceAwareItemReaderItemStream for reading data from Resource instances*/
	private ResourceAwareItemReaderItemStream<? extends T> delegate;
	
	/** The array of Resource instances to read data from */
	private Resource[] resources;
	
	/** The index position of the current Resource*/
	private int currentResourceIndex = INVALID_INDEX;

	/**
	 * Interface method implementation. Checks if the 'delegate' and 'resources' properties have been set
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(delegate, "The 'delegate' may not be null");
		Assert.notNull(resources, "The 'resources' may not be null");		
	}

	/**
	 * Interface method implementation. Opens Resource instance as required until all are exhausted. Reads and returns a single item using the
	 * delegate or null when all resources have been read. This method serializes access across calling calling threads.
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	public synchronized T read() throws Exception, UnexpectedInputException, ParseException,NonTransientResourceException {
		if (this.currentResourceIndex == INVALID_INDEX) {
			this.openNextAvailableResource();
		}
		T item = this.delegate.read();
		while (item == null) {
			this.openNextAvailableResource();
			if (this.currentResourceIndex >= this.getResources().length) {
				return null;
			}
			item = this.delegate.read();
		}
		return item;
	}

	/**
	 * Interface method implementation. Calls {@link ResourceAwareItemReaderItemStream#close()} on the delegate
	 * @see org.springframework.batch.item.ItemStream#close()
	 */
	public void close() throws ItemStreamException {
		this.delegate.close();
	}
	
	/**
	 * Interface method implementation. Does nothing as Resource instances are opened and read as required in {@link #read()}  
	 * configured that are yet to be processed.
	 * @see org.springframework.batch.item.ItemStream#open(org.springframework.batch.item.ExecutionContext)
	 */
	public void open(ExecutionContext ctx) throws ItemStreamException {
		// dont call open on the delegate. Open will be called as part of read
	}
	
	/**
	 * Interface method implementation. Does nothing and implies that a restart/resume will start from first resource
	 * @see org.springframework.batch.item.ItemStream#update(org.springframework.batch.item.ExecutionContext)
	 */
	public void update(ExecutionContext ctx) throws ItemStreamException {
		// does nothing
	}
	
	/**
	 * Returns the current Resource being read of null, if none
	 * @return Resource bing read
	 */
	public Resource getCurrentResource() {
		if (this.currentResourceIndex >= this.getResources().length) {
			return null;
		}
		return this.getResources()[this.currentResourceIndex];
	}
	
	/**
	 * Helper method to open the next available Resource for reading
	 * @throws Exception in case of errors in opening the next available Resource
	 */
	private void openNextAvailableResource() throws Exception {
		this.currentResourceIndex += 1;
		if (this.currentResourceIndex >= this.getResources().length) { // all Resource instances have been read
			return;
		}
		this.delegate.close();
		this.delegate.setResource(this.getResources()[this.currentResourceIndex]);
		this.getDelegate().open(new ExecutionContext());
		LOGGER.info("Opened Resource {} for read. Resource Index, Resource Length is : [" + this.currentResourceIndex +","+ this.getResources().length + "]", this.getResources()[this.currentResourceIndex]);
	}

	/** Getter/setter methods */
	public ResourceAwareItemReaderItemStream<? extends T> getDelegate() {
		return this.delegate;
	}
	public void setDelegate(ResourceAwareItemReaderItemStream<? extends T> delegate) {
		this.delegate = delegate;
	}
	public Resource[] getResources() {
		return this.resources;
	}
	public void setResources(Resource[] resources) {
		this.resources = resources;
	}		
	/** End getter/setter methods */

}