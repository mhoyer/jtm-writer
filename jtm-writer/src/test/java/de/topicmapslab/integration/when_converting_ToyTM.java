package de.topicmapslab.integration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapExistsException;
import org.tmapix.io.JTMTopicMapWriter;
import org.tmapix.io.XTMTopicMapReader;

import de.topicmapslab.with_JTMWriter;
import de.topicmapslab.jtm.writer.JTMVersion;

/**
 * Author: mhoyer Created: 28.10.2010 01:24:45
 */
public class when_converting_ToyTM extends with_JTMWriter {
	private TopicMap toytm;
	private JTMTopicMapWriter tmapixJTMWriter;
	private final int TIMES = 3;

	@Before
	public void given_imported_ToyTM() throws TopicMapExistsException, IOException, InterruptedException {
		toytm = tms.createTopicMap("class:" + getClass().getSimpleName());
		InputStream toytmStream = ClassLoader.getSystemResourceAsStream("toytm.xtm");
		new XTMTopicMapReader(toytm, toytmStream, "toytm").read();
		tmapixJTMWriter = new org.tmapix.io.JTMTopicMapWriter(out, "base-iri");
	}

	@Test
	public void it_should_convert_all_topics() throws IOException, InterruptedException {
		for (int i = 0; i < TIMES; i++) {
			Thread.sleep(50);
			long start = new Date().getTime();
			jtmwriter.write(toytm, JTMVersion.JTM_1_0).flush();
			System.out.println("TMLab Writing took: " + (new Date().getTime() - start) + "ms.");
		}
	}

	@Test
	public void it_should_convert_all_topics_with_tmapix() throws IOException, InterruptedException {
		for (int i = 0; i < TIMES; i++) {
			Thread.sleep(50);
			long start = new Date().getTime();
			tmapixJTMWriter.write(toytm);
			out.flush();
			System.out.println("TMAPIX Writing took: " + (new Date().getTime() - start) + "ms.");
		}
	}

}
