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
package org.sift.runtime.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;
import org.sift.runtime.Tuple;
import org.sift.runtime.spi.OutputCollector;
import org.sift.runtime.spi.Processor;
import org.sift.winnow.StopWords;
import org.trpr.platform.core.impl.logging.LogFactory;
import org.trpr.platform.core.spi.logging.Logger;

/**
 * The <code>LuceneWordSplitterProcessor</code> is an implementation of the {@link Processor} that splits {@link Tuple} values as words using the Apache
 * Lucene libraries
 * 
 * @author Regunath B
 * @version 1.0, 31 Jan 2013
 */

public class LuceneWordSplitterProcessor implements Processor {
	
	/** Logger instance for this class*/
	private static final Logger LOGGER = LogFactory.getLogger(LuceneWordSplitterProcessor.class);
	
	/** The default Analyzer*/
	private static final Analyzer DEFAULT_ANALYZER = new StandardAnalyzer(Version.LUCENE_CURRENT);
	
	/** The n-grams to extract */
	private int nGram = StopWords.DEFAULT_N_GRAM;
	
	/** The Analyzer to use for identifying words*/
	private Analyzer analyzer = DEFAULT_ANALYZER;
	
	/** Additional stop words, if any */
	private StopWords stopWords;

	/**
	 * Interface method implementation. Emits words as {@link Tuple} values by applying the configured Lucene {@link Analyzer} on specified {@link Tuple} values
	 * @see org.sift.runtime.spi.Processor#process(org.sift.runtime.Tuple, org.sift.runtime.spi.OutputCollector)
	 */
	public void process(Tuple tuple, OutputCollector collector) {
		Tuple returnTuple = new Tuple(tuple.getKey());
		for (Object line : tuple.getValues()) {
			List<String> tokensList = new ArrayList<String>();
			try {
				TokenStream stream = this.analyzer.tokenStream(null, new StringReader(((String)line).toLowerCase()));
				while (stream.incrementToken()) { 
					tokensList.add(((TermAttribute)stream.getAttribute(TermAttribute.class)).term());
				}			
			} catch (IOException e) {
				LOGGER.error("Error parsing input line : " + line,e);
			}
			String[] tokens = tokensList.toArray(new String[0]);
			for (int i = 0; i < tokens.length; i ++) {
				StringBuffer tokenBuffer = new StringBuffer();
				for (int j = 0; j < this.getnGram(); j++) {
					if (i+j <  tokens.length) {
						tokenBuffer.append(tokens[i+j]);
						tokenBuffer.append(StopWords.WORD_BOUNDARY_STRING);
					}
					String word = tokenBuffer.toString().trim();
					if (this.getStopWords() != null && !this.getStopWords().isStopWord(word)) {
						returnTuple.addValue(tokenBuffer.toString().trim());
					}
				}
			}						
		}
		collector.emit(returnTuple);
	}

	/** Getter/Setter methods*/
	public Analyzer getAnalyzer() {
		return this.analyzer;
	}
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	public int getnGram() {
		return this.nGram;
	}
	public void setnGram(int nGram) {
		this.nGram = nGram;
	}
	public StopWords getStopWords() {
		return this.stopWords;
	}
	public void setStopWords(StopWords stopWords) {
		this.stopWords = stopWords;
	}
	
}
