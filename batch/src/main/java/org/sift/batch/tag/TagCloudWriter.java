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
package org.sift.batch.tag;

import java.util.List;

import org.sift.tagcloud.Tag;
import org.sift.tagcloud.TagCloud;
import org.sift.tagcloud.spi.service.PersistenceService;
import org.sift.tagcloud.ui.DisplayTag;
import org.sift.tagcloud.ui.DisplayTagCloud;
import org.springframework.batch.item.ItemWriter;

/**
 * The <code>TagCloudWriter</code> is an implementation of the Spring Batch {@link ItemWriter} that persists the specified {@link DisplayTagCloud} using the
 * configured {@link PersistenceService}
 * 
 * @author Regunath B
 * @version 1.0, 31 Jan 2013
 */
public class TagCloudWriter<T extends Tag, S extends TagCloud<T>> implements ItemWriter<DisplayTagCloud<DisplayTag>>{

	/** The tag cloud persistence service */
	private List<PersistenceService<T,S>> persistenceServices;

	/**
	 * Interface method implementation. Persists the 
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public void write(List<? extends DisplayTagCloud<DisplayTag>> tagClouds) throws Exception {
		System.out.println("here");
		for (DisplayTagCloud<DisplayTag> dtc : tagClouds) {
			for(PersistenceService<T,S> persistenceService: this.persistenceServices) {
				persistenceService.persistTagCloud((S)dtc);
			}
		}
	}

	/** Getter/Setter methods */
	public List<PersistenceService<T,S>> getPersistenceServices() {
		return this.persistenceServices;
	}
	public void setPersistenceServices(List<PersistenceService<T,S>> persistenceServices) {
		this.persistenceServices = persistenceServices;
	}

}
