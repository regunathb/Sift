package org.sift.batch.tag.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.sift.runtime.model.sentimentdata.Classification;
import org.sift.runtime.model.sentimentdata.ClassificationCollection;
import org.sift.sieve.spi.ClassificationService;
import org.trpr.platform.integration.spi.marshalling.Marshaller;

/**
 * <code>ClassificationServiceImpl</code> is an implementation of {@see ClassificationService}
 * 
 * @author devashishshankar
 * @version 1.0, 15th Feb, 2013
 */
public class ClassificationServiceImpl implements ClassificationService {
	/** URl at which sentiment API exists */
	private URL url;

	/** The parameter of the query String to which line has to be passed */
	private String urlParameter;

	/** The Marshaller implementation */
	private Marshaller marshaller;

	public ClassificationCollection getSentiment(String line) {
		try{	
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(urlParameter+"="+line);
			writer.flush();
			String retLine;
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String jsonLine="";
			while ((retLine = reader.readLine()) != null) {
				jsonLine+=retLine;
			}
			Classification sentimentData = 
					marshaller.unmarshal(jsonLine, Classification.class);
			writer.close();
			reader.close();
			reader.close();
			ClassificationCollection sentimentCollection = new ClassificationCollection();
			sentimentCollection.getClassification().add(sentimentData);
			return sentimentCollection;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** Getter/Setter methods */
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getUrlParameter() {
		return urlParameter;
	}

	public void setUrlParameter(String urlParameter) {
		this.urlParameter = urlParameter;
	}

	public Marshaller getMarshaller() {
		return marshaller;
	}

	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}

	/** End Getter/Setter methods */
}
